package com.malferma.brunetto.data.lastInput

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.malferma.brunetto.data.database.LocalBrunettoDb
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LastInputViewModel(application: Application): AndroidViewModel(application) {

    val readAllData: LiveData<List<LastInput>>
    private val repository: LastInputRepository
    val isEmpty: LiveData<Boolean>
    val lastInput: LiveData<LastInput>

    init {
        val lastInputDao = LocalBrunettoDb.getDatabase(application).lastInputDao()
        repository = LastInputRepository(lastInputDao)
        readAllData = repository.readAllData
        isEmpty = repository.isEmpty
        lastInput = repository.lastInput
    }

    fun add(lastInput: LastInput){
        viewModelScope.launch(Dispatchers.IO){
            repository.addLastInput(lastInput)
        }
    }

    suspend fun getLastInputByYearId(yearId: Int): LiveData<LastInput> {
        return repository.getLastInputByYearId(yearId)
    }

    fun update(lastInput: LastInput){
        viewModelScope.launch(Dispatchers.IO){
            repository.updateLastInput(lastInput)
        }
    }
}