package hr.algebra.sabitify.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import hr.algebra.sabitify.model.Address
import hr.algebra.sabitify.model.EventDate
import hr.algebra.sabitify.model.EventLocation
import hr.algebra.sabitify.model.Item
import hr.algebra.sabitify.model.TicketInfo
import hr.algebra.sabitify.model.Venue


private const val DB_NAME = "Sabitify.db"
private const val DB_VERSION = 1

// Table name constants
const val ITEM_TABLE_NAME = "Item"
const val VENUE_TABLE = "Venue"
const val EVENT_DATE_TABLE = "EventDate"
const val EVENT_LOCATION_TABLE = "EventLocation"
const val TICKET_INFO_TABLE = "TicketInfo"
const val ADDRESS_TABLE = "Address"

private val CREATE_ITEM_TABLE = """
    CREATE TABLE $ITEM_TABLE_NAME (
        ${Item::_id.name} INTEGER PRIMARY KEY AUTOINCREMENT,
        ${Item::title.name} TEXT NOT NULL,
        ${Item::link.name} TEXT NOT NULL,
        ${Item::description.name} TEXT NOT NULL,
        ${Item::thumbnail.name} TEXT,
        ${Item::image.name} TEXT,
        ${Item::read.name} INTEGER NOT NULL
    )
""".trimIndent()

private val CREATE_EVENT_DATE_TABLE = """
    CREATE TABLE $EVENT_DATE_TABLE (
        ${EventDate::_id.name} INTEGER PRIMARY KEY AUTOINCREMENT,
        ${EventDate::start_date.name} TEXT,
        ${EventDate::item_id.name} INTEGER,
        FOREIGN KEY (${EventDate::item_id.name}) REFERENCES $ITEM_TABLE_NAME(${Item::_id.name})
    )
""".trimIndent()

// EventLocation table
private val CREATE_EVENT_LOCATION_TABLE = """
    CREATE TABLE $EVENT_LOCATION_TABLE (
        ${EventLocation::_id.name} INTEGER PRIMARY KEY AUTOINCREMENT,
        ${EventLocation::link.name} TEXT,
        ${EventLocation::item_id.name} INTEGER,
        FOREIGN KEY (${EventLocation::item_id.name}) REFERENCES $ITEM_TABLE_NAME(${Item::_id.name})
    )
""".trimIndent()

// Venue table
private val CREATE_VENUE_TABLE = """
    CREATE TABLE $VENUE_TABLE (
        ${Venue::_id.name} INTEGER PRIMARY KEY AUTOINCREMENT,
        ${Venue::name.name} TEXT NOT NULL,
        ${Venue::rating.name} REAL,
        ${Venue::reviews.name} INTEGER,
        ${Venue::link.name} TEXT,
        ${Venue::item_id.name} INTEGER,
        FOREIGN KEY (${Venue::item_id.name}) REFERENCES $ITEM_TABLE_NAME(${Item::_id.name})
    )
""".trimIndent()

private val CREATE_TICKET_INFO_TABLE = """
    CREATE TABLE $TICKET_INFO_TABLE (
        ${TicketInfo::_id.name} INTEGER PRIMARY KEY AUTOINCREMENT,
        ${TicketInfo::source.name} TEXT NOT NULL,
        ${TicketInfo::link.name} TEXT NOT NULL,
        ${TicketInfo::link_type.name} TEXT NOT NULL,
        ${TicketInfo::item_id.name} INTEGER,
        FOREIGN KEY (${TicketInfo::item_id.name}) REFERENCES $ITEM_TABLE_NAME(${Item::_id.name})
        
    )
""".trimIndent()

private val CREATE_ADDRESS_TABLE = """
    CREATE TABLE $ADDRESS_TABLE (
        ${Address::_id.name} INTEGER PRIMARY KEY AUTOINCREMENT,
        ${Address::street.name} TEXT NOT NULL,
        ${Address::city.name} TEXT NOT NULL,
        ${Address::item_id.name} INTEGER,
        FOREIGN KEY (${Address::item_id.name}) REFERENCES $ITEM_TABLE_NAME(${Item::_id.name})
    )
""".trimIndent()

class SabitifySqlHelper(context: Context?) : SQLiteOpenHelper(
    context,
    DB_NAME,
    null,
    DB_VERSION

), SabitifyRepository {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_VENUE_TABLE)
        db.execSQL(CREATE_EVENT_DATE_TABLE)
        db.execSQL(CREATE_EVENT_LOCATION_TABLE)
        db.execSQL(CREATE_TICKET_INFO_TABLE)
        db.execSQL(CREATE_ADDRESS_TABLE)
        db.execSQL(CREATE_ITEM_TABLE)

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        recreateTables()
    }


    override fun deleteItems(selection: String?, selectionArgs: Array<String>?) =
        writableDatabase.delete(ITEM_TABLE_NAME, selection, selectionArgs)


    override fun deleteVenues(selection: String?, selectionArgs: Array<String>?) =
        writableDatabase.delete(VENUE_TABLE, selection, selectionArgs)


    override fun deleteEventDates(selection: String?, selectionArgs: Array<String>?) =
        writableDatabase.delete(EVENT_DATE_TABLE, selection, selectionArgs)


    override fun deleteEventLocations(selection: String?, selectionArgs: Array<String>?) =
        writableDatabase.delete(EVENT_LOCATION_TABLE, selection, selectionArgs)


    override fun deleteTicketInfos(selection: String?, selectionArgs: Array<String>?) =
        writableDatabase.delete(TICKET_INFO_TABLE, selection, selectionArgs)


    override fun deleteAddresses(selection: String?, selectionArgs: Array<String>?) =
        writableDatabase.delete(ADDRESS_TABLE, selection, selectionArgs)

    override fun deleteAllTables() {
        val db = writableDatabase
        db.execSQL("DROP TABLE IF EXISTS $ADDRESS_TABLE")
        db.execSQL("DROP TABLE IF EXISTS $TICKET_INFO_TABLE")
        db.execSQL("DROP TABLE IF EXISTS $EVENT_LOCATION_TABLE")
        db.execSQL("DROP TABLE IF EXISTS $EVENT_DATE_TABLE")
        db.execSQL("DROP TABLE IF EXISTS $VENUE_TABLE")
        db.execSQL("DROP TABLE IF EXISTS $ITEM_TABLE_NAME")
    }


    override fun recreateTables() {
        val db = writableDatabase
        deleteAllTables()
        onCreate(db) // Calls onCreate to rebuild tables
    }


    override fun updateItems(
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        return writableDatabase.update(ITEM_TABLE_NAME, values, selection, selectionArgs)
    }

    override fun updateVenue(
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        return writableDatabase.update(VENUE_TABLE, values, selection, selectionArgs)
    }

    override fun updateEventDate(
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        return writableDatabase.update(EVENT_DATE_TABLE, values, selection, selectionArgs)
    }

    override fun updateEventLocation(
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        return writableDatabase.update(EVENT_LOCATION_TABLE, values, selection, selectionArgs)
    }

    override fun updateTicketInfo(
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        return writableDatabase.update(TICKET_INFO_TABLE, values, selection, selectionArgs)
    }

    override fun updateAddress(
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        return writableDatabase.update(ADDRESS_TABLE, values, selection, selectionArgs)
    }


    override fun queryItems(
        projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?,
    ): Cursor = readableDatabase.query(
        ITEM_TABLE_NAME,
        projection,
        selection,
        selectionArgs,
        null,
        null,
        sortOrder
    )

    override fun queryVenue(
        projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor {
        return readableDatabase.query(
            VENUE_TABLE,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            sortOrder
        )
    }

    override fun queryEventDate(
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor {
        return readableDatabase.query(
            EVENT_DATE_TABLE,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            sortOrder
        )
    }

    override fun queryEventLocation(
        projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor {
        return readableDatabase.query(
            EVENT_LOCATION_TABLE,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            sortOrder
        )
    }

    override fun queryTicketInfo(
        projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor {
        return readableDatabase.query(
            TICKET_INFO_TABLE,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            sortOrder
        )
    }

    override fun queryAddress(
        projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor {
        return readableDatabase.query(
            ADDRESS_TABLE,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            sortOrder
        )
    }

    override fun insertItem(values: ContentValues?): Long {
        return writableDatabase.insert(ITEM_TABLE_NAME, null, values)
    }

    // Insert method for Venue table
    override fun insertVenue(values: ContentValues?): Long {
        return writableDatabase.insert(VENUE_TABLE, null, values)
    }

    // Insert method for EventDate table
    override fun insertEventDate(values: ContentValues?): Long {
        return writableDatabase.insert(EVENT_DATE_TABLE, null, values)
    }

    // Insert method for EventLocation table
    override fun insertEventLocation(values: ContentValues?): Long {
        return writableDatabase.insert(EVENT_LOCATION_TABLE, null, values)
    }

    // Insert method for TicketInfo table
    override fun insertTicketInfo(values: ContentValues?): Long {
        return writableDatabase.insert(TICKET_INFO_TABLE, null, values)
    }

    // Insert method for Address table
    override fun insertAddress(values: ContentValues?): Long {
        return writableDatabase.insert(ADDRESS_TABLE, null, values)
    }

}