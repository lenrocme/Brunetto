package com.example.brunetto.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.brunetto.data.lastInput.LastInput

/**
 * Fill the text fields with data from db
 * */
class TaxViewModel : ViewModel() {
    // Social versicherung part of the tax
    var brutSalary : String by mutableStateOf("50000")
    var factorByTaxClassFour : String by mutableStateOf("")

    var additionalAmount : String by mutableStateOf("1.3")
    var anpkvPayedPremium : String by mutableStateOf("300")
    var pkpvBasicPremium : String by mutableStateOf("500")

    var sonstb : String by mutableStateOf("")
    var jsonstb : String by mutableStateOf("")

    var vmt : String by mutableStateOf("")
    var entsch : String by mutableStateOf("")

    var wfundf : String by mutableStateOf("")
    var hinzur : String by mutableStateOf("")

    fun setDataFromTheDbLastInput(lastInput : LastInput) {
        this.brutSalary = transformDoubleToOutputString(lastInput.salaryBrut)

        this.factorByTaxClassFour = if (lastInput.onClassFour == 1.0)
            ""
        else
            lastInput.onClassFour.toString()

        // about health insurance
        this.additionalAmount = transformDoubleToOutputString(lastInput.additionalAmount)

        // private health insurance
        this.anpkvPayedPremium = transformDoubleToOutputString(lastInput.anpkvPayedPremium)
        this.pkpvBasicPremium = transformDoubleToOutputString(lastInput.pkpvBasicPremium)

        // optional taxes
        this.sonstb = transformDoubleToOutputString(lastInput.sonstb)
        this.jsonstb = transformDoubleToOutputString(lastInput.jsonstb)

        this.vmt = transformDoubleToOutputString(lastInput.vmt)
        this.entsch = transformDoubleToOutputString(lastInput.entsch)

        this.wfundf = transformDoubleToOutputString(lastInput.wfundf)
        this.hinzur = transformDoubleToOutputString(lastInput.hinzur)
    }

    /**
     * Transform double value from to string value for output in text fields
     * @param input The double value to be transformed
     * @return The transformed string value of parameter
     * */
    private fun transformDoubleToOutputString(input: Double): String {
        return if (input == 0.0)
            ""
        else
            input.toString()
    }
}