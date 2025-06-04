package hr.algebra.sabitify.framework

import android.annotation.SuppressLint
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnimationUtils
import androidx.core.content.getSystemService
import androidx.preference.PreferenceManager
import hr.algebra.sabitify.SABITIFY_PROVIDER_CONTENT_URI_EVENT_ADDRESS
import hr.algebra.sabitify.SABITIFY_PROVIDER_CONTENT_URI_EVENT_DATES
import hr.algebra.sabitify.SABITIFY_PROVIDER_CONTENT_URI_EVENT_LOCATION
import hr.algebra.sabitify.SABITIFY_PROVIDER_CONTENT_URI_EVENT_TICKET_INFO
import hr.algebra.sabitify.SABITIFY_PROVIDER_CONTENT_URI_EVENT_VENUE
import hr.algebra.sabitify.SABITIFY_PROVIDER_CONTENT_URI_ITEMS
import hr.algebra.sabitify.model.Address
import hr.algebra.sabitify.model.EventDate
import hr.algebra.sabitify.model.EventLocation
import hr.algebra.sabitify.model.Item
import hr.algebra.sabitify.model.TicketInfo
import hr.algebra.sabitify.model.Venue

fun View.applyAnimation(id: Int) {
    startAnimation(AnimationUtils.loadAnimation(context, id))
}

inline fun <reified T : Activity> Context.startActivity() {
    startActivity(
        Intent(this, T::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    )
}
inline fun <reified T : Activity> Context.startActivity(
    key: String,
    value: Int
) {
    startActivity(
        Intent(this, T::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            putExtra(key, value)
        }
    )
}

inline fun <reified T : BroadcastReceiver> Context.sendBroadcast() {
    sendBroadcast(Intent(this, T::class.java))
}

fun Context.setBooleanPreference(key: String, value: Boolean = true) {
    PreferenceManager.getDefaultSharedPreferences(this)
        .edit()
        .putBoolean(key, value)
        .apply()
}

fun Context.getBooleanPreference(key: String) =
    PreferenceManager.getDefaultSharedPreferences(this)
        .getBoolean(key, false)

fun Context.isOnline(): Boolean {
    val connectivityManager = getSystemService<ConnectivityManager>()
    connectivityManager?.activeNetwork?.let { network ->
        connectivityManager.getNetworkCapabilities(network)?.let { cap ->
            return cap.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                    || cap.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
        }
    }

    return false
}

fun callDelayed(delay: Long, work: Runnable) {
    Handler(Looper.getMainLooper()).postDelayed(
        work,
        delay
    )
}

@SuppressLint("Range")
fun Context.fetchItems(): MutableList<Item> {
    val items = mutableListOf<Item>()

    val cursorItems = contentResolver.query(
        SABITIFY_PROVIDER_CONTENT_URI_ITEMS,
        null,
        null,
        null,
        null
    )

    cursorItems?.use { cursor ->
        while (cursor.moveToNext()) {
            val itemId = cursor.getLong(cursor.getColumnIndex(Item::_id.name))
            val title = cursor.getString(cursor.getColumnIndex(Item::title.name))
            val link = cursor.getString(cursor.getColumnIndex(Item::link.name))
            val thumbnail = cursor.getString(cursor.getColumnIndex(Item::thumbnail.name))
            val image = cursor.getString(cursor.getColumnIndex(Item::image.name))
            val description = cursor.getString(cursor.getColumnIndex(Item::description.name))
            val liked = cursor.getInt(cursor.getColumnIndex(Item::liked.name))

            val date = fetchEventDate(itemId)
            val venue = fetchVenue(itemId)
            val address = fetchAddress(itemId)
            val ticketInfo = fetchTicketInfo(itemId)
            val eventLocation = fetchEventLocation(itemId)

            items.add(
                Item(
                    _id = itemId,
                    title = title,
                    date = date,
                    address = address,
                    link = link,
                    description = description,
                    thumbnail = thumbnail,
                    image = image,
                    liked = liked != 0,
                    eventLocationMap = eventLocation,
                    ticketInfo = ticketInfo,
                    venue = venue

                )
            )
        }
    }

    return items
}

@SuppressLint("Range")
private fun Context.fetchEventDate(itemId: Long): EventDate? {
    val cursor = contentResolver.query(
        SABITIFY_PROVIDER_CONTENT_URI_EVENT_DATES,
        null,
        "${EventDate::item_id.name} = ?",
        arrayOf(itemId.toString()),
        null
    )
    return cursor?.use {
        if (it.moveToFirst()) {
            EventDate(
                _id = it.getLong(it.getColumnIndex(EventDate::_id.name)),
                start_date = it.getString(it.getColumnIndex(EventDate::start_date.name)),
                item_id = itemId
            )
        } else {
            null
        }
    }
}

@SuppressLint("Range")
private fun Context.fetchVenue(itemId: Long): Venue? {
    val cursor = contentResolver.query(
        SABITIFY_PROVIDER_CONTENT_URI_EVENT_VENUE,
        null,
        "${Venue::item_id.name} = ?",
        arrayOf(itemId.toString()),
        null
    )
    return cursor?.use {
        if (it.moveToFirst()) {
            Venue(
                _id = it.getLong(it.getColumnIndex(Venue::_id.name)),
                name = it.getString(it.getColumnIndex(Venue::name.name)),
                reviews = it.getInt(it.getColumnIndex(Venue::reviews.name)),
                link = it.getString(it.getColumnIndex(Venue::link.name)),
                item_id = itemId,
                rating = it.getDouble(it.getColumnIndex(Venue::rating.name))
            )
        } else {
            null
        }
    }
}

@SuppressLint("Range")
private fun Context.fetchAddress(itemId: Long): Address? {
    val cursor = contentResolver.query(
        SABITIFY_PROVIDER_CONTENT_URI_EVENT_ADDRESS,
        null,
        "${Address::item_id.name} = ?",
        arrayOf(itemId.toString()),
        null
    )
    return cursor?.use {
        if (it.moveToFirst()) {
            Address(
                _id = it.getLong(it.getColumnIndex(Address::_id.name)),
                street = it.getString(it.getColumnIndex(Address::street.name)),
                city = it.getString(it.getColumnIndex(Address::city.name)),
                item_id = itemId
            )
        } else {
            null
        }
    }
}

@SuppressLint("Range")
private fun Context.fetchTicketInfo(itemId: Long): MutableList<TicketInfo> {
    val ticketInfo = mutableListOf<TicketInfo>()
    val cursor = contentResolver.query(
        SABITIFY_PROVIDER_CONTENT_URI_EVENT_TICKET_INFO,
        null,
        "${TicketInfo::item_id.name} = ?",
        arrayOf(itemId.toString()),
        null
    )
    cursor?.use {
        while (it.moveToNext()) {
            ticketInfo.add(
                TicketInfo(
                    _id = it.getLong(it.getColumnIndex(TicketInfo::_id.name)),
                    source = it.getString(it.getColumnIndex(TicketInfo::source.name)),
                    link = it.getString(it.getColumnIndex(TicketInfo::link.name)),
                    link_type = it.getString(it.getColumnIndex(TicketInfo::link_type.name)),
                    item_id = itemId
                )
            )
        }
    }
    return ticketInfo
}

@SuppressLint("Range")
private fun Context.fetchEventLocation(itemId: Long): EventLocation? {
    val cursor = contentResolver.query(
        SABITIFY_PROVIDER_CONTENT_URI_EVENT_LOCATION,
        null,
        "${EventLocation::item_id.name} = ?",
        arrayOf(itemId.toString()),
        null
    )
    return cursor?.use {
        if (it.moveToFirst()) {
            EventLocation(
                _id = it.getLong(it.getColumnIndex(EventLocation::_id.name)),
                item_id = itemId,
                link = it.getString(it.getColumnIndex(EventLocation::link.name))
            )
        } else {
            null
        }
    }
}

