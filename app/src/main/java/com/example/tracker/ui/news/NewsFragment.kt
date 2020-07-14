package com.example.tracker.ui.news

import android.app.PendingIntent
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tracker.Constants.Constants.RECYCLER_STATE
import com.example.tracker.Constants.Status
import com.example.tracker.R
import com.example.tracker.Utils.ExpansionUtils.hideKeyboard
import com.example.tracker.Utils.ExpansionUtils.isDarkThemeOn
import com.example.tracker.Utils.Utils
import com.example.tracker.ui.MainActivity
import com.example.tracker.ui.countries.CountriesViewModel
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.fragment_list_news.*
import kotlinx.android.synthetic.main.loading_and_error_layout.*


class NewsFragment() : Fragment() {
    private var mRecycler: RecyclerView? = null
    private var mState: Bundle? = null
    private var mSpinner: AutoCompleteTextView? = null
    private lateinit var mViewModel: NewsViewModel
    private lateinit var mCountryViewModel: CountriesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_list_news, container, false)
        init(v)
        return v
    }

    private fun init(v: View) {
        mViewModel = ViewModelProvider(
            (requireActivity() as MainActivity),
            NewsViewModelFactory(
                requireContext().getString(R.string.app_locale),
                requireContext().getString(R.string.by_world)
            )
        )
            .get(NewsViewModel::class.java)

        mCountryViewModel = ViewModelProvider((requireActivity() as MainActivity))
            .get(CountriesViewModel::class.java)
        mCountryViewModel.mAppLang = requireContext().getString(R.string.app_locale)

        mRecycler = v.findViewById(R.id.news_recycler)
        mRecycler?.apply {
            setItemViewCacheSize(30)
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = NewsRecyclerAdapter(
                { news ->
                    openLink(news.link)
                }, {
                    Utils.shareNews(it, requireContext())
                })
            mRecycler?.scheduleLayoutAnimation()
            setHasFixedSize(true)
        }
        mSpinner = v.findViewById(R.id.spinner)
        v.findViewById<MaterialButton>(R.id.try_again).setOnClickListener {
            mViewModel.getNewsData(
                requireContext().getString(R.string.by_world),
                requireContext().getString(R.string.app_locale)
            )

            mCountryViewModel.getCountriesStatistic()
        }
        observers()
    }

    private fun openLink(link: String) {
        val builder = CustomTabsIntent.Builder()
        if (requireContext().isDarkThemeOn()) {
            builder.setColorScheme(CustomTabsIntent.COLOR_SCHEME_DARK)
        } else {
            builder.setColorScheme(CustomTabsIntent.COLOR_SCHEME_LIGHT)
        }
        builder.addDefaultShareMenuItem()
        builder.setToolbarColor(requireContext().getColor(R.color.colorPrimary))
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(requireContext(), Uri.parse(link))
    }

    private fun observers() {
        mCountryViewModel.mCountriesNameList.observe(viewLifecycleOwner, Observer {
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                it
            ).apply {
                mSpinner?.setAdapter(this)
            }

            spinner_container?.animate()?.translationY(0f)?.setDuration(
                requireContext().resources.getInteger(R.integer.secondary_duration).toLong()
            )
            val margins = (mRecycler?.layoutParams as RelativeLayout.LayoutParams).apply {
                topMargin = 0
            }

            mRecycler?.layoutParams = margins

            mSpinner?.setOnItemClickListener { parent, view, position, id ->
                (view as TextView).apply {
                    mViewModel.getNewsData(
                        text.toString(),
                        requireContext().getString(R.string.app_locale)
                    )

                    mSpinner?.hideKeyboard()
                    setOnEditorActionListener { v, actionId, event ->
                        if (actionId == EditorInfo.IME_ACTION_DONE) {
                            hideKeyboard()
                        }
                        true
                    }
                }
                progress?.visibility = View.VISIBLE

                mRecycler?.apply {
                    visibility = View.GONE
                    (mRecycler?.adapter as NewsRecyclerAdapter).clearAdapterData()
                }
            }
        })


        mViewModel.mNewsData.observe(viewLifecycleOwner, Observer { item ->
            item?.let {
                mRecycler?.layoutManager!!.onRestoreInstanceState(mRecycler?.layoutManager?.onSaveInstanceState())
                (mRecycler?.adapter as NewsRecyclerAdapter).updateRecyclerAdapter(
                    it.second,
                    it.first
                )
                mRecycler?.scheduleLayoutAnimation()
            }
        })

        mViewModel.mNewsListStatus.observe(viewLifecycleOwner, Observer {
            when (it) {
                Status.LOADING -> {
                    progress?.visibility = View.GONE
                    failure_container.visibility = View.GONE
                    mRecycler?.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    Toast.makeText(
                        requireContext(),
                        requireContext().getString(R.string.all_news_loaded),
                        Toast.LENGTH_SHORT
                    ).show()
                    failure_container.visibility = View.GONE
                    progress?.visibility = View.GONE
                    mRecycler?.visibility = View.VISIBLE
                }
                Status.ERROR -> {
                    mRecycler?.visibility = View.GONE
                    failure_container.visibility = View.VISIBLE
                    progress?.visibility = View.GONE
                }
                else -> Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()

            }

        })
    }


    override fun onPause() {
        super.onPause()
        mState = Bundle()
        val state = mRecycler?.layoutManager?.onSaveInstanceState()
        mState?.putParcelable(RECYCLER_STATE, state)

        mSpinner?.hideKeyboard()
        mSpinner?.clearFocus()
    }

    override fun onResume() {
        super.onResume()
        mState?.let {
            val state: Parcelable? = mState!!.getParcelable(RECYCLER_STATE)
            mRecycler?.layoutManager?.onRestoreInstanceState(state)
        }

    }


}
