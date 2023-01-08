package com.malferma.brunetto.data.lastInput

import androidx.lifecycle.LiveData

class LastInputRepository(private val lastInputDao: LastInputDao) {

    val readAllData : LiveData<List<LastInput>> = lastInputDao.readAllData()
    val isEmpty : LiveData<Boolean> = lastInputDao.isEmpty()
    val lastInput : LiveData<LastInput> = lastInputDao.getLastInputByYearId()

    suspend fun addLastInput(lastInput: LastInput) {
        lastInputDao.addLastInput(lastInput)
    }

    suspend fun getLastInputByYearId(yearId : Int = 2022): LiveData<LastInput> {
        return lastInputDao.getLastInputByYearId(yearId)
    }

    suspend fun updateLastInput(lastInput: LastInput) {
        lastInputDao.updateLastInput(lastInput)
    }
}