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
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.aksihijau.R
import com.aksihijau.databinding.ActivityMakereportWebviewBinding
import com.aksihijau.datastore.TokenPreferences
import com.aksihijau.datastore.TokenViewModel
import com.aksihijau.datastore.TokenViewModelFactory
import com.aksihijau.ui.fiturcampaign.detail.CampaignDetailActivity

class Makereport_webview_activity : AppCompatActivity() {
    private lateinit var binding : ActivityMakereportWebviewBinding
    private lateinit var tokenViewModel: TokenViewModel
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "token")
    private var filePathCallback: ValueCallback<Array<Uri>>? = null
    private lateinit var slug: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMakereportWebviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setTitle("Membuat Report Baru")
        }
        binding.toolbar.root.setTitleTextColor(resources.getColor(R.color.white))

        val tokenPreferences = TokenPreferences.getInstance(dataStore)
        val tokenViewModelFactory = TokenViewModelFactory(tokenPreferences)
        tokenViewModel = ViewModelProvider(this, tokenViewModelFactory).get(TokenViewModel::class.java)

        binding.wvMakereport.webViewClient = WebViewClient()
        loadWebViewWithToken()
    }

    private fun loadWebViewWithToken() {
        tokenViewModel.getToken().observe(this, { token ->
            tokenViewModel.getRefreshToken().observe(this, { refreshToken ->
                slug = intent.getStringExtra(CampaignDetailActivity.EXTRA_SLUG) ?: ""
                val url = "https://aksihijau-reports.netlify.app/campaigns/$slug/reports?accessToken=$token&refreshToken=$refreshToken"
                binding.wvMakereport.loadUrl(url)
            })
        })

        val webSettings = binding.wvMakereport.settings
        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true
        webSettings.allowFileAccess = true
        webSettings.allowContentAccess = true
        webSettings.allowFileAccessFromFileURLs = true
        webSettings.allowUniversalAccessFromFileURLs = true

        binding.wvMakereport.webChromeClient = object : WebChromeClient() {
            override fun onShowFileChooser(
                webView: WebView?,
                filePathCallback: ValueCallback<Array<Uri>>?,
                fileChooserParams: FileChooserParams?
            ): Boolean {
                if (filePathCallback == null) return false

                this@Makereport_webview_activity.filePathCallback = filePathCallback

                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "*/*"
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                intent.addCategory(Intent.CATEGORY_OPENABLE)

                val chooserIntent = Intent.createChooser(intent, "Choose File")
                startActivityForResult(chooserIntent,
                    Makereport_webview_activity.FILE_CHOOSER_REQUEST_CODE
                )

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
                this@Makereport_webview_activity.filePathCallback?.onReceiveValue(filePaths)
                this@Makereport_webview_activity.filePathCallback = null
            } else {
                this@Makereport_webview_activity.filePathCallback?.onReceiveValue(null)
                this@Makereport_webview_activity.filePathCallback = null
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        if(binding.wvMakereport.canGoBack()){
            binding.wvMakereport.goBack()
        } else {
            super.onBackPressed()
        }
    }

    companion object {
        private const val FILE_CHOOSER_REQUEST_CODE = 1
    }
}