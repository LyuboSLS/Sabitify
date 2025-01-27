package hr.algebra.sabitify

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import hr.algebra.sabitify.dao.ADDRESS_TABLE
import hr.algebra.sabitify.dao.EVENT_DATE_TABLE
import hr.algebra.sabitify.dao.EVENT_LOCATION_TABLE
import hr.algebra.sabitify.dao.ITEM_TABLE_NAME
import hr.algebra.sabitify.dao.SabitifyRepository
import hr.algebra.sabitify.dao.TICKET_INFO_TABLE
import hr.algebra.sabitify.dao.VENUE_TABLE
import hr.algebra.sabitify.factory.getSabitifyRepository
import hr.algebra.sabitify.model.Item

private const val AUTHORITY = "hr.algebra.sabitify.api.provider"
val SABITIFY_PROVIDER_CONTENT_URI_ITEMS: Uri = Uri.parse("content://$AUTHORITY/$ITEM_TABLE_NAME")
val SABITIFY_PROVIDER_CONTENT_URI_EVENT_DATES: Uri =
    Uri.parse("content://$AUTHORITY/$EVENT_DATE_TABLE")
val SABITIFY_PROVIDER_CONTENT_URI_EVENT_LOCATION: Uri =
    Uri.parse("content://$AUTHORITY/$EVENT_LOCATION_TABLE")
val SABITIFY_PROVIDER_CONTENT_URI_EVENT_TICKET_INFO: Uri =
    Uri.parse("content://$AUTHORITY/$TICKET_INFO_TABLE")
val SABITIFY_PROVIDER_CONTENT_URI_EVENT_VENUE: Uri = Uri.parse("content://$AUTHORITY/$VENUE_TABLE")
val SABITIFY_PROVIDER_CONTENT_URI_EVENT_ADDRESS: Uri =
    Uri.parse("content://$AUTHORITY/$ADDRESS_TABLE")


private const val ITEMS = 10
private const val ITEM_ID = 20
private const val EVENT_DATES = 30
private const val EVENT_LOCATIONS = 40
private const val EVENT_TICKET_INFO = 50
private const val EVENT_VENUE = 60
private const val EVENT_ADDRESS = 70


private val URI_MATCHER = with(UriMatcher(UriMatcher.NO_MATCH)) {
    addURI(AUTHORITY, ITEM_TABLE_NAME, ITEMS)
    addURI(AUTHORITY, EVENT_DATE_TABLE, EVENT_DATES)
    addURI(AUTHORITY, EVENT_LOCATION_TABLE, EVENT_LOCATIONS)
    addURI(AUTHORITY, TICKET_INFO_TABLE, EVENT_TICKET_INFO)
    addURI(AUTHORITY, VENUE_TABLE, EVENT_VENUE)
    addURI(AUTHORITY, ADDRESS_TABLE, EVENT_ADDRESS)
    this
}

class SabitifyProvider : ContentProvider() {

    private lateinit var repository: SabitifyRepository


    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return when (URI_MATCHER.match(uri)) {
            ITEMS -> {
                val id = repository.insertItem(values)
                ContentUris.withAppendedId(uri, id)
            }

            EVENT_DATES -> {
                val id = repository.insertEventDate(values)
                ContentUris.withAppendedId(uri, id)
            }

            EVENT_LOCATIONS -> {
                val id = repository.insertEventLocation(values)
                ContentUris.withAppendedId(uri, id)
            }

            EVENT_TICKET_INFO -> {
                val id = repository.insertTicketInfo(values)
                ContentUris.withAppendedId(uri, id)
            }

            EVENT_VENUE -> {
                val id = repository.insertVenue(values)
                ContentUris.withAppendedId(uri, id)
            }

            EVENT_ADDRESS -> {
                val id = repository.insertAddress(values)
                ContentUris.withAppendedId(uri, id)
            }

            else -> throw IllegalArgumentException("Unsupported URI: $uri")
        }
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        when (URI_MATCHER.match(uri)) {
            ITEMS -> return repository.deleteItems(selection, selectionArgs)
            ITEM_ID -> uri.lastPathSegment?.let {
                return repository.deleteItems("${Item::_id.name}=?", arrayOf(it))
            }
        }
        throw IllegalArgumentException("wrong uri")
    }

    override fun getType(uri: Uri): String? {
        TODO(
            "Implement this to handle requests for the MIME type of the data" +
                    "at the given URI"
        )
    }


    override fun onCreate(): Boolean {
        repository = getSabitifyRepository(context)
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor = repository.queryItems(
        projection,
        selection,
        selectionArgs,
        sortOrder
    )


    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        when (URI_MATCHER.match(uri)) {
            ITEMS -> return repository.updateItems(values, selection, selectionArgs)
            ITEM_ID -> uri.lastPathSegment?.let {
                return repository.updateItems(values, "${Item::_id.name}=?", arrayOf(it))
            }
        }
        throw IllegalArgumentException("wrong uri")
    }
}