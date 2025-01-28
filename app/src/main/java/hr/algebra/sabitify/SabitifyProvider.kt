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
import hr.algebra.sabitify.model.Address
import hr.algebra.sabitify.model.EventDate
import hr.algebra.sabitify.model.EventLocation
import hr.algebra.sabitify.model.Item
import hr.algebra.sabitify.model.TicketInfo
import hr.algebra.sabitify.model.Venue

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
private const val EVENT_DATES = 20
private const val EVENT_LOCATIONS = 30
private const val EVENT_TICKET_INFO = 40
private const val EVENT_VENUE = 50
private const val EVENT_ADDRESS = 60

private const val ITEM_ID = 15
private const val EVENT_DATE_ID = 25
private const val EVENT_LOCATION_ID = 35
private const val EVENT_TICKET_INFO_ID = 45
private const val EVENT_VENUE_ID = 55
private const val EVENT_ADDRESS_ID = 65

private val URI_MATCHER = with(UriMatcher(UriMatcher.NO_MATCH)) {
    addURI(AUTHORITY, ITEM_TABLE_NAME, ITEMS)
    addURI(AUTHORITY, EVENT_DATE_TABLE, EVENT_DATES)
    addURI(AUTHORITY, EVENT_LOCATION_TABLE, EVENT_LOCATIONS)
    addURI(AUTHORITY, TICKET_INFO_TABLE, EVENT_TICKET_INFO)
    addURI(AUTHORITY, VENUE_TABLE, EVENT_VENUE)
    addURI(AUTHORITY, ADDRESS_TABLE, EVENT_ADDRESS)

    addURI(AUTHORITY, "$ITEM_TABLE_NAME/#", ITEM_ID)
    addURI(AUTHORITY, "$EVENT_DATE_TABLE/#", EVENT_DATE_ID)
    addURI(AUTHORITY, "$EVENT_LOCATION_TABLE/#", EVENT_LOCATION_ID)
    addURI(AUTHORITY, "$TICKET_INFO_TABLE/#", EVENT_TICKET_INFO_ID)
    addURI(AUTHORITY, "$VENUE_TABLE/#", EVENT_VENUE_ID)
    addURI(AUTHORITY, "$ADDRESS_TABLE/#", EVENT_ADDRESS_ID)
    this
}

class SabitifyProvider : ContentProvider() {

    private lateinit var repository: SabitifyRepository


    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return when (URI_MATCHER.match(uri)) {
            EVENT_DATES -> {
                val id = repository.insertEventDate(values)
                ContentUris.withAppendedId(uri, id)
            }

            ITEMS -> {
                val id = repository.insertItem(values)
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
        return when (URI_MATCHER.match(uri)) {
            ITEMS -> repository.deleteItems(selection, selectionArgs)
            ITEM_ID -> uri.lastPathSegment?.let { id ->
                repository.deleteItems("${Item::_id.name}=?", arrayOf(id))
            } ?: throw IllegalArgumentException("Item ID not provided")

            EVENT_DATES -> repository.deleteEventDates(selection, selectionArgs)
            EVENT_DATE_ID -> uri.lastPathSegment?.let { id ->
                repository.deleteEventDates("${EventDate::_id.name}=?", arrayOf(id))
            } ?: throw IllegalArgumentException("Event Date ID not provided")

            EVENT_LOCATIONS -> repository.deleteEventLocations(selection, selectionArgs)
            EVENT_LOCATION_ID -> uri.lastPathSegment?.let { id ->
                repository.deleteEventLocations("${EventLocation::_id.name}=?", arrayOf(id))
            } ?: throw IllegalArgumentException("Event Location ID not provided")

            EVENT_TICKET_INFO -> repository.deleteTicketInfos(selection, selectionArgs)
            EVENT_TICKET_INFO_ID -> uri.lastPathSegment?.let { id ->
                repository.deleteTicketInfos("${TicketInfo::_id.name}=?", arrayOf(id))
            } ?: throw IllegalArgumentException("Ticket Info ID not provided")

            EVENT_VENUE -> repository.deleteVenues(selection, selectionArgs)
            EVENT_VENUE_ID -> uri.lastPathSegment?.let { id ->
                repository.deleteVenues("${Venue::_id.name}=?", arrayOf(id))
            } ?: throw IllegalArgumentException("Venue ID not provided")

            EVENT_ADDRESS -> repository.deleteAddresses(selection, selectionArgs)
            EVENT_ADDRESS_ID -> uri.lastPathSegment?.let { id ->
                repository.deleteAddresses("${Address::_id.name}=?", arrayOf(id))
            } ?: throw IllegalArgumentException("Address ID not provided")

            else -> throw IllegalArgumentException("Wrong URI: $uri")
        }
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
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor = when (URI_MATCHER.match(uri)) {
        ITEMS -> repository.queryItems(projection, selection, selectionArgs, sortOrder)
        EVENT_DATES -> repository.queryEventDate(projection, selection, selectionArgs, sortOrder)
        EVENT_LOCATIONS -> repository.queryEventLocation(
            projection,
            selection,
            selectionArgs,
            sortOrder
        )

        EVENT_TICKET_INFO -> repository.queryTicketInfo(
            projection,
            selection,
            selectionArgs,
            sortOrder
        )

        EVENT_VENUE -> repository.queryVenue(projection, selection, selectionArgs, sortOrder)
        EVENT_ADDRESS -> repository.queryAddress(projection, selection, selectionArgs, sortOrder)
        else -> throw IllegalArgumentException("Unsupported URI: $uri")
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ) = when (URI_MATCHER.match(uri)) {
        ITEMS -> repository.updateItems(values, selection, selectionArgs)
        ITEM_ID -> uri.lastPathSegment?.let { id ->
            repository.updateItems(values, "${Item::_id.name}=?", arrayOf(id))
        } ?: throw IllegalArgumentException("Item ID not provided")

        EVENT_DATES -> repository.updateEventDate(values, selection, selectionArgs)
        EVENT_DATE_ID -> uri.lastPathSegment?.let { id ->
            repository.updateEventDate(values, "${EventDate::_id.name}=?", arrayOf(id))
        } ?: throw IllegalArgumentException("Event Date ID not provided")

        EVENT_LOCATIONS -> repository.updateEventLocation(values, selection, selectionArgs)
        EVENT_LOCATION_ID -> uri.lastPathSegment?.let { id ->
            repository.updateEventLocation(values, "${EventLocation::_id.name}=?", arrayOf(id))
        } ?: throw IllegalArgumentException("Event Location ID not provided")

        EVENT_TICKET_INFO -> repository.updateTicketInfo(values, selection, selectionArgs)
        EVENT_TICKET_INFO_ID -> uri.lastPathSegment?.let { id ->
            repository.updateTicketInfo(values, "${TicketInfo::_id.name}=?", arrayOf(id))
        } ?: throw IllegalArgumentException("Ticket Info ID not provided")

        EVENT_VENUE -> repository.updateVenue(values, selection, selectionArgs)
        EVENT_VENUE_ID -> uri.lastPathSegment?.let { id ->
            repository.updateVenue(values, "${Venue::_id.name}=?", arrayOf(id))
        } ?: throw IllegalArgumentException("Venue ID not provided")

        EVENT_ADDRESS -> repository.updateAddress(values, selection, selectionArgs)
        EVENT_ADDRESS_ID -> uri.lastPathSegment?.let { id ->
            repository.updateAddress(values, "${Address::_id.name}=?", arrayOf(id))
        } ?: throw IllegalArgumentException("Address ID not provided")

        else -> throw IllegalArgumentException("Unsupported URI: $uri")
    }
}