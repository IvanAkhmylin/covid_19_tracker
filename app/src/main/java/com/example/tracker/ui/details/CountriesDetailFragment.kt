package com.example.tracker.ui.details

import android.animation.Animator
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.tracker.Constants
import com.example.tracker.R
import com.example.tracker.model.Country
import com.facebook.drawee.view.SimpleDraweeView
import java.text.SimpleDateFormat
import java.util.*


class CountriesDetailFragment(dataModel: Country?) : Fragment() {
    private var data = dataModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.detail_layout, container, false)
        init(v)
        return v
    }

    @SuppressLint("SetTextI18n", "ResourceAsColor")
    private fun init(v: View) {
        val currentFragment = requireActivity().supportFragmentManager
            .getBackStackEntryAt(requireActivity().supportFragmentManager.backStackEntryCount - 1)
            .name

        if (currentFragment == Constants.fragmentDetailSearch){
        }

        data.apply {
            val dateFormat = SimpleDateFormat("dd MMMM yyyy hh:mm:ss a")
            val netDate = Date(this!!.updated)

            v.findViewById<SimpleDraweeView>(R.id.country_flag).setImageURI(data?.countryInfo?.flag)
            v.findViewById<TextView>(R.id.last_update).text =
                "Updated: ${dateFormat.format(netDate)}"
            v.findViewById<TextView>(R.id.country_iso).text =
                "Country Code: ${data?.countryInfo?.iso2}, ${data?.countryInfo?.iso3}"
            v.findViewById<TextView>(R.id.continent).text = "Continent: ${data?.continent}"
            v.findViewById<TextView>(R.id.country_name).text = "${data?.country}"

            val view = v.findViewById<LinearLayout>(R.id.additional_data)
            var flag = false
//            mShowMore.setOnClickListener {
//                if (flag){
//                    view.animate().translationY((view.height.toFloat()).unaryMinus()).alpha(0f)
//                        .setListener(object : Animator.AnimatorListener{
//                            override fun onAnimationRepeat(animation: Animator?) {}
//                            override fun onAnimationEnd(animation: Animator?) {
//                                view.visibility = View.GONE
//                                flag = false
//                            }
//                            override fun onAnimationCancel(animation: Animator?) {}
//                            override fun onAnimationStart(animation: Animator?) {
//                                mShowMore.animate().translationY(0f).rotationBy(-180f).start()
//                            }
//                        }).start()
//
//                }else{
//                    view.animate().translationY(-96f).alpha(1f).setListener(object : Animator.AnimatorListener{
//                        override fun onAnimationRepeat(animation: Animator?) {}
//                        override fun onAnimationEnd(animation: Animator?) {
//                            flag = true
//                        }
//                        override fun onAnimationCancel(animation: Animator?) {}
//                        override fun onAnimationStart(animation: Animator?) {
//                            view.visibility = View.VISIBLE
//                            Handler().postDelayed(Runnable {
//                                mShowMore.animate().translationY(view.height.toFloat()).rotationBy(180f).start()
//                            }, 5)
//                        }
//                    }).start()
//
//                }
//            }



//            v.findViewById<TextView>(R.id.cases).text = data?.cases.toString()
//            v.findViewById<TextView>(R.id.active).text = data?.active.toString()
//            v.findViewById<TextView>(R.id.critical).text = data?.critical.toString()
//            v.findViewById<TextView>(R.id.today_cases).text = data?.todayCases.toString()
//            v.findViewById<TextView>(R.id.deaths).text = data?.deaths.toString()
//            v.findViewById<TextView>(R.id.today_deaths).text = data?.todayDeaths.toString()
//            v.findViewById<TextView>(R.id.recovered).text = data?.recovered.toString()
//            v.findViewById<TextView>(R.id.tests).text = data?.tests.toString()
//            v.findViewById<TextView>(R.id.cases_per_million).text =
//                data?.casesPerOneMillion.toString()
//            v.findViewById<TextView>(R.id.deaths_per_million).text =
//                data?.deathsPerOneMillion.toString()
//            v.findViewById<TextView>(R.id.tests_per_million).text =
//                data?.testsPerOneMillion.toString()

        }
    }

}