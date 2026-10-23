package com.omnicodemobile.app

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors

class MainActivity : Activity() {
    private lateinit var webView: WebView
    private val worker = Executors.newSingleThreadExecutor()
    private val mainHandler = Handler(Looper.getMainLooper())

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        webView = WebView(this)
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.settings.allowFileAccess = false
        webView.settings.allowContentAccess = false
        webView.webChromeClient = WebChromeClient()
        webView.webViewClient = WebViewClient()
        webView.addJavascriptInterface(CompilerBridge(), "OmniBridge")
        setContentView(webView)
        webView.loadUrl("file:///android_asset/index.html")
    }

    inner class CompilerBridge {
        @JavascriptInterface
        fun execute(language: String, source: String) {
            worker.execute {
                var result: String
                try {
                    val payload = JSONObject()
                        .put("language", language)
                        .put("version", "*" )
                        .put("files", org.json.JSONArray().put(JSONObject().put("content", source)))
                    val connection = (URL("https://emkc.org/api/v2/piston/execute").openConnection() as HttpURLConnection)
                    connection.requestMethod = "POST"
                    connection.connectTimeout = 12000
                    connection.readTimeout = 30000
                    connection.setRequestProperty("Content-Type", "application/json")
                    connection.doOutput = true
                    OutputStreamWriter(connection.outputStream, Charsets.UTF_8).use { it.write(payload.toString()) }
                    val stream = if (connection.responseCode in 200..299) connection.inputStream else connection.errorStream
                    val response = BufferedReader(InputStreamReader(stream, Charsets.UTF_8)).use { it.readText() }
                    val json = JSONObject(response)
                    val run = json.optJSONObject("run")
                    val compile = json.optJSONObject("compile")
                    val output = buildString {
                        if (compile != null && compile.optString("stderr").isNotBlank()) append("[compile stderr]\n").append(compile.optString("stderr")).append('\n')
                        if (run != null) {
                            if (run.optString("stdout").isNotBlank()) append(run.optString("stdout"))
                            if (run.optString("stderr").isNotBlank()) {
                                if (isNotEmpty()) append('\n')
                                append("[stderr]\n").append(run.optString("stderr"))
                            }
                            append("\n\nExit code: ").append(run.optInt("code", -1))
                        } else append(response)
                    }
                    result = output.ifBlank { "Program finished with no output.\n\n" + response }
                    connection.disconnect()
                } catch (e: Exception) {
                    result = "Online execution failed: ${e.message ?: e.javaClass.simpleName}\n\nCheck your internet connection or whether the execution service is available."
                }
                val safe = JSONObject.quote(result)
                mainHandler.post { webView.evaluateJavascript("window.showOutput($safe)", null) }
            }
        }

        @JavascriptInterface
        fun saveText(filename: String, content: String) {
            // Saving is handled by browser download/share in a later version; keep bridge intentionally minimal.
            val message = JSONObject.quote("File export is not implemented yet. Copy your code from the editor.")
            mainHandler.post { webView.evaluateJavascript("window.showOutput($message)", null) }
        }
    }

    override fun onDestroy() {
        worker.shutdownNow()
        webView.destroy()
        super.onDestroy()
    }
}
