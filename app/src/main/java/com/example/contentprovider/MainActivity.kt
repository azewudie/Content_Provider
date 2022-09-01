package com.example.contentprovider

import android.content.ContentValues.TAG
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.example.minimalistcontentprovider.Contract

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
        //test


        clickListener?.text = "Hello there"

        allWord?.setOnClickListener {
            Log.d("AllWord", getString(R.string.list_all_word))
            Toast.makeText(this, "list all word button clicked", Toast.LENGTH_LONG).show()
            clickListener?.text = "${getString(R.string.all)}\n"
        }
        firstWord?.setOnClickListener {
            Log.d("AllWord", getString(R.string.list_first_word))
            Toast.makeText(this, "list First word button clicked", Toast.LENGTH_LONG).show()
            clickListener?.text = "${getString(R.string.first)}\n"
        }
        DataProvider()
    }

    private fun DataProvider() {
        val queryUri: String = Contract.CONTENT_URI.toString()
        val projection = arrayOf<String>(Contract.CONTENT_PATH) // Only get words.
        var selectionClause: String? = null
        var selectionArgs: Array<String?>? = null

        val sortOrder: String? =
            null // For this example, we accept the order returned by the response.

        val cursor: Cursor? = contentResolver.query(
            Uri.parse(queryUri), projection, selectionClause,
            selectionArgs, sortOrder
        )

        if (cursor != null) {
            if (cursor.count > 0) {
                cursor.moveToFirst()
                val columnIndex = cursor.getColumnIndex(projection[0])
                firstWord?.setOnClickListener {
                    do {
                        val word = cursor.getString(columnIndex)
                        clickListener?.append(word.trimIndent())
                    } while (cursor.moveToNext())
                }
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



