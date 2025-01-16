package hr.algebra.sabitify.api

import retrofit2.Call
import retrofit2.http.GET

const val API_URL = "https://serpapi.com/searches/ad596637b898c9bf/"

interface SabitifyApi {
    @GET("6787f0b8fbedfad21100af0b.json")
    fun fetchItems(): Call<SabitifyEvents>

}