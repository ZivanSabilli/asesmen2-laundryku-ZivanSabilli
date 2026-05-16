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

    suspend fun getPesanan(id: Long): Pesanan? {
        return dao.getPesananById(id)
    }

    fun insert(
        namaPelanggan: String,
        beratKg: Double,
        serviceType: String,
        antarJemput: Boolean,
        catatan: String
    ) {
        val pesanan = createPesanan(
            id = 0L,
            namaPelanggan = namaPelanggan,
            beratKg = beratKg,
            serviceType = serviceType,
            antarJemput = antarJemput,
            catatan = catatan
        )

        viewModelScope.launch(Dispatchers.IO) {
            dao.insert(pesanan)
        }
    }

    fun update(
        id: Long,
        namaPelanggan: String,
        beratKg: Double,
        serviceType: String,
        antarJemput: Boolean,
        catatan: String
    ) {
        val pesanan = createPesanan(
            id = id,
            namaPelanggan = namaPelanggan,
            beratKg = beratKg,
            serviceType = serviceType,
            antarJemput = antarJemput,
            catatan = catatan
        )

        viewModelScope.launch(Dispatchers.IO) {
            dao.update(pesanan)
        }
    }

    fun delete(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteById(id)
        }
    }

    private fun createPesanan(
        id: Long,
        namaPelanggan: String,
        beratKg: Double,
        serviceType: String,
        antarJemput: Boolean,
        catatan: String
    ): Pesanan {
        val pricePerKg = if (serviceType == "express") 10000 else 7000
        val pickupFee = if (antarJemput) 5000 else 0
        val totalHarga = (beratKg * pricePerKg + pickupFee).toInt()
        val estimasiHari = if (serviceType == "express") 1 else 3
        val jenisLayanan = if (serviceType == "express") "Express" else "Regular"

        return Pesanan(
            id = id,
            namaPelanggan = namaPelanggan,
            beratKg = beratKg,
            jenisLayanan = jenisLayanan,
            antarJemput = antarJemput,
            totalHarga = totalHarga,
            estimasiHari = estimasiHari,
            catatan = catatan,
            tanggal = formatter.format(Date())
        )
    }
}