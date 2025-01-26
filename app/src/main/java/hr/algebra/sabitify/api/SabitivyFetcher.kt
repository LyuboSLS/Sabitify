package hr.algebra.sabitify.api

import android.content.Context
import android.util.Log
import hr.algebra.nasa.framework.sendBroadcast
import hr.algebra.sabitify.SabitifyReceiver
import hr.algebra.sabitify.model.EventDate
import hr.algebra.sabitify.model.EventLocation
import hr.algebra.sabitify.model.Item
import hr.algebra.sabitify.model.TicketInfo
import hr.algebra.sabitify.model.Venue
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SabitivyFetcher(private val context: Context) {
    private val sabitifyApi: SabitifyApi

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        sabitifyApi = retrofit.create(SabitifyApi::class.java)
    }

    fun fetchItems() {
        val request = sabitifyApi.fetchItems()


        request.enqueue(/* callback = */ object : Callback<SabitifyEvents> {
            override fun onResponse(
                call: Call<SabitifyEvents>,
                response: Response<SabitifyEvents>
            ) {
                response?.body().let { populateItems(it!!) }
            }

            override fun onFailure(call: Call<SabitifyEvents>, t: Throwable) {
                Log.d("DOWNLOAD", t.message, t)
            }
        })
    }

    private fun populateItems(sabitifyItems: SabitifyEvents) {
        var items = mutableListOf<Item>()

        sabitifyItems.events_results.forEach {
            val ticketInfos = mutableListOf<TicketInfo>()
            it.ticketInfo.forEach { info ->
                ticketInfos.add(
                    TicketInfo(
                        info.source,
                        info.link,
                        info.link_type
                    )
                )
            }
            items.add(
                Item(
                    _id = null,
                    title = it.title,
                    date = EventDate(it.date.start_date),
                    address = it.address,
                    link = it.link,
                    eventLocationMap = EventLocation(it.eventLocationMap.link),
                    description = it.description ?: "no description",
                    ticketInfo = ticketInfos,
                    venue = it.venue?.let { venue ->
                        Venue(
                            name = venue.name ?: "Unknown venue",
                            rating = venue.rating ?: 0.0,
                            reviews = venue.reviews ?: 0,
                            link = venue.link ?: ""
                        )
                    } ?: Venue(
                        name = "Unknown venue",
                        rating = 0.0,
                        reviews = 0,
                        link = ""
                    ),
                    thumbnail = it.thumbnail ?: "no image",
                    image = it.image ?: "no image",
                    read = false
                )
            )

        }
        context.sendBroadcast<SabitifyReceiver>()
    }
}


