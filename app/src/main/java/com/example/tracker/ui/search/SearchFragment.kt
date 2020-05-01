package com.example.tracker.ui.search

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
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
    private lateinit var  mMapViewModel: MapViewModel
    private var mRecyclerView: RecyclerView? = null

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
        mRecyclerView = v.findViewById(R.id.search_recycler)

        mSearchViewModel = ViewModelProvider(requireActivity() as MainActivity).get(SearchViewModel::class.java)
        mMapViewModel = ViewModelProvider(requireActivity() as MainActivity).get(MapViewModel::class.java)

        mSearchViewModel.searchResult.observe(viewLifecycleOwner, Observer {
            mRecyclerView?.visibility = View.VISIBLE
            mRecyclerView?.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
                mRecyclerView?.adapter = SearchAdapter(it) {
                    Toast.makeText(requireContext(), "CLICK" , Toast.LENGTH_SHORT).show()
                }
                mRecyclerView?.adapter!!.notifyDataSetChanged()
        })
//        mSearchViewModel.mRecentlySeen.observe(viewLifecycleOwner, Observer {
//            it?.let {
//                mRecyclerView?.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
//                mRecyclerView?.adapter = RecentlyRecyclerAdapter(it) {
//                    Toast.makeText(requireContext(), "CLICK" , Toast.LENGTH_SHORT).show()
//                }
//                mRecyclerView?.adapter!!.notifyDataSetChanged()
//            }
//        })


//
//        mSearchViewModel.mFailureMessage.observe(viewLifecycleOwner, Observer {
//            it?.let {
//                error_text.visibility = View.VISIBLE
//            }
//        })

//        mSearchViewModel.mShowProgress.observe(viewLifecycleOwner, Observer {
//            if (it) {
//                 progress.visibility = View.VISIBLE
//                search_container.visibility = View.GONE
//            } else {
//                progress.visibility = View.GONE
//            }
//        })



    }



}


