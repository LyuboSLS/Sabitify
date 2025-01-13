package hr.algebra.sabitify

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import hr.algebra.sabitify.framework.setBooleanPreferences
import hr.algebra.sabitify.framework.startActivity

class SabitifyReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        context.setBooleanPreferences(DATA_IMPORTED)
        context.startActivity<HostActivity>()
    }
}