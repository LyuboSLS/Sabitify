package hr.algebra.sabitify.api

import com.google.gson.annotations.SerializedName

data class SabitifyEventVenue(
    @SerializedName("name") val name: String?,
    @SerializedName("rating") val rating: Double?,
    @SerializedName("reviews") val reviews: Int?,
    @SerializedName("link") val link: String?
)
