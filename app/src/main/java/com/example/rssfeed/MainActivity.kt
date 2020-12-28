package com.example.rssfeed

import android.content.Context
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import kotlinx.android.synthetic.main.activity_main.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL
class FeedEntry {
    var name: String = ""
    var artist: String = ""
    var releaseDate: String = ""
    var summary: String = ""
    var imageURL: String = ""
    // create a custom toString method for the FeedEntry class
    override fun toString(): String {
        return """ 
            name = $name 
            artist = $artist
            releaseDate = $releaseDate
            imageURL = $imageURL
        """.trimIndent()
    }
}

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "onCreate start")
        val rssUrl = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=10/xml"
        // android synthetically imports xmlListView so we don't have to do findViewById
        DownloadTask(this,xmlListView).execute(rssUrl)
        Log.d(TAG, "onCreate end")
    }

    private inner class DownloadTask(context: Context, listView: ListView): AsyncTask<String, Void, String>() {
        private val TAG ="DownloadActivity"

        var propContext = context
        var propListView = listView
        override fun onPostExecute(result: String) {
            super.onPostExecute(result)
//            Log.d(TAG, "result: $result")
            val parseApplications = ParseApplications()
            parseApplications.parse(result)

            var arrayAdapter = ArrayAdapter<FeedEntry>(propContext, R.layout.list_item, parseApplications.applications)
            propListView.adapter = arrayAdapter
        }

        override fun doInBackground(vararg url: String?): String {
            Log.d(TAG, "result: ${url[0]}")
            val rssFeed = downloadXML(url[0])
            if (rssFeed.isEmpty()) {
                Log.d(TAG, "doInBackground: Error in downloading")
            }
            return rssFeed
        }

    }
    private fun downloadXML(urlPath: String?): String {
        return URL(urlPath).readText()
    }
}
