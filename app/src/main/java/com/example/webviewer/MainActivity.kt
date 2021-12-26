package com.example.webviewer

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import java.io.IOException
import java.io.InputStream
import android.graphics.Bitmap

import android.webkit.WebResourceRequest

import android.webkit.WebViewClient
import androidx.webkit.WebViewAssetLoader
import android.net.http.SslError
import android.view.Window
import android.view.WindowManager

import android.webkit.SslErrorHandler




class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)

        val webView = WebView(this)
        webView.settings.javaScriptEnabled = true
        webView.addJavascriptInterface(WebAppInterface(this), "Android")
        setContentView(webView)

        var page = readTextFromAsset("index.html")
//        webView.loadUrl("http://192.168.69.103:8000/")
//        webView.loadUrl("https://infinite-galactic.myshopify.com/")
        webView.loadUrl("https://realityexpander.github.io/tesla/index.html")
//        webView.loadUrl("https://realityexpander.github.io/index.html")
//        val baseURL = "https://realityexpander.github.io/"
//        webView.loadDataWithBaseURL(baseURL, page, "text/html", null, baseURL)
//        webView.loadDataWithBaseURL(null, page, "text/html", null, null)

        webView.webChromeClient = object: WebChromeClient() {
            override fun onConsoleMessage(message: String, lineNumber: Int, sourceID: String?) {
                Log.i("WebViewConsole", "$message, line:$lineNumber")
            }
        }
//        webView.getSettings().loadsImagesAutomatically = true;
//        webView.getSettings().domStorageEnabled = true;
//        webView.getSettings().allowContentAccess = true;
//        webView.getSettings().allowFileAccess = true;


        var loadingFinished = false
        webView.webViewClient = object : WebViewClient() {

//            override fun onPageStarted(
//                view: WebView, url: String, favicon: Bitmap
//            ) {
//                super.onPageStarted(view, url, favicon)
//                loadingFinished = false
//                //SHOW LOADING IF IT ISNT ALREADY VISIBLE
//            }

            override fun onPageFinished(view: WebView, url: String) {
                loadingFinished = true
                //HIDE LOADING IT HAS FINISHED

                //webView.evaluateJavascript("document.body.style.background = 'blue';", null)

//                webView.evaluateJavascript("(function() { return document.getElementById('toastMessage').value; })();") { returnValue ->
//                    Toast.makeText(applicationContext, returnValue, Toast.LENGTH_SHORT).show()
//                }
//
//                webView.evaluateJavascript("getToastMessage();") { returnValue ->
//                    Toast.makeText(applicationContext, returnValue, Toast.LENGTH_SHORT).show()
//                }
            }

            override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler, er: SslError?) {
                handler.proceed()
            }
        }

    }

    fun loadData(inFile: String): String {
        var tContents: String = ""
        try {
            val stream: InputStream = assets.open(inFile)
            val size: Int = stream.available()
            val buffer = ByteArray(size)
            stream.read(buffer)
            stream.close()
            tContents = String(buffer)
        } catch (e: IOException) {
            // Handle exceptions here
        }
        return tContents
    }
}

/** Instantiate the interface and set the context  */
class WebAppInterface(private val mContext: Context) {

    /** Show a toast from the web page  */
    @JavascriptInterface
    fun showToast(toast: String) {
        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show()
    }
}

fun Context.readTextFromAsset(fileName : String) : String{
    return assets.open(fileName).bufferedReader().use {
        it.readText()}
}