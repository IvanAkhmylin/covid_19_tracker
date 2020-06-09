package com.example.tracker.ui.countries

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tracker.Constants.Constants
import com.example.tracker.Constants.Constants.RECYCLER_STATE
import com.example.tracker.Constants.Status
import com.example.tracker.R
import com.example.tracker.Utils.ExpansionUtils.hideKeyboard
import com.example.tracker.ui.MainActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.loading_and_error_layout.*


class CountriesFragment : Fragment() {
    private lateinit var mViewModel: CountriesViewModel
    private var mSearchView: SearchView? = null
    private lateinit var mChipGroup: ChipGroup
    private lateinit var mMenuItem: MenuItem
    private lateinit var mFab: FloatingActionButton
    private var mRecycler: RecyclerView? = null
    private var mQuery = ""
    private var mState: Bundle? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_search, container, false)
        setHasOptionsMenu(true)
        init(v)
        return v
    }


    @SuppressLint("SetTextI18n")
    private fun init(v: View) {
        mViewModel =
            ViewModelProvider(requireActivity() as MainActivity).get(CountriesViewModel::class.java)
        mChipGroup = v.findViewById(R.id.chip_group)
        mRecycler = v.findViewById(R.id.search_recycler)
        mRecycler?.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        mFab = v.findViewById(R.id.search_fab)

        v.findViewById<MaterialButton>(R.id.try_again).setOnClickListener {
            mViewModel.getCountriesStatistic()
        }

        initChips()
        initFab()
        initObservers()
    }

    private fun initObservers() {
        mViewModel.mCountriesStatus.observe(viewLifecycleOwner, Observer {
            when (it) {
                Status.LOADING -> {
                    mFab.visibility = View.GONE
                    mChipGroup.visibility = View.GONE
                    failure_container.visibility = View.GONE
                    progress?.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    not_find_data.visibility = View.GONE
                    mFab.visibility = View.VISIBLE
                    mChipGroup.visibility = View.VISIBLE
                    progress?.visibility = View.GONE
                    failure_container.visibility = View.GONE
                    mRecycler?.visibility = View.VISIBLE
                }
                Status.ERROR -> {
                    failure_container.visibility = View.VISIBLE
                    mFab.visibility = View.GONE
                    mChipGroup.visibility = View.GONE
                    progress?.visibility = View.GONE
                    mRecycler?.visibility = View.GONE
                }
                Status.NOT_FOUND -> {
                    not_find_data.visibility = View.VISIBLE
                    failure_container.visibility = View.GONE
                    mRecycler?.visibility = View.GONE
                }
            }
        })

        mViewModel.mFilteredData.observe(viewLifecycleOwner, Observer { list ->
            list?.let {
                mRecycler?.adapter = CountriesAdapter(list) {
                    val countyHistoric = mViewModel.getHistoric(it.country)
                    if (countyHistoric != null) {
                        Navigation.findNavController(requireActivity(), R.id.nav_host)
                            .navigate(
                                R.id.action_countries_to_detailCountryFragment,
                                bundleOf(
                                    Constants.COUNTRIES_TITLE to it.country,
                                    Constants.HISTORIC_OF_COUNTRIES to countyHistoric
                                )
                            )
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Cannot get historic date of this region",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                }
                resumeLayoutManager()
            }
        })
    }

    private fun initFab() {
        mFab.setOnClickListener {
            if (mSearchView!!.isIconified) {
               showSearchView()
            } else {
                hideSearchView()
            }
        }
    }

    private fun hideSearchView() {
        mFab.apply {
            setImageDrawable(requireContext().getDrawable(R.drawable.ic_search))
            hideKeyboard()
        }
        mMenuItem.isVisible = false
        mSearchView!!.onActionViewCollapsed()
    }

    private fun showSearchView() {
        mFab.setImageDrawable(requireContext().getDrawable(R.drawable.ic_close))
        mMenuItem.isVisible = true
        mSearchView!!.onActionViewExpanded()
    }

    private fun initChips() {
        Constants.CONTINENTS.forEachIndexed { index, continent ->
            val chip = Chip(mChipGroup.context, null, R.attr.ChipStyle).apply {
                id = index
                text = continent
                chipBackgroundColor = requireContext().getColorStateList(R.color.chip_selector)
            }
            mChipGroup.addView(chip)
        }

        mChipGroup.setOnCheckedChangeListener { group, checkedId ->

            if (checkedId != -1) {
                val chip = group.getChildAt(checkedId) as Chip
                if (chip.isPressed) {
                    mViewModel.getCountriesByContinent(checkedId)
                }
                chip_scroll.post(Runnable {
                    chip_scroll.smoothScrollTo(
                        chip.right - (chip.width * 2), chip.top
                    )
                })
            } else {
                mViewModel.getCountriesByContinent(checkedId)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_menu, menu)
        mMenuItem = menu.findItem(R.id.search_item)
        mMenuItem.apply {
            expandActionView()
            isVisible = false
            mSearchView = mMenuItem.actionView as SearchView
        }

        initSearchView()
    }

    private fun initSearchView() {
        mSearchView?.apply {
            if (mQuery.isNotEmpty()) {
                showSearchView()
                setQuery(mQuery, true)
            }

            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    mQuery = newText?.trim()!!
                    mViewModel.searchByFilteredData(newText.trim())
                    return false
                }

            })

            queryHint = "Country (EN)"
        }
    }


    override fun onPause() {
        super.onPause()
        pauseLayoutManager()
    }

    override fun onDestroy() {
        mViewModel.resetData()
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        resumeLayoutManager()
    }

    private fun pauseLayoutManager() {
        mState = Bundle()
        mSearchView?.hideKeyboard()
        val state = mRecycler?.layoutManager?.onSaveInstanceState()
        mState?.putParcelable(RECYCLER_STATE, state)
    }

    private fun resumeLayoutManager() {
        mState?.let {
            val state: Parcelable? = mState!!.getParcelable(RECYCLER_STATE)
            mRecycler?.layoutManager?.onRestoreInstanceState(state)
            mState = null
        }
    }

}


