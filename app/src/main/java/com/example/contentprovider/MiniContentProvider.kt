package com.example.contentprovider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import android.util.Log
import androidx.annotation.Nullable

private const val TAG = "MiniContentProvider"

class MiniContentProvider : ContentProvider() {
    private lateinit var mData: Array<String>
    override fun onCreate(): Boolean {
        initializeUriMatching()
        val context = context
        mData = context!!.resources.getStringArray(R.array.words)
        return true
    }

    private fun initializeUriMatching() {
        sUriMatcher.addURI(AUTHORITY, "$CONTENT_PATH/#", 1)
        sUriMatcher.addURI(AUTHORITY, CONTENT_PATH, 0)
    }


    @Nullable
    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        var id = -1
        when (sUriMatcher.match(uri)) {
            0 -> {
                id = ALL_ITEMS
                if (selection != null) {
                    id = selectionArgs!![0].toInt()
                }
            }
            1 -> id = uri.lastPathSegment!!.toInt()
            UriMatcher.NO_MATCH -> {
                Log.d(TAG, "NO MATCH FOR THIS URI IN SCHEME. ")
                id = id
            }
            else -> {
                Log.d(TAG, "INVALID URI - URI NOT RECOGNIZED. ")
                id = id
            }
        }
        Log.d(TAG, "query: $id")
        return populatedCursor(id)
    }

    private fun populatedCursor(id: Int): Cursor? {
        val cursor = MatrixCursor(arrayOf(CONTENT_PATH))

        if (id == ALL_ITEMS) {
            for (i in mData.indices) {
                val word = mData[i]
                cursor.addRow(arrayOf<Any>(word))
            }
        } else if (id >= 0) {
            val word = mData[id]
            cursor.addRow(arrayOf<Any>(word))
        }
        return cursor
    }

    @Nullable
    override fun getType(uri: Uri): String? {
        return when (sUriMatcher.match(uri)) {
            0 -> MULTIPLE_RECORDS_MIME_TYPE
            1 -> SINGLE_RECORD_MIME_TYPE
            else -> null
        }
    }

    @Nullable
    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        Log.d(TAG, "Not Implemented: Update Uri: $uri")
        return null
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        Log.d(TAG, "Not Implemented: Update Uri: $uri")
        return 0
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        Log.d(TAG, "Not Implemented: Update Uri: $uri")
        return 0
    }

    companion object {
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
    }
}