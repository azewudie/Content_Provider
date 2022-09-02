package com.example.contentprovider

import android.content.ContentValues.TAG
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {


    private var allWord: Button? = null
    private var firstWord: Button? = null
    private var clickListener: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        allWord = findViewById(R.id.bt_allWords)
        firstWord = findViewById(R.id.bt_firstWords)
        clickListener = findViewById(R.id.tv_clickListener)


        allWord?.setOnClickListener(this::onClickDisplayEntries)
        firstWord?.setOnClickListener(this::onClickDisplayEntries)

    }

    private fun onClickDisplayEntries(view: View) {
        val queryUri: String = CONTENT_URI.toString()
        val projection = arrayOf<String>(CONTENT_PATH)
        val selectionClause: String?
        val selectionArgs: Array<String>?
        val sortOrder: String? = null

        when (view.id) {
            R.id.bt_allWords -> {
                selectionClause = null
                selectionArgs = null
            }
            R.id.bt_firstWords -> {
                selectionClause = "$WORD_ID=?"
                selectionArgs = arrayOf("0")
            }
            else -> {
                selectionClause = null
                selectionArgs = null
            }
        }

        val cursor: Cursor? = contentResolver.query(
            Uri.parse(queryUri), projection, selectionClause,
            selectionArgs, sortOrder
        )

        if (cursor != null) {
            if (cursor.count > 0) {
                cursor.moveToFirst()
                val columnIndex = cursor.getColumnIndex(projection[0])
                do {
                    val word = cursor.getString(columnIndex)
                    clickListener?.append(word.trimIndent())
                } while (cursor.moveToNext())
            } else {
                Log.d(TAG, "onClickDisplayEntries: " + " no data returned")
                clickListener?.append("No data returned".trimIndent())
            }
            cursor.close()
        } else {
            Log.d(TAG, "onClickDisplayEntries: " + "Cursor is null")
            clickListener?.append("Cursor is null".trimIndent())
        }

    }

}