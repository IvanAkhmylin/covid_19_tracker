package com.example.tracker.ui.countries

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Parcelable
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tracker.Constants.Constants
import com.example.tracker.Constants.Constants.RECYCLER_STATE
import com.example.tracker.Constants.Status
import com.example.tracker.R
import com.example.tracker.base.BaseFragment
import com.example.tracker.data.local.entity.Country
import com.example.tracker.databinding.FragmentCountriesBinding
import com.example.tracker.utils.ExpansionUtils.hideKeyboard
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_countries.*
import kotlinx.android.synthetic.main.loading_and_error_layout.*


class CountriesFragment : BaseFragment() {
    private val mViewModel: CountriesViewModel by injectViewModel()
    private var _binding: FragmentCountriesBinding? = null
    private val binding get() = _binding

    private var mSearchView: SearchView? = null
    private lateinit var mSearchItem: MenuItem
    private lateinit var mSortItem: MenuItem

    private var mRecyclerAdapter: CountriesAdapter? = null
    private var mQuery = ""
    private var mState: Bundle? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCountriesBinding.inflate(inflater, container, false)
        val v = _binding!!.root
        setHasOptionsMenu(true)
        init(v)
        return v
    }

    override fun onDestroy() {
        super.onDestroy()
        mViewModel.resetData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("SetTextI18n")
    private fun init(v: View) {

        binding?.searchRecycler?.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        mRecyclerAdapter = CountriesAdapter {
            val countyHistoric = mViewModel.getHistoric(it.country)
            if (countyHistoric != null) {
                Navigation.findNavController(requireActivity(), R.id.nav_host)
                    .navigate(
                        R.id.action_countries_to_detailCountryFragment,
                        bundleOf(
                            Constants.COUNTRIES_NAME to it.country,
                            Constants.HISTORIC_OF_COUNTRIES to countyHistoric
                        )
                    )
            } else {
                Toast.makeText(
                    requireContext(),
                    requireContext().getString(R.string.error_getting_historic_data),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        mRecyclerAdapter?.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.ALLOW
        binding?.searchRecycler?.adapter = mRecyclerAdapter

        v.findViewById<MaterialButton>(R.id.try_again).setOnClickListener {
            mViewModel.getCountries()
        }

        initChips()
        initFab()
        initObservers()
    }

    private fun initObservers() {
        mViewModel.mStatus.observe(viewLifecycleOwner, Observer {
            when (it) {
                Status.LOADING -> {
                    binding?.searchRecycler?.visibility = View.GONE
                    binding?.searchFab?.visibility = View.GONE
                    binding?.chipGroup?.visibility = View.GONE
                    failure_container.visibility = View.GONE
                    progress?.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    not_find_data.visibility = View.GONE
                    binding?.searchFab?.visibility = View.VISIBLE
                    binding?.chipGroup?.visibility = View.VISIBLE
                    progress?.visibility = View.GONE
                    failure_container.visibility = View.GONE
                    binding?.searchRecycler?.visibility = View.VISIBLE
                }
                Status.ERROR -> {
                    failure_container.visibility = View.VISIBLE
                    binding?.searchFab?.visibility = View.GONE
                    binding?.chipGroup?.visibility = View.GONE
                    progress?.visibility = View.GONE
                    binding?.searchRecycler?.visibility = View.GONE
                }
            }
        })

        mViewModel.mCountries.observe(viewLifecycleOwner, Observer {
            populateRecyclerView(it)
        })

    }

    private fun populateRecyclerView(list: List<Country>) {
        if (list.isNotEmpty()) {
            mRecyclerAdapter?.updateRecyclerView(list)
            not_find_data.visibility = View.GONE
            restoreRecyclerViewPosition()
        } else {
            mRecyclerAdapter?.updateRecyclerView(list)
            not_find_data.visibility = View.VISIBLE
        }
    }



    private fun initFab() {
        binding?.searchFab?.setOnClickListener {
            if (mSearchView!!.isIconified) {
                showSearchView()
            } else {
                hideSearchView()
            }
        }
    }

    private fun hideSearchView() {
        binding?.searchFab?.apply {
            setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_search))
            hideKeyboard()
        }

        mSearchItem.isVisible = false
        mSearchView!!.onActionViewCollapsed()
        mSortItem.isVisible = true
    }

    private fun showSearchView() {
        binding?.searchFab?.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.ic_close
            )
        )
        mSearchItem.isVisible = true
        mSearchView!!.onActionViewExpanded()

        mSortItem.isVisible = false
    }

    private fun initChips() {
        Constants.CONTINENTS.forEachIndexed { index, continent ->
            val chip = Chip(binding?.chipGroup?.context, null, R.attr.ChipStyle).apply {
                id = index
                text = continent
                ViewCompat.setElevation(this, 1f)
                setTextColor(
                    ContextCompat.getColorStateList(
                        requireContext(),
                        R.color.chip_selector_text_color
                    )
                )
                chipBackgroundColor =
                    ContextCompat.getColorStateList(
                        requireContext(),
                        R.color.chip_selector_background
                    )
            }
            binding?.chipGroup?.addView(chip)
        }

        binding?.chipGroup?.setOnCheckedChangeListener { group, checkedId ->
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

        mSearchItem = menu.findItem(R.id.search_item)
        mSortItem = menu.findItem(R.id.sort_item)

        mSearchItem.apply {
            expandActionView()
            isVisible = false
            mSearchView = mSearchItem.actionView as SearchView
        }

        initSearchView()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.sort_cases -> {
                mViewModel.sortBy(Constants.CASES)
            }

            R.id.sort_recovered -> {
                mViewModel.sortBy(Constants.RECOVERED)
            }

            R.id.sort_deaths -> {
                mViewModel.sortBy(Constants.DEATHS)
            }
        }

        return super.onOptionsItemSelected(item)
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
            queryHint = requireContext().getString(R.string.countries_search_hint)

        }
    }


    override fun onPause() {
        super.onPause()
        saveRecyclerViewPosition()
    }

    private fun saveRecyclerViewPosition(){
        mState = Bundle()
        val state = binding?.searchRecycler?.layoutManager?.onSaveInstanceState()
        mState?.putParcelable(RECYCLER_STATE, state)

        mSearchView?.hideKeyboard()
        mSearchView?.clearFocus()
    }

    private fun restoreRecyclerViewPosition() {
        if (mState != null){
            val state: Parcelable? = mState!!.getParcelable(RECYCLER_STATE)
            binding?.searchRecycler?.layoutManager?.onRestoreInstanceState(state)
            mState = null
        }else{
            binding?.searchRecycler?.scrollToPosition(0)
        }
    }

}


