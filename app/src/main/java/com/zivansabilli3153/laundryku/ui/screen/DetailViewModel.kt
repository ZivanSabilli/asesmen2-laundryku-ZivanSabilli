package com.zivansabilli3153.laundryku.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zivansabilli3153.laundryku.database.PesananDao
import com.zivansabilli3153.laundryku.model.Pesanan
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DetailViewModel(private val dao: PesananDao) : ViewModel() {

    private val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

    fun insert(
        namaPelanggan: String,
        beratKg: Double,
        serviceType: String,
        antarJemput: Boolean,
        catatan: String
    ) {
        val pricePerKg = if (serviceType == "express") 10000 else 7000
        val pickupFee = if (antarJemput) 5000 else 0
        val totalHarga = (beratKg * pricePerKg + pickupFee).toInt()
        val estimasiHari = if (serviceType == "express") 1 else 3
        val jenisLayanan = if (serviceType == "express") "Express" else "Regular"

        val pesanan = Pesanan(
            namaPelanggan = namaPelanggan,
            beratKg = beratKg,
            jenisLayanan = jenisLayanan,
            antarJemput = antarJemput,
            totalHarga = totalHarga,
            estimasiHari = estimasiHari,
            catatan = catatan,
            tanggal = formatter.format(Date())
        )

        viewModelScope.launch(Dispatchers.IO) {
            dao.insert(pesanan)
        }
    }
}