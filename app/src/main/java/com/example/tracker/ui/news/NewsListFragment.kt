package com.example.tracker.ui.news

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tracker.Constants.Constants
import com.example.tracker.Constants.Status
import com.example.tracker.R
import com.example.tracker.Utils.Utils
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.loading_and_error_layout.*


class NewsListFragment() : Fragment() {
    private var mRecycler: RecyclerView? = null
    private var mState: Bundle? = null
    private lateinit var mViewModel: NewsViewModel
    private var mProgressBar: ProgressBar? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_list_news, container, false)
        init(v)
        return v
    }

    private fun init(v: View) {
        val factory = NewsViewModelFactory(arguments?.getString(QUERY, "")!!)
        mViewModel = ViewModelProvider(this, factory).get(NewsViewModel::class.java)

        mRecycler = v.findViewById(R.id.news_recycler)
        mRecycler?.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

        mProgressBar = v.findViewById(R.id.progress)
        v.findViewById<MaterialButton>(R.id.try_again).setOnClickListener {
            mViewModel.getNewsData(arguments?.getString(QUERY, "")!!)
        }

        initFragment()
    }

    private fun initFragment() {
        mViewModel.mNewsData.observe(viewLifecycleOwner, Observer { newsList ->
            mRecycler?.adapter = NewsRecyclerAdapter(newsList,
            { news ->
                Navigation.findNavController(requireActivity(), R.id.nav_host)
                    .navigate(
                        R.id.action_news_to_newsDetailFragment,
                        bundleOf(
                            Constants.NEWS_LINK to news.link,
                            Constants.NEWS_TITLE to news.resource
                        )
                    )
            },{
                Utils.shareNews(it , requireContext())
            })
        })

        mViewModel.mNewsListStatus.observe(viewLifecycleOwner, Observer {
            when (it) {
                Status.LOADING -> {
                    failure_container.visibility = View.GONE
                    mProgressBar?.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    mProgressBar?.visibility = View.GONE
                    failure_container.visibility = View.GONE
                    mRecycler?.visibility = View.VISIBLE
                }
                Status.ERROR -> {
                    mProgressBar?.visibility = View.GONE
                    mRecycler?.visibility = View.GONE
                    failure_container.visibility = View.VISIBLE
                }

            }

        })
    }



    override fun onPause() {
        super.onPause()
        mState = Bundle()
        val state = mRecycler?.layoutManager?.onSaveInstanceState()
        mState?.putParcelable(RECYCLER_STATE, state)
    }

    override fun onResume() {
        super.onResume()
        mState?.let {
            val state: Parcelable? = mState!!.getParcelable(RECYCLER_STATE)
            mRecycler?.layoutManager?.onRestoreInstanceState(state)
        }

    }


    companion object {
        const val QUERY = "QUERY"
        const val RECYCLER_STATE = "RECYCLER STATE"

        @JvmStatic
        fun newInstance(query: String): Fragment {
            val fragment = NewsListFragment().apply {
                this.arguments = Bundle().apply {
                    putString(QUERY, query)
                }
            }
            return fragment
        }

    }

}
