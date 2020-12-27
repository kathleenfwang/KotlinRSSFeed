package com.example.rssfeed

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
        DownloadTask().execute(rssUrl)
        Log.d(TAG, "onCreate end")
    }

    private inner class DownloadTask : AsyncTask<String, Void, String>() {
        private val TAG ="DownloadActivity"
        override fun onPostExecute(result: String) {
            super.onPostExecute(result)
//            Log.d(TAG, "result: $result")
            val parseApplications = ParseApplications()
            parseApplications.parse(result)
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
//        val xmlResult = StringBuilder()
//        try {
//            val url = URL(urlPath)
//            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
//            val response = connection.responseCode
//            Log.d(TAG, "downloadXML: Respone code is $response")
////            val reader = BufferedReader(InputStreamReader(connection.inputStream))
////            var charsRead = 0
////            var inputBuffer = CharArray(500)
////            while (charsRead >= 0) {
////                charsRead = reader.read(inputBuffer)
////                if (charsRead > 0) {
////                    xmlResult.append(String(inputBuffer, 0, charsRead))
////                }
////            }
////            reader.close()
//            connection.inputStream.buffered().reader().use { xmlResult.append(it.readText())
//            }
//            Log.d(TAG, "Received ${xmlResult.length} bytes")
//            return xmlResult.toString()
//        }
//        catch(e: Exception) {
//                Log.e(TAG, "Error: ${e.message}")
//            }
//        // if nothing returned from try, return empty string
//        return ""
        return URL(urlPath).readText()
    }
}
