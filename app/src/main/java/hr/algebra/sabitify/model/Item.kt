package hr.algebra.sabitify.model


data class Item(
    var _id: Long?,
    val title: String,
    val date: EventDate,
    val address: Address,
    val link: String,
    val eventLocationMap: EventLocation,
    val description: String,
    val ticketInfo: List<TicketInfo>,
    val venue: Venue,
    val thumbnail: String?,
    val image: String?,
    val read: Boolean

)
