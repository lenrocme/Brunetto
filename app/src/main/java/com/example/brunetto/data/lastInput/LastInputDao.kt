package com.example.brunetto.data.lastInput

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.brunetto.data.lastInput.LastInput

@Dao
interface LastInputDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addPLastInput(lastInput: LastInput)

    @Query("SELECT * FROM LastInput ORDER BY id ASC")
    fun readAllData(): LiveData<List<LastInput>>

    @Update
    fun updateLastInput(entity: LastInput)

}