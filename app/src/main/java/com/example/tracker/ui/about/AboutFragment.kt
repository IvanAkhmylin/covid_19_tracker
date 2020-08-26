package com.example.tracker.ui.about

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.example.tracker.R
import com.example.tracker.utils.ExpansionUtils.isDarkThemeOn


class AboutFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_about, container, false)
        init(v)
        return v
    }

    private fun init(v: View) {
        v.findViewById<ImageButton>(R.id.github_link).apply {
            if (requireContext().isDarkThemeOn()) {
                this.background = requireContext().getDrawable(R.drawable.github_logo_light)
            } else {
                this.background = requireContext().getDrawable(R.drawable.github_logo_dark)
            }

            setOnClickListener {
                val github = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("${requireContext().getText(R.string.DEVELOPER_GITHUB)}")
                )
                startActivity(github)
            }
        }

        v.findViewById<ImageButton>(R.id.gmail_link).apply {
            setOnClickListener {
                val gmail = Intent(
                    Intent.ACTION_SENDTO,
                    Uri.parse("mailto:${requireContext().getText(R.string.DEVELOPER_GMAIL)}")
                )
                startActivity(gmail)
            }
        }

        v.findViewById<ImageButton>(R.id.telegram_link).apply {
            setOnClickListener {
                val telegram = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("${requireContext().getText(R.string.DEVELOPER_TELEGRAM)}")
                )
                startActivity(telegram)
            }
        }
        v.findViewById<ImageButton>(R.id.icon_link).apply {
            setOnClickListener {
                val iconLink = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://pngtree.com/so/virus")
                )
                startActivity(iconLink)
            }
        }

    }

}