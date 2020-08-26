package com.example.tracker.utils

import androidx.recyclerview.widget.DiffUtil
import com.example.tracker.data.local.entity.Country

class CountriesDiffUtilCallback(var oldValues: List<Country>, var newValues: List<Country>) :
        DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return (oldValues[oldItemPosition].countryInfo.id == newValues[newItemPosition].countryInfo.id)
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
