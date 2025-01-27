package hr.algebra.sabitify.dao

import android.content.ContentValues
import android.database.Cursor

interface SabitifyRepository {
    fun deleteItems(selection: String?, selectionArgs: Array<String>?): Int
    fun deleteVenues(selection: String?, selectionArgs: Array<String>?): Int
    fun deleteEventDates(selection: String?, selectionArgs: Array<String>?): Int
    fun deleteEventLocations(selection: String?, selectionArgs: Array<String>?): Int
    fun deleteTicketInfos(selection: String?, selectionArgs: Array<String>?): Int
    fun deleteAddresses(selection: String?, selectionArgs: Array<String>?): Int
    fun deleteAllTables()

    fun recreateTables()

    fun updateItems(values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int
    fun updateVenue(values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int
    fun updateEventDate(
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int

    fun updateEventLocation(
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int

    fun updateTicketInfo(
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int

    fun updateAddress(
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int

    fun queryItems(
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor

    fun queryVenue(
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor

    fun queryEventDate(
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor

    fun queryEventLocation(
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor

    fun queryTicketInfo(
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor

    fun queryAddress(
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor

    fun insertItem(values: ContentValues?): Long
    fun insertVenue(values: ContentValues?): Long
    fun insertEventDate(values: ContentValues?): Long
    fun insertEventLocation(values: ContentValues?): Long
    fun insertTicketInfo(values: ContentValues?): Long
    fun insertAddress(values: ContentValues?): Long
}