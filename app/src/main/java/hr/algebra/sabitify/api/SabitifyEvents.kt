package hr.algebra.sabitify.api

import com.google.gson.annotations.SerializedName

data class SabitifyEvents(
    @SerializedName("events_results") val events_results: List<SabitifyEventItem>
)
