package hr.algebra.sabitify.api

import android.content.Context
import android.util.Log
import hr.algebra.nasa.framework.sendBroadcast
import hr.algebra.sabitify.SabitifyReceiver
import hr.algebra.sabitify.handler.downloadImage
import hr.algebra.sabitify.model.EventDate
import hr.algebra.sabitify.model.EventLocation
import hr.algebra.sabitify.model.Item
import hr.algebra.sabitify.model.TicketInfo
import hr.algebra.sabitify.model.Venue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// SabitivyFetcher.kt
class SabitivyFetcher(private val context: Context) {
    private val sabitifyApi: SabitifyApi
    private val scope = CoroutineScope(Dispatchers.Main + Job())

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        sabitifyApi = retrofit.create(SabitifyApi::class.java)
    }

    fun fetchItems() {
        sabitifyApi.fetchItems().enqueue(object : Callback<SabitifyEvents> {
            override fun onResponse(
                call: Call<SabitifyEvents>,
                response: Response<SabitifyEvents>
            ) {
                response.body()?.let {
                    scope.launch {
                        val items = populateItems(it)

                        context.sendBroadcast<SabitifyReceiver>()
                    }
                }
            }

            override fun onFailure(call: Call<SabitifyEvents>, t: Throwable) {
                Log.e("FETCHER", "API call failed", t)
            }
        })
    }

    private suspend fun populateItems(sabitifyItems: SabitifyEvents): List<Item> =
        withContext(Dispatchers.IO) {
            sabitifyItems.events_results.mapNotNull { event ->
                try {
                    // Process images in parallel
                    val thumbnail = async { event.thumbnail?.let { downloadImage(context, it) } }
                    val image = async { event.image?.let { downloadImage(context, it) } }

                    Item(
                        _id = null,
                        title = event.title,
                        date = EventDate(event.date.start_date),
                        address = event.address,
                        link = event.link,
                        eventLocationMap = EventLocation(event.eventLocationMap.link),
                        description = event.description ?: "",
                        ticketInfo = event.ticketInfo.map {
                            TicketInfo(
                                it.source,
                                it.link,
                                it.link_type
                            )
                        },
                        venue = event.venue?.let {
                            Venue(
                                it.name ?: "Unknown venue",
                                it.rating ?: 0.0,
                                it.reviews ?: 0,
                                it.link ?: ""
                            )
                        } ?: Venue("Unknown venue", 0.0, 0, ""),
                        thumbnail = thumbnail.await() ?: "no image",
                        image = image.await() ?: "no image",
                        read = false
                    )
                } catch (e: Exception) {
                    Log.e("POPULATE_ITEMS", "Error processing item", e)
                    null
                }
            }
        }

    fun cancel() {
        scope.cancel()
    }
}
