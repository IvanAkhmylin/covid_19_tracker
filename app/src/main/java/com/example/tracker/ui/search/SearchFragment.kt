package com.example.tracker.ui.search

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tracker.R
import com.example.tracker.ui.MainActivity
import com.example.tracker.ui.map.MapViewModel
import kotlinx.android.synthetic.main.fragment_search.*


class SearchFragment : Fragment() {
    private lateinit var mSearchViewModel: SearchViewModel
    private lateinit var mMapViewModel: MapViewModel
    private var mSearchRecycler: RecyclerView? = null
    private var mRecentlyRecycler: RecyclerView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_search, container, false)
        init(v)
        return v
    }

    @SuppressLint("SetTextI18n")
    private fun init(v: View) {
        mSearchRecycler = v.findViewById(R.id.search_recycler)
        mRecentlyRecycler = v.findViewById(R.id.recently_recycler)

        mSearchViewModel =
            ViewModelProvider(requireActivity() as MainActivity).get(SearchViewModel::class.java)
        mMapViewModel =
            ViewModelProvider(requireActivity() as MainActivity).get(MapViewModel::class.java)

        mSearchViewModel.searchResult.observe(viewLifecycleOwner, Observer { list ->
            if (list != null) {
                recently_container.visibility = View.GONE
                mSearchRecycler?.visibility = View.VISIBLE
                mSearchRecycler?.layoutManager =
                    LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
                mSearchRecycler?.adapter = SearchAdapter(list) {
                    mSearchViewModel.addRecentlyCountry(it)


                }
                mSearchRecycler?.adapter!!.notifyDataSetChanged()
            } else {
                mSearchRecycler?.visibility = View.GONE
            }
        })

        mSearchViewModel.mRecentlySeen.observe(viewLifecycleOwner, Observer { list ->
            if (list != null) {
                recently_container.visibility = View.VISIBLE
                mRecentlyRecycler?.layoutManager =
                    LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
                mRecentlyRecycler?.adapter = SearchAdapter(list) {
                    mSearchViewModel.addRecentlyCountry(it)


                }
                mRecentlyRecycler?.adapter!!.notifyDataSetChanged()
            } else {
                recently_container.visibility = View.GONE
            }
        })

        mSearchViewModel.notFound.observe(viewLifecycleOwner, Observer {
            when (it) {
                true -> error_container.visibility = View.VISIBLE
                false -> error_container.visibility = View.GONE
            }
        })
    }



    override fun onDestroyView() {
        super.onDestroyView()
        mSearchViewModel.searchResult.postValue(null)
        mSearchViewModel.notFound.postValue(false)
    }
}


