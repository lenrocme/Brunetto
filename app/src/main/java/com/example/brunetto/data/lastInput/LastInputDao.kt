package com.example.brunetto.data.lastInput

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.brunetto.data.lastInput.LastInput

@Dao
interface LastInputDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addLastInput(lastInput: LastInput)

    @Query("SELECT * FROM LastInput ORDER BY id ASC")
    fun readAllData(): LiveData<List<LastInput>>

    @Update
    fun updateLastInput(entity: LastInput)

    @Query("SELECT (SELECT COUNT(id) FROM lastInput) == 0")
    fun isEmpty(): LiveData<Boolean>
}