package com.example.brunetto.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class CalcTaxViewModel : ViewModel() {
    val rentenVers : Double by mutableStateOf(0.0)
    val arbeitslosenVers : Double by mutableStateOf(0.0)
    val krankVers : Double by mutableStateOf(0.0)
    val kvZusatzBetr : Double by mutableStateOf(0.0)
    val plfegVers : Double by mutableStateOf(0.0)
}