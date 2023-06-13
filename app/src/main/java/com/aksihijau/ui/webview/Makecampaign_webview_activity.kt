package com.aksihijau.ui.webview

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.widget.Toolbar
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.aksihijau.R
import com.aksihijau.databinding.ActivityMakecampaignWebviewBinding
import com.aksihijau.datastore.TokenPreferences
import com.aksihijau.datastore.TokenViewModel
import com.aksihijau.datastore.TokenViewModelFactory

class Makecampaign_webview_activity : AppCompatActivity() {
    private lateinit var binding : ActivityMakecampaignWebviewBinding
    private lateinit var tokenViewModel: TokenViewModel
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "token")
    private var filePathCallback: ValueCallback<Array<Uri>>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMakecampaignWebviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar.root as Toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setTitle("Membuat Campaign Baru")
        }
        binding.toolbar.root.setTitleTextColor(resources.getColor(R.color.white))

        val tokenPreferences = TokenPreferences.getInstance(dataStore)
        val tokenViewModelFactory = TokenViewModelFactory(tokenPreferences)
        tokenViewModel = ViewModelProvider(this, tokenViewModelFactory).get(TokenViewModel::class.java)

        binding.wvMakecampaign.webViewClient = WebViewClient()
        loadWebViewWithToken()
    }

    private fun loadWebViewWithToken() {
        tokenViewModel.getToken().observe(this, { token ->
            tokenViewModel.getRefreshToken().observe(this, { refreshToken ->
                val url = "https://aksihijau.vercel.app/?accessToken=$token&refreshToken=$refreshToken"
                binding.wvMakecampaign.loadUrl(url)
            })
        })

        val webSettings = binding.wvMakecampaign.settings
        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true
        webSettings.allowFileAccess = true
        webSettings.allowContentAccess = true
        webSettings.allowFileAccessFromFileURLs = true
        webSettings.allowUniversalAccessFromFileURLs = true

        binding.wvMakecampaign.webChromeClient = object : WebChromeClient() {
            override fun onShowFileChooser(
                webView: WebView?,
                filePathCallback: ValueCallback<Array<Uri>>?,
                fileChooserParams: FileChooserParams?
            ): Boolean {
                if (filePathCallback == null) return false

                this@Makecampaign_webview_activity.filePathCallback = filePathCallback

                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "*/*"
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                intent.addCategory(Intent.CATEGORY_OPENABLE)

                val chooserIntent = Intent.createChooser(intent, "Choose File")
                startActivityForResult(chooserIntent, FILE_CHOOSER_REQUEST_CODE)

                return true
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == FILE_CHOOSER_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val results = WebChromeClient.FileChooserParams.parseResult(resultCode, data)
                val filePaths = results?.map { Uri.parse(it.toString()) }?.toTypedArray()
                this@Makecampaign_webview_activity.filePathCallback?.onReceiveValue(filePaths)
                this@Makecampaign_webview_activity.filePathCallback = null
            } else {
                this@Makecampaign_webview_activity.filePathCallback?.onReceiveValue(null)
                this@Makecampaign_webview_activity.filePathCallback = null
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        if(binding.wvMakecampaign.canGoBack()){
            binding.wvMakecampaign.goBack()
        } else {
            super.onBackPressed()
        }
    }

    companion object {
        private const val FILE_CHOOSER_REQUEST_CODE = 1
    }
}