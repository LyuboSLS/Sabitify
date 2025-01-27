package hr.algebra.sabitify

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import hr.algebra.nasa.framework.setBooleanPreference
import hr.algebra.nasa.framework.startActivity


class SabitifyReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        context.setBooleanPreference(DATA_IMPORTED)
        context.startActivity<HostActivity>()
    }
}