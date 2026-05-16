package com.zivansabilli3153.laundryku.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pesanan")
data class Pesanan(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val namaPelanggan: String,
    val beratKg: Double,
    val jenisLayanan: String,
    val antarJemput: Boolean,
    val totalHarga: Int,
    val estimasiHari: Int,
    val tanggal: String,
    val waktuDibuat: Long = System.currentTimeMillis()
)