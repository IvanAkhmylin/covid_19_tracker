package com.example.tracker.ui.news

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tracker.Constants.Constants.RECYCLER_STATE
import com.example.tracker.Constants.Status
import com.example.tracker.R
import com.example.tracker.base.BaseFragment
import com.example.tracker.data.local.entity.News
import com.example.tracker.databinding.FragmentNewsBinding
import com.example.tracker.databinding.NewsItemBinding
import com.example.tracker.utils.ExpansionUtils.hideKeyboard
import com.example.tracker.utils.Utils
import com.example.tracker.ui.countries.CountriesViewModel
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.fragment_news.*
import kotlinx.android.synthetic.main.loading_and_error_layout.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class NewsFragment() : BaseFragment() {
    private val mViewModel: NewsViewModel by injectViewModel()
    private val mCountryViewModel: CountriesViewModel by injectViewModel()

    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding

    private var mState: Bundle? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewsBinding.inflate(inflater, container, false)
        val v = binding!!.root
        init(v)
        return v
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun init(v: View) {
        mViewModel.refreshNewsData(
            requireContext().getString(R.string.by_world),
            requireContext().getString(R.string.app_locale)
        )

        initViews(v)
        observers()
    }

    private fun initViews(v: View) {
        binding?.newsRecycler?.apply {
            setItemViewCacheSize(30)
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = NewsRecyclerAdapter(
                { news ->
                    Utils.showWebPage(requireContext(), news.link)
                }, {
                    Utils.shareNews(it, requireContext())
                })
            binding?.newsRecycler?.scheduleLayoutAnimation()
            setHasFixedSize(true)
        }


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
                        binding?.spinner?.setAdapter(this)
                    }

                    spinner_container?.animate()?.translationY(0f)?.setDuration(
                        requireContext().resources.getInteger(R.integer.secondary_duration).toLong()
                    )
                    val margins = (binding?.newsRecycler?.layoutParams as RelativeLayout.LayoutParams).apply {
                        topMargin = 0
                    }

                    binding?.newsRecycler?.layoutParams = margins

                    binding?.spinner?.setOnItemClickListener { _, view, _, _ ->
                        (view as TextView).apply {
                            mViewModel.refreshNewsData(
                                text.toString(),
                                requireContext().getString(R.string.app_locale)
                            )
                            progress?.visibility = View.VISIBLE
                            binding?.spinner?.hideKeyboard()
                            setOnEditorActionListener { _, actionId, _ ->
                                if (actionId == EditorInfo.IME_ACTION_DONE) {
                                    hideKeyboard()
                                }
                                true
                            }
                        }

                        binding?.newsRecycler?.apply {
                            visibility = View.GONE
                            (binding?.newsRecycler?.adapter as NewsRecyclerAdapter).clearAdapterData()
                        }
                    }
                }
            }
        })


        mViewModel.mNewsList.observe(viewLifecycleOwner, Observer { item ->
            if (item.isNotEmpty()) {

                binding?.newsRecycler?.layoutManager!!.onRestoreInstanceState(binding?.newsRecycler?.layoutManager?.onSaveInstanceState())
                (binding?.newsRecycler?.adapter as NewsRecyclerAdapter).updateRecyclerAdapter(
                    item.last().loadStatus,
                    item as ArrayList<News>
                )

                binding?.newsRecycler?.isNestedScrollingEnabled = false
                binding?.newsRecycler?.scheduleLayoutAnimation()
            }
        })

        mViewModel.mStatus.observe(viewLifecycleOwner, Observer {
            when (it) {
                Status.LOADING -> {
                    failure_container.visibility = View.GONE
                    progress?.visibility = View.GONE
                    binding?.newsRecycler?.visibility = View.VISIBLE
                }

                Status.SUCCESS -> {
                    Toast.makeText(
                        requireContext(),
                        requireContext().getString(R.string.all_news_loaded),
                        Toast.LENGTH_SHORT
                    ).show()
                    progress?.visibility = View.GONE
                    failure_container.visibility = View.GONE
                    binding?.newsRecycler?.visibility = View.VISIBLE
                }

                Status.ERROR -> {
                    binding?.newsRecycler?.visibility = View.GONE
                    failure_container.visibility = View.VISIBLE
                    progress?.visibility = View.GONE
                }

                Status.PRE_LOADING -> {
                    failure_container.visibility = View.GONE
                    progress?.visibility = View.VISIBLE
                    binding?.newsRecycler?.visibility = View.GONE
                }

                else -> Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }

        })
    }


    override fun onPause() {
        super.onPause()
        mState = Bundle()
        val state = binding?.newsRecycler?.layoutManager?.onSaveInstanceState()
        mState?.putParcelable(RECYCLER_STATE, state)

        binding?.spinner?.hideKeyboard()
        binding?.spinner?.clearFocus()
    }

    override fun onResume() {
        super.onResume()
        mState?.let {
            val state: Parcelable? = mState!!.getParcelable(RECYCLER_STATE)
            binding?.newsRecycler?.layoutManager?.onRestoreInstanceState(state)
        }

    }


}
