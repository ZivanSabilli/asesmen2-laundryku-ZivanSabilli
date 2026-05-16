package com.zivansabilli3153.laundryku.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zivansabilli3153.laundryku.database.PesananDao
import com.zivansabilli3153.laundryku.model.Pesanan
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class MainViewModel(dao: PesananDao) : ViewModel() {

    val dataPesanan: StateFlow<List<Pesanan>> = dao.getAllPesanan().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
}