package hr.algebra.sabitify.api

import android.content.Context
import android.content.Intent
import hr.algebra.sabitify.SabitifyReceiver

class SabitivyFetcher(private val context: Context) {
    fun fetchItems(count: Int) {
        Thread.sleep(3000)
        context.sendBroadcast(Intent(context, SabitifyReceiver::class.java))
    }
}