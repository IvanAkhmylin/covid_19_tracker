package com.example.tracker.Utils

import androidx.recyclerview.widget.DiffUtil
import com.example.tracker.model.Country

class CountriesDiffUtilCallback(var oldValues: List<Country>, var newValues: List<Country>) :
        DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return (oldValues[oldItemPosition].countryInfo._id == newValues[newItemPosition].countryInfo._id)
        }

        override fun getOldListSize(): Int {
            return oldValues.size
        }

        override fun getNewListSize(): Int {
            return newValues.size
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldValues[oldItemPosition].equals(newValues[newItemPosition])

        }

    }
