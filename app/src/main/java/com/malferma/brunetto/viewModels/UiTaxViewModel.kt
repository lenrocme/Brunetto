package com.malferma.brunetto.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class UiTaxViewModel: ViewModel() {
    var isTaxReportExtended: Boolean by mutableStateOf(false)
}