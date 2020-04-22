package com.example.tracker.ui.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.example.tracker.App.Companion.hideKeyboard
import com.example.tracker.App.Companion.showKeyboard
import com.example.tracker.R


class SearchFragment : Fragment() {
    private var mSearch: EditText? = null
    private var mBack: ImageButton? = null
    private var mClearText: ImageButton? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v =  inflater.inflate(R.layout.fragment_search, container, false)
//        init(v)
        return v
    }

    private fun init(v: View?) {
//        mBack = v?.findViewById(R.id.back)
//        mBack?.setOnClickListener {
//            parentFragmentManager.popBackStack()
//            mSearch?.hideKeyboard()
//        }
//        mSearch = v?.findViewById(R.id.search_country)
//        mSearch?.apply {
//            this.requestFocus()
//            mSearch?.showKeyboard()
//        }
//
//
//        mSearch?.addTextChangedListener(object: TextWatcher {
//            override fun afterTextChanged(s: Editable?) {}
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                if (s?.isNotEmpty()!!){
//                    mClearText?.setImageResource(R.drawable.ic_clear)
//                }else{
//                    mClearText?.setImageResource(0)
//                }
//            }
//
//        })
//        mClearText = v?.findViewById(R.id.button_clear)
//        mClearText?.setOnClickListener {
//            mSearch?.setText("")
//        }
//    }
    }

}
