package com.zivansabilli3153.laundryku.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.zivansabilli3153.laundryku.model.Pesanan
import kotlinx.coroutines.flow.Flow

@Dao
interface PesananDao {

    @Insert
    suspend fun insert(pesanan: Pesanan)

    @Update
    suspend fun update(pesanan: Pesanan)

    @Delete
    suspend fun delete(pesanan: Pesanan)

    @Query("SELECT * FROM pesanan ORDER BY waktuDibuat DESC")
    fun getAllPesanan(): Flow<List<Pesanan>>

    @Query("SELECT * FROM pesanan WHERE id = :id")
    suspend fun getPesananById(id: Long): Pesanan?
}