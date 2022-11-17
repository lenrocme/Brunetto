package com.example.brunetto.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class CalcTaxViewModel : ViewModel() {

    // Social versicherung part of the tax
    var totalSocialVers : Double by mutableStateOf(0.0)
    var rentenVers : Double by mutableStateOf(0.0)
    var arbeitslosenVers : Double by mutableStateOf(0.0)
    var krankVers : Double by mutableStateOf(0.0)
    var kvZusatzBetr : Double by mutableStateOf(0.0)
    var plfegVers : Double by mutableStateOf(0.0)
}