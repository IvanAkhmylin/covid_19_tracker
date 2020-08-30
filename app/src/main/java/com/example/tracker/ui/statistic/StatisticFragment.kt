package com.example.tracker.ui.statistic

import android.animation.Animator
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tracker.Constants.Status
import com.example.tracker.R
import com.example.tracker.base.BaseFragment
import com.example.tracker.utils.ExpansionUtils.decimalFormatter
import com.example.tracker.utils.ExpansionUtils.setColorBefore
import com.example.tracker.utils.ExpansionUtils.timestampToDate
import com.example.tracker.utils.Utils
import com.example.tracker.data.local.entity.Statistic
import com.example.tracker.data.local.entity.TimeLine
import com.example.tracker.databinding.StatisticLayoutBinding
import com.example.tracker.ui.countries.CountriesDaysAdapter
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_HIDDEN
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.loading_and_error_layout.*
import kotlinx.android.synthetic.main.statistic_layout.*


class StatisticFragment : BaseFragment() {
    private val mViewModel: StatisticViewModel by injectViewModel()

    private var _binding: StatisticLayoutBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = StatisticLayoutBinding.inflate(inflater, container, false)
        val v = binding!!.root
        init(v)
        return v
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun init(v: View) {
        setHasOptionsMenu(true)
        initBottomSheet(v)

        v.findViewById<MaterialButton>(R.id.try_again).setOnClickListener {
            mViewModel.getOverallStatistic()
            mViewModel.getOverallHistoric()
        }

        initChangeChartButton(v)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
    }

    private fun initChangeChartButton(v: View) {
        v.findViewById<MaterialButton>(R.id.change_chart).setOnClickListener {

            if (binding?.lineChart!!.isVisible) {
                binding?.lineChart!!.animate().apply {
                    alpha(0f)
                    translationY(-binding?.lineChart!!.height.toFloat())
                    duration = resources.getInteger(R.integer.primary_duration).toLong()
                    setListener(object : Animator.AnimatorListener {
                        override fun onAnimationRepeat(animation: Animator?) {}

                        override fun onAnimationEnd(animation: Animator?) {
                            binding?.lineChart!!.visibility = View.GONE
                        }

                        override fun onAnimationCancel(animation: Animator?) {}

                        override fun onAnimationStart(animation: Animator?) {}
                    })
                }

                binding?.pieChart!!.animate().apply {
                    alpha(1f)
                    duration = resources.getInteger(R.integer.primary_duration).toLong()
                    translationY(0f)
                    setListener(object : Animator.AnimatorListener {
                        override fun onAnimationRepeat(animation: Animator?) {}

                        override fun onAnimationEnd(animation: Animator?) {
                        }

                        override fun onAnimationCancel(animation: Animator?) {}

                        override fun onAnimationStart(animation: Animator?) {
                            binding?.pieChart!!.visibility = View.VISIBLE
                        }
                    })
                }


                (it as MaterialButton).icon =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_line_chart)
            } else {
                binding?.pieChart!!.animate().apply {
                    alpha(0f)
                    translationY(binding?.pieChart!!.height.toFloat())
                    duration = resources.getInteger(R.integer.primary_duration).toLong()
                    setListener(object : Animator.AnimatorListener {
                        override fun onAnimationRepeat(animation: Animator?) {}

                        override fun onAnimationEnd(animation: Animator?) {}

                        override fun onAnimationCancel(animation: Animator?) {}

                        override fun onAnimationStart(animation: Animator?) {
                            binding?.lineChart!!.visibility = View.VISIBLE
                        }
                    })
                }
                binding?.lineChart!!.animate().apply {
                    alpha(1f)
                    translationY(0f)
                    duration = resources.getInteger(R.integer.primary_duration).toLong()
                    setListener(object : Animator.AnimatorListener {
                        override fun onAnimationRepeat(animation: Animator?) {}

                        override fun onAnimationEnd(animation: Animator?) {
                            binding?.pieChart!!.visibility = View.GONE
                        }

                        override fun onAnimationCancel(animation: Animator?) {}

                        override fun onAnimationStart(animation: Animator?) {

                        }
                    })
                }

                (it as MaterialButton).icon =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_pie_chart)
            }
        }

    }




    private fun initObservers() {
        mViewModel.mStatus.observe(viewLifecycleOwner, Observer {
            when (it) {
                Status.LOADING -> {
                    fab_show_hide.visibility = View.GONE
                    overall_container.visibility = View.GONE
                    failure_container.visibility = View.GONE
                    progress.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    fab_show_hide.visibility = View.VISIBLE
                    failure_container.visibility = View.GONE
                    progress.visibility = View.GONE
                    overall_container.visibility = View.VISIBLE
                }
                Status.ERROR -> {
                    progress.visibility = View.GONE
                    failure_container.visibility = View.VISIBLE
                    overall_container.visibility = View.GONE
                }

            }
        })

        mViewModel.mHistoric.observe(viewLifecycleOwner, Observer {
            it?.let { timeLine ->
                Utils.initializeLineChart(binding?.lineChart!!, binding?.nestedScroll, requireContext(), timeLine)
                initRecyclerView(timeLine)
            }

        })

        mViewModel.mStatistic.observe(viewLifecycleOwner, Observer { statistic ->
            statistic?.let {
                fab_show_hide.visibility = View.VISIBLE
                failure_container.visibility = View.GONE
                progress.visibility = View.GONE
                overall_container.visibility = View.VISIBLE

                Utils.initializePieChart(
                    binding?.pieChart!!,
                    statistic.cases!!.toFloat(),
                    statistic.deaths!!.toFloat(),
                    statistic.recovered!!.toFloat(),
                    requireContext()
                )

                initStatistic(statistic)
            }
        })
    }


    private fun initBottomSheet(v: View) {
        val behavior = BottomSheetBehavior.from(binding?.behaviorCard!!)

        behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {}

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    STATE_EXPANDED -> {
                        fab_show_hide.setImageDrawable(
                            ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.ic_close
                            )
                        )
                    }
                    STATE_HIDDEN -> {
                        fab_show_hide.setImageDrawable(
                            ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.ic_list
                            )
                        )
                    }
                    else -> {
                        fab_show_hide.setImageDrawable(
                            ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.ic_list
                            )
                        )
                    }
                }
            }

        })

        v.findViewById<FloatingActionButton>(R.id.fab_show_hide).setOnClickListener {
            if (behavior.state == STATE_EXPANDED) {
                behavior.state = STATE_HIDDEN
            } else {
                behavior.state = STATE_EXPANDED
            }
        }
    }

    private fun initRecyclerView(it: TimeLine) {
        binding?.statisticDays?.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding?.statisticDays?.adapter = CountriesDaysAdapter(it)
        binding?.statisticDays?.adapter!!.notifyDataSetChanged()
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    private fun initStatistic(it: Statistic) {
        binding?.updated?.text = it.updated.timestampToDate(requireContext())

        binding?.cases?.text = it.cases?.decimalFormatter()
        binding?.deaths?.text = it.deaths?.decimalFormatter()
        binding?.recovered?.text = it.recovered?.decimalFormatter()

        binding?.casesToday?.text = "+${it.todayCases?.decimalFormatter()} ${requireContext().getString(R.string.today)}"
        binding?.casesToday?.setColorBefore(requireContext().getString(R.string.today))

        binding?.deathsToday?.text = "+${it.todayDeaths?.decimalFormatter()} ${requireContext().getString(R.string.today)}"
        binding?.deathsToday?.setColorBefore(requireContext().getString(R.string.today))

        binding?.recoveredToday?.text = "+${it.todayRecovered?.decimalFormatter()} ${requireContext().getString(R.string.today)}"
        binding?.recoveredToday?.setColorBefore(requireContext().getString(R.string.today))

        binding?.active?.text = it.active?.decimalFormatter()
        binding?.tests?.text = it.tests?.decimalFormatter()
        binding?.critical?.text = it.critical?.decimalFormatter()
        binding?.casesPerMillion?.text = it.casesPerOneMillion?.toString()
        binding?.deathsPerMillion?.text = it.deathsPerOneMillion?.toString()
        binding?.testsPerMillion?.text = it.testsPerOneMillion?.toString()
        binding?.affectedCountry?.text = it.affectedCountries?.decimalFormatter()
    }

}
