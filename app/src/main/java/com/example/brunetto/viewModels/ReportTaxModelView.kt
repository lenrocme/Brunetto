package com.example.brunetto.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ReportTaxModelView : ViewModel(){

    var taxes : Double by mutableStateOf(0.0)           // steur
    var taxesByBrutto : Double by mutableStateOf(0.0)   // lstlzz
    var oneTimePay : Double by mutableStateOf(0.0)      // sts
    var multiYearEmploy : Double by mutableStateOf(0.0)             // stv
    var solidaritat : Double by mutableStateOf(0.0)            // soli
    var churchTax : Double by mutableStateOf(0.0)        // kisteuer
    var sumTax : Double by mutableStateOf(0.0)       // summTaxes

    var pension : Double by mutableStateOf(0.0)       // rentewert
    var unemployed : Double by mutableStateOf(0.0)        // aloswert
    var medInsurance : Double by mutableStateOf(0.0)          // kvwert
    var careInsurance : Double by mutableStateOf(0.0)      // pflegewert
    var socialSum : Double by mutableStateOf(0.0)       // sozabgabe

    var netSalary : Double by mutableStateOf(0.0)           // netto

    var pensionCompany : Double by mutableStateOf(0.0)     // rentewertag
    var unemployedCompany : Double by mutableStateOf(0.0)      // aloswertag
    var medInsuranceCompany : Double by mutableStateOf(0.0)        // kvwertag
    var careInsuranceCompany : Double by mutableStateOf(0.0)    // pflegewertag
    var socialSumCompany : Double by mutableStateOf(0.0)     // agsozabgabe

    var totalLoadCompany : Double by mutableStateOf(0.0)    // employerPart

}