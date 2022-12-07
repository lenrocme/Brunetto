package com.example.brunetto.data.lastInput

import androidx.lifecycle.LiveData
import com.example.brunetto.data.lastInput.LastInput
import com.example.brunetto.data.lastInput.LastInputDao

class LastInputRepository(private val lastInputDao: LastInputDao) {

    val readAllData: LiveData<List<LastInput>> = lastInputDao.readAllData()
    val isEmpty = lastInputDao.isEmpty()

    suspend fun addLastInput(lastInput: LastInput) {
        lastInputDao.addLastInput(lastInput)
    }

    suspend fun updateLastInput(lastInput: LastInput) {
        lastInputDao.updateLastInput(lastInput)
    }
}