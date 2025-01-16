package hr.algebra.sabitify.api

import com.google.gson.annotations.SerializedName

data class SabitifyEventTicketInfo(
    @SerializedName("source") val source: String,
    @SerializedName("link") val link: String,
    @SerializedName("link_type") val link_type: String
)
