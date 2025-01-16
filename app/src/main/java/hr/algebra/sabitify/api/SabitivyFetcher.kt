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
                    date = EventDate(it.date.val_when),
                    address = it.address,
                    link = it.link,
                    eventLocationMap = EventLocation(it.eventLocationMap.link),
                    description = it.description,
                    ticketInfo = ticketInfos,
                    venue = Venue(
                        it.venue.name,
                        it.venue.rating,
                        it.venue.reviews,
                        it.venue.link
                    ),
                    thumbnail = null,
                    image = null,
                    read = false
                )
            )
        }
        context.sendBroadcast<SabitifyReceiver>()
    }
}


