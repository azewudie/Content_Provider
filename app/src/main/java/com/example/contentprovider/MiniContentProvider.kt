package com.android.example.minimalistcontentprovider

import android.R
import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import android.util.Log
import androidx.annotation.Nullable

 /**
 * Created by vhaecky on 5/27/16.
 *
 * The purpose of a content provider is to act as an intermediary between data and a UI.
 * As such, you can use any repository for the data you want, though an SQL database is by far one
 * of the more common ones, where the data is in tables and the response a cursor.
 *
 * However, for the purpose of this practical, and the focus on the mechanics of implementing a
 * basic content provider, this class uses generated data stored in a list.
 */
class MiniContentProvider : ContentProvider() {
    lateinit var mData: Array<String>
    override fun onCreate(): Boolean {
        // Set up the URI scheme for this content provider.
        initializeUriMatching()
        val context = context
        mData = context!!.resources.getStringArray(R.array.emailAddressTypes)
        return true
    }

    /**
     * Defines the accepted Uri schemes for this content provider.
     * Calls addURI()for all of the content URI patterns that the provide should recognize.
     */
    private fun initializeUriMatching() {
        sUriMatcher.addURI(Contract.AUTHORITY, Contract.CONTENT_PATH + "/#", 1)

        // Matches a URI that is just the authority + the path, triggering the return of all words.
        sUriMatcher.addURI(Contract.AUTHORITY, Contract.CONTENT_PATH, 0)
    }

    @Nullable
    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        var id = -1
        when (sUriMatcher.match(uri)) {
            0 -> {
                // Matches URI to get all of the entries.
                id = Contract.ALL_ITEMS
                if (selection != null) {
                    id = selectionArgs!![0].toInt()
                }
            }
            1 ->
                id = uri.lastPathSegment!!.toInt()
            UriMatcher.NO_MATCH -> {
                // You should do some error handling here.
                Log.d(TAG, "NO MATCH FOR THIS URI IN SCHEME.")
                id = -1
            }
            else -> {
                // You should do some error handling here.
                Log.d(TAG, "INVALID URI - URI NOT RECOGNZED.")
                id = -1
            }
        }
        Log.d(TAG, "query: $id")
        return populateCursor(id)
    }

    private fun populateCursor(id: Int): Cursor {
        // The query() method must return a cursor.
        // If you are not using data storage that returns a cursor,
        // you can use a simple MatrixCursor to hold the data to return.
        // https://developer.android.com/reference/android/database/MatrixCursor.html
        val cursor = MatrixCursor(arrayOf(Contract.CONTENT_PATH))

        // If there is a valid query, execute it and add the result to the cursor.
        if (id == Contract.ALL_ITEMS) {
            for (i in mData.indices) {
                val word = mData[i]
                cursor.addRow(arrayOf<Any>(word))
            }
        } else if (id >= 0) {
            // Execute the query to get the requested word.
            val word = mData[id]
            // Add the result to the cursor.
            cursor.addRow(arrayOf<Any>(word))
        }
        return cursor
    }

    // getType must be implemented.
    @Nullable
    override fun getType(uri: Uri): String? {
        return when (sUriMatcher.match(uri)) {
            0 -> Contract.MULTIPLE_RECORDS_MIME_TYPE
            1 -> Contract.SINGLE_RECORD_MIME_TYPE
            else ->                 // Alternatively, throw an exception.
                null
        }
    }

    @Nullable  // Inserts the values into the provider.
    // Returns a URI that points to the newly inserted record.
    // We will implement this method in the next practical.
    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        Log.e(TAG, "Not implemented: insert uri: $uri")
        return null
    }

    // Deletes records(s) specified by either the URI or selection/selectionArgs combo.
    // Returns the number of records affected.
    // We will implement this method in the next practical.
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        Log.e(TAG, "Not implemented: delete uri: $uri")
        return 0
    }

    // Updates records(s) specified by either the URI or selection/selectionArgs combo.
    // Returns the number of records affected.
    // We will implement this method in the next practical.
    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        Log.e(TAG, "Not implemented: update uri: $uri")
        return 0
    }

    companion object {
        private val TAG = MiniContentProvider::class.java.simpleName
        // UriMatcher is a helper class for processing the accepted Uri schemes
        // for this content provider.
        // https://developer.android.com/reference/android/content/UriMatcher.html
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
    }
}