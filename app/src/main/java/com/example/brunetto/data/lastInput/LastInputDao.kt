package com.example.brunetto.data.lastInput

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface LastInputDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addLastInput(lastInput: LastInput)

    @Query("SELECT * FROM LastInput ORDER BY id ASC")
    fun readAllData(): LiveData<List<LastInput>>

    @Query("SELECT * FROM LastInput WHERE id = :id")
    fun getLastInputByYearId(id: Int = 2022): LiveData<LastInput>

    @Update
    fun updateLastInput(entity: LastInput)

    @Query("SELECT (SELECT COUNT(id) FROM lastInput) == 0")
    fun isEmpty(): LiveData<Boolean>
}