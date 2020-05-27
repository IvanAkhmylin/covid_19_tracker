package com.example.tracker.ui.search

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tracker.Constants.Constants
import com.example.tracker.Constants.Status
import com.example.tracker.R
import com.example.tracker.Utils.ExpansionUtils.hideKeyboard
import com.example.tracker.ui.MainActivity
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.loading_and_error_layout.*


class SearchFragment : Fragment() {
    private lateinit var mViewModel: SearchViewModel
    private lateinit var mSearchView: SearchView
    private lateinit var mChipGroup: ChipGroup
    private lateinit var mMenuItem: MenuItem
    private lateinit var mFab: FloatingActionButton
    private var mRecycler: RecyclerView? = null

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
        mViewModel = ViewModelProvider(requireActivity() as MainActivity).get(SearchViewModel::class.java)
        mChipGroup = v.findViewById(R.id.chip_group)
        mRecycler = v.findViewById(R.id.search_recycler)
        mFab = v.findViewById(R.id.search_fab)

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
                mRecycler?.layoutManager =
                    LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
                mRecycler?.adapter = SearchAdapter(list) {

                }
                mRecycler?.adapter!!.notifyDataSetChanged()
            }
        })
    }

    private fun initFab() {
        mFab.setOnClickListener {
            if (mSearchView.isIconified){
                mFab.setImageDrawable(requireContext().getDrawable(R.drawable.ic_close))
                mMenuItem.isVisible = true
                mSearchView.onActionViewExpanded()
            }else{
                mFab.apply {
                    setImageDrawable(requireContext().getDrawable(R.drawable.ic_search))
                    hideKeyboard()
                }
                mMenuItem.isVisible = false
                mSearchView.onActionViewCollapsed()
            }
        }
    }

    private fun initChips() {

        Constants.CONTINENTS.forEachIndexed { index, continent ->
            val chip = Chip(mChipGroup.context, null , R.attr.ChipStyle).apply{
                id = index
                text = continent
                chipBackgroundColor = requireContext().getColorStateList(R.color.chip_selector)
            }
            mChipGroup.addView(chip)
        }

        mChipGroup.setOnCheckedChangeListener { group, checkedId ->
            mViewModel.getCountriesByContinent(checkedId)
            mViewModel
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_menu, menu)

        mMenuItem = menu.findItem(R.id.search_item)
        mMenuItem.let {
            it.expandActionView()
            it.isVisible = false
            mSearchView = mMenuItem.actionView as SearchView
        }

        mSearchView.apply {
            this.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {

                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    mViewModel.searchByFilteredData(newText)
                    Toast.makeText(requireContext(), newText , Toast.LENGTH_SHORT).show()
                    return true
                }

            })

            this.queryHint = "Country (EN)"
        }

    }





}


