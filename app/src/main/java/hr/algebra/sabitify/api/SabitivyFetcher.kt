package hr.algebra.sabitify.api

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.database.SQLException
import android.util.Log
import hr.algebra.nasa.framework.sendBroadcast
import hr.algebra.sabitify.SABITIFY_PROVIDER_CONTENT_URI_EVENT_ADDRESS
import hr.algebra.sabitify.SABITIFY_PROVIDER_CONTENT_URI_EVENT_DATES
import hr.algebra.sabitify.SABITIFY_PROVIDER_CONTENT_URI_EVENT_LOCATION
import hr.algebra.sabitify.SABITIFY_PROVIDER_CONTENT_URI_EVENT_TICKET_INFO
import hr.algebra.sabitify.SABITIFY_PROVIDER_CONTENT_URI_EVENT_VENUE
import hr.algebra.sabitify.SABITIFY_PROVIDER_CONTENT_URI_ITEMS
import hr.algebra.sabitify.SabitifyReceiver
import hr.algebra.sabitify.handler.downloadImage
import hr.algebra.sabitify.model.Address
import hr.algebra.sabitify.model.EventDate
import hr.algebra.sabitify.model.EventLocation
import hr.algebra.sabitify.model.Item
import hr.algebra.sabitify.model.TicketInfo
import hr.algebra.sabitify.model.Venue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
        sabitifyApi.fetchItems().enqueue(object : Callback<SabitifyEvents> {
            override fun onResponse(
                call: Call<SabitifyEvents>,
                response: Response<SabitifyEvents>
            ) {
                response.body()?.let { populateItems(it) }
            }

            override fun onFailure(call: Call<SabitifyEvents>, t: Throwable) {
                Log.e("FETCHER", "API call failed", t)
            }
        })
    }

    private fun populateItems(sabitifyItems: SabitifyEvents) {
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            sabitifyItems.events_results.forEach { eventItem ->
                try {
                    // Download images first
                    val thumbnailPath = downloadImage(context, eventItem.thumbnail)
                    val imagePath = eventItem.image?.let { downloadImage(context, it) }

                    // 1. Insert EventDate
                    val eventDateId = insertEventDate(eventItem.date).takeIf { it != -1L }
                        ?: throw SQLException("Failed to insert EventDate")

                    // 2. Insert EventLocation
                    val eventLocationId =
                        insertEventLocation(eventItem.eventLocationMap).takeIf { it != -1L }
                            ?: throw SQLException("Failed to insert EventLocation")

                    // 3. Insert Venue (if present)
                    val venueId = eventItem.venue?.let { insertVenue(it) } ?: -1L

                    // 4. Insert Main Item
                    val itemValues = ContentValues().apply {
                        put(Item::title.name, eventItem.title)
                        put("date_id", eventDateId)
                        put("eventLocationMap_id", eventLocationId)
                        put("venue_id", if (venueId != -1L) venueId else null)
                        put(Item::link.name, eventItem.link)
                        put(Item::description.name, eventItem.description ?: "")
                        put(Item::thumbnail.name, thumbnailPath ?: "")
                        put(Item::image.name, imagePath ?: "")
                        put(Item::read.name, false)
                    }

                    val itemUri = context.contentResolver.insert(
                        SABITIFY_PROVIDER_CONTENT_URI_ITEMS,
                        itemValues
                    ) ?: throw SQLException("Failed to insert Item")
                    val itemId = ContentUris.parseId(itemUri)

                    // 5. Insert Address
                    val addressValues = ContentValues().apply {
                        put(Address::street.name, eventItem.address.getOrNull(0) ?: "")
                        put(Address::city.name, eventItem.address.getOrNull(1) ?: "")
                        put(Address::item_id.name, itemId)
                    }
                    context.contentResolver.insert(
                        SABITIFY_PROVIDER_CONTENT_URI_EVENT_ADDRESS,
                        addressValues
                    ) ?: throw SQLException("Failed to insert Address")

                    // 6. Insert Ticket Info
                    eventItem.ticketInfo.forEach { ticket ->
                        val ticketValues = ContentValues().apply {
                            put(TicketInfo::source.name, ticket.source)
                            put(TicketInfo::link.name, ticket.link)
                            put(TicketInfo::link_type.name, ticket.link_type)
                            put(TicketInfo::item_id.name, itemId)
                        }
                        context.contentResolver.insert(
                            SABITIFY_PROVIDER_CONTENT_URI_EVENT_TICKET_INFO,
                            ticketValues
                        ) ?: throw SQLException("Failed to insert TicketInfo")
                    }

                } catch (e: Exception) {
                    Log.e("PopulateItems", "Error processing item ${eventItem.title}", e)
                }
            }
            context.sendBroadcast<SabitifyReceiver>()
        }
    }

    // Helper functions for nested object insertion
    private fun insertEventDate(date: SabitifyEventDate): Long {
        val values = ContentValues().apply {
            put(EventDate::start_date.name, date.start_date)
        }
        // Use the dedicated URI for EventDate
        return context.contentResolver.insert(
            SABITIFY_PROVIDER_CONTENT_URI_EVENT_DATES,
            values
        )?.let { ContentUris.parseId(it) } ?: -1L
    }

    private fun insertEventLocation(location: SabitifyEventLocationMap): Long {
        val values = ContentValues().apply {
            put(EventLocation::link.name, location.link)
        }
        return context.contentResolver.insert(
            SABITIFY_PROVIDER_CONTENT_URI_EVENT_LOCATION,
            values
        )?.let { ContentUris.parseId(it) } ?: -1L
    }

    private fun insertVenue(venue: SabitifyEventVenue): Long {
        val values = ContentValues().apply {
            put(Venue::name.name, venue.name ?: "")
            put(Venue::rating.name, venue.rating ?: 0.0)
            put(Venue::reviews.name, venue.reviews ?: 0)
            put(Venue::link.name, venue.link ?: "")
        }
        return context.contentResolver.insert(
            SABITIFY_PROVIDER_CONTENT_URI_EVENT_VENUE,
            values
        )?.let { ContentUris.parseId(it) } ?: -1L
    }
}
