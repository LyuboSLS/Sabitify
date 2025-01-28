package hr.algebra.sabitify.model

data class Venue(
    val _id: Long,
    val name: String,
    val rating: Double,
    val reviews: Int,
    val link: String,
    val item_id: Long
)