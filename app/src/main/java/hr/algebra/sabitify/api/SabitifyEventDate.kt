package hr.algebra.sabitify.api

import com.google.gson.annotations.SerializedName

data class SabitifyEventDate(
    @SerializedName("start_date") val start_date: String,
    @SerializedName("when") val val_when: String

)
