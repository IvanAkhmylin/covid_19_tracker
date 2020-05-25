package com.example.tracker.ui.news

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.tracker.Constants.Constants.NEWS_LINK
import com.example.tracker.Constants.Constants.NEWS_TITLE
import com.example.tracker.Constants.Status
import com.example.tracker.R
import com.example.tracker.ui.MainActivity
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.loading_and_error_layout.*


class NewsDetailFragment : Fragment() {
    private var mWebView: WebView? = null
    private lateinit var mViewModel: NewsViewModel
    private var mProgress: ProgressBar? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v =  inflater.inflate(R.layout.fragment_news_detail, container, false)
        init(v)
        return v
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun init(v: View) {
        val factory = NewsViewModelFactory(arguments?.getString(NewsListFragment.QUERY, "")!!)
        mViewModel = ViewModelProvider(this, factory).get(NewsViewModel::class.java)

        mWebView = v.findViewById(R.id.news_web_view)
        mProgress = v.findViewById(R.id.progress)
        v.findViewById<MaterialButton>(R.id.try_again).setOnClickListener {
            mViewModel.getUrlWithOutRedirect(arguments?.getString(NEWS_LINK)!!)
        }
        mWebView!!.settings.apply {
            this.javaScriptEnabled = true
            this.domStorageEnabled = true
            this.setAppCacheEnabled(true)
            this.domStorageEnabled = true
            this.databaseEnabled = true
            this.allowFileAccessFromFileURLs = true
            this.allowUniversalAccessFromFileURLs = true
        }

        mWebView?.webViewClient = object : WebViewClient(){
            override fun onPageCommitVisible(view: WebView?, url: String?) {
                super.onPageCommitVisible(view, url)
                progress.visibility = View.GONE
                mWebView?.visibility = View.VISIBLE
            }
        }

        mViewModel.mNewsListStatus.observe(viewLifecycleOwner, Observer {
            when (it) {
                Status.LOADING -> {
                    failure_container.visibility = View.GONE
                    progress.visibility = View.VISIBLE
                }
                Status.SUCCESS ->{
                    mViewModel.mTrueLink.observe(viewLifecycleOwner,  Observer {
                        mWebView?.loadUrl(it)
                    })

                }
                Status.ERROR->{
                    mWebView?.visibility = View.GONE
                    progress.visibility = View.GONE
                    failure_container.visibility = View.VISIBLE
                }

            }

        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        arguments?.let {
            mViewModel.getUrlWithOutRedirect(it.getString(NEWS_LINK)!!)
            (requireActivity() as MainActivity).toolbar.title = it.getString(NEWS_TITLE)
        }
    }
}
