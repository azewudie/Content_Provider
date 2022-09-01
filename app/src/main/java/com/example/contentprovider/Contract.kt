package com.android.example.minimalistcontentprovider

import android.net.Uri


object Contract {
    const val AUTHORITY = "com.android.example.minimalistcontentprovider.provider"
    const val CONTENT_PATH = "words"
    val CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + CONTENT_PATH)
    const val ALL_ITEMS = -2
    const val WORD_ID = "id"
    // MIME types for this content provider.
    // https://developer.android.com/guide/topics/providers/content-provider-creating.html#MIMETypes
    const val SINGLE_RECORD_MIME_TYPE = "vnd.android.cursor.item/vnd.com.example.provider.words"
    const val MULTIPLE_RECORDS_MIME_TYPE = "vnd.android.cursor.dir/vnd.com.example.provider.words"
}
