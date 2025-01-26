package hr.algebra.sabitify.api

import com.google.gson.annotations.SerializedName

data class SabitifyEventItem(
    @SerializedName("title") val title: String,
    @SerializedName("date") val date: SabitifyEventDate,
    @SerializedName("address") val address: List<String>,
    @SerializedName("link") val link: String,
    @SerializedName("event_location_map") val eventLocationMap: SabitifyEventLocationMap,
    @SerializedName("description") val description: String?,
    @SerializedName("ticket_info") val ticketInfo: List<SabitifyEventTicketInfo>,
    @SerializedName("venue") val venue: SabitifyEventVenue?,
    @SerializedName("thumbnail") val thumbnail: String,
    @SerializedName("image") val image: String?
)