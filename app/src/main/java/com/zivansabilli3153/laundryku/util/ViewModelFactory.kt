package com.zivansabilli3153.laundryku.util

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zivansabilli3153.laundryku.database.LaundryDb
import com.zivansabilli3153.laundryku.ui.screen.DetailViewModel
import com.zivansabilli3153.laundryku.ui.screen.MainViewModel

class ViewModelFactory(context: Context) : ViewModelProvider.Factory {

    private val dao = LaundryDb.getInstance(context).dao

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(dao) as T
        }

        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(dao) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}