package hr.algebra.sabitify.api

import com.google.gson.annotations.SerializedName

data class SabitifyEventLocationMap(
    @SerializedName("image") val image: String,
    @SerializedName("link") val link: String,
    @SerializedName("serpapi_link") val serpapi_link: String
)
