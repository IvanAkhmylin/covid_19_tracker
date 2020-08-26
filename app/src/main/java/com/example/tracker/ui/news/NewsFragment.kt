package com.example.tracker.ui.news

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tracker.Constants.Constants.RECYCLER_STATE
import com.example.tracker.Constants.Status
import com.example.tracker.R
import com.example.tracker.base.BaseFragment
import com.example.tracker.data.local.entity.News
import com.example.tracker.utils.ExpansionUtils.hideKeyboard
import com.example.tracker.utils.Utils
import com.example.tracker.ui.MainActivity
import com.example.tracker.ui.countries.CountriesViewModel
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.fragment_list_news.*
import kotlinx.android.synthetic.main.loading_and_error_layout.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class NewsFragment() : BaseFragment() {
    private val mViewModel: NewsViewModel by injectViewModel()
    private val mCountryViewModel: CountriesViewModel by injectViewModel()

    private var mRecycler: RecyclerView? = null
    private var mState: Bundle? = null
    private var mSpinner: AutoCompleteTextView? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_list_news, container, false)
        init(v)
        return v
    }

    private fun init(v: View) {
        Toast.makeText(
            requireContext(), "${requireContext().getString(R.string.app_locale)} --- " +
                    "${requireContext().getString(R.string.by_world)}", Toast.LENGTH_SHORT
        ).show()

        mViewModel.refreshNewsData(
            requireContext().getString(R.string.by_world),
            requireContext().getString(R.string.app_locale)
        )

        initViews(v)
        observers()
    }

    private fun initViews(v: View) {
        mRecycler = v.findViewById(R.id.news_recycler)
        mRecycler?.apply {
            setItemViewCacheSize(30)
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = NewsRecyclerAdapter(
                { news ->
                    Utils.showWebPage(requireContext(), news.link)
                }, {
                    Utils.shareNews(it, requireContext())
                })
            mRecycler?.scheduleLayoutAnimation()
            setHasFixedSize(true)
        }

        mSpinner = v.findViewById(R.id.spinner)
        v.findViewById<MaterialButton>(R.id.try_again).setOnClickListener {
            mViewModel.refreshNewsData(
                requireContext().getString(R.string.by_world),
                requireContext().getString(R.string.app_locale)
            )

            mCountryViewModel.getCountries()
        }
    }

    private fun observers() {

        mCountryViewModel.mCountriesNames.observe(viewLifecycleOwner, Observer { countryNames ->
            GlobalScope.launch(Dispatchers.Main) {
                Utils.translateCountryNames(
                    requireContext().getString(R.string.app_locale),
                    countryNames
                ) {
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
                            mViewModel.refreshNewsData(
                                text.toString(),
                                requireContext().getString(R.string.app_locale)
                            )
                            progress?.visibility = View.VISIBLE
                            mSpinner?.hideKeyboard()
                            setOnEditorActionListener { v, actionId, _ ->
                                if (actionId == EditorInfo.IME_ACTION_DONE) {
                                    hideKeyboard()
                                }
                                true
                            }
                        }

                        mRecycler?.apply {
                            visibility = View.GONE
                            (mRecycler?.adapter as NewsRecyclerAdapter).clearAdapterData()
                        }
                    }
                }
            }
        })


        mViewModel.mNewsList.observe(viewLifecycleOwner, Observer { item ->
            if (item.isNotEmpty()) {

                mRecycler?.layoutManager!!.onRestoreInstanceState(mRecycler?.layoutManager?.onSaveInstanceState())
                (mRecycler?.adapter as NewsRecyclerAdapter).updateRecyclerAdapter(
                    item.last().loadStatus,
                    item as ArrayList<News>
                )
                mRecycler?.isNestedScrollingEnabled = false
                mRecycler?.scheduleLayoutAnimation()
            }
        })

        mViewModel.mStatus.observe(viewLifecycleOwner, Observer {
            when (it) {
                Status.LOADING -> {
                    failure_container.visibility = View.GONE
                    progress?.visibility = View.GONE
                    mRecycler?.visibility = View.VISIBLE
                }

                Status.SUCCESS -> {
                    Toast.makeText(
                        requireContext(),
                        requireContext().getString(R.string.all_news_loaded),
                        Toast.LENGTH_SHORT
                    ).show()
                    progress?.visibility = View.GONE
                    failure_container.visibility = View.GONE
                    mRecycler?.visibility = View.VISIBLE
                }

                Status.ERROR -> {
                    mRecycler?.visibility = View.GONE
                    failure_container.visibility = View.VISIBLE
                    progress?.visibility = View.GONE
                }

                Status.PRE_LOADING -> {
                    failure_container.visibility = View.GONE
                    progress?.visibility = View.VISIBLE
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
