package com.example.brunetto.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.brunetto.data.lastInput.LastInput

class LegacyTaxModelView : ViewModel() {

    var mOutputTxt : TaxViewModel by mutableStateOf(TaxViewModel())

    // main input
    var e_re4 : Double by mutableStateOf(50000.0)     // brutto lohn
    var e_lzz : Double by mutableStateOf(1.0)     // zeitraum  => 1 for Year, 2 for Month
    var isProYear : Boolean by mutableStateOf(true)

    // dropdown menus
    var geb_tag : Double by mutableStateOf(0.0)     // jahr gebohren
    var e_stkl : Double by mutableStateOf(1.0)      // steuer klasse  & faczeigen()
    var e_f : Double by mutableStateOf(1.0)     // only with str class 4, between 0 and 1, no more then 3 digit after comma
    var e_zkf : Double by mutableStateOf(0.0)       //kinder frei betr, number of childs
    var e_bundesland : Int by mutableStateOf(1)     // krv by index +1
    var e_r : Double by mutableStateOf(8.0)             // kirch steuer

    // checkboxes
    var kinderlos : Boolean by mutableStateOf(true)         // no children above 23 years old
    var e_krv : Boolean by mutableStateOf(true)
    var e_av : Boolean by mutableStateOf(true)              //arbetislos vers

    // about health insurance
    var e_barmer : Double by mutableStateOf(14.6)
    var e_kvz : Double by mutableStateOf(1.3)

    // private health insurance !! not used yet in the calculation
    var isPrivatInsur : Boolean by mutableStateOf(false)
    var e_anpkv : Double by mutableStateOf(300.0)     // call listener pkvwahl()  //was not used
    var mitag : Boolean by mutableStateOf(true)
    var nachweis : Boolean by mutableStateOf(false)                             //was not used
    var e_pkpv : Double by mutableStateOf(500.0)     // call listener pkvwahl()   //was not used

    // last points optional !! not used yet in the calculation
    var e_sonstb : Double by mutableStateOf(0.0)
    var e_jsonstb : Double by mutableStateOf(0.0)
    var e_vmt : Double by mutableStateOf(0.0)
    var e_entsch : Double by mutableStateOf(0.0)
    var e_wfundf : Double by mutableStateOf(0.0)
    var e_hinzur : Double by mutableStateOf(0.0)

    // view model
    var selectedOptionKinderZahl : String by mutableStateOf("--")

    fun getTheEntityLastInput() : LastInput {
        return LastInput(salaryBrut = e_re4,
            timePeriod = e_lzz,
            isProYear = isProYear,
            birthday = geb_tag,
            taxClass = e_stkl,
            onClassFour = e_f,
            childrenConst = e_zkf,
            statNumb = e_bundesland,
            churchTax = e_r,
            noChildren = kinderlos,
            pensionIns = e_krv,
            noJobIns = e_av,
            healthIns = e_barmer,
            additionalAmount = e_kvz,
            isPrivateIns = isPrivatInsur,
            anpkvPayedPremium = e_anpkv,
            mitag = mitag,
            nachweis = nachweis,
            pkpvBasicPremium = e_pkpv,
            sonstb = e_sonstb,
            jsonstb = e_jsonstb,
            vmt = e_vmt,
            entsch = e_entsch,
            wfundf = e_wfundf,
            hinzur = e_hinzur,
            selectedOptionKinderZahl = selectedOptionKinderZahl,
        )
    }

    fun setDataFromTheDbLastInput(lastInput : LastInput) {
        e_re4 = lastInput.salaryBrut
        e_lzz = lastInput.timePeriod
        isProYear = lastInput.isProYear

        // dropdown menus
        geb_tag = lastInput.birthday
        e_stkl = lastInput.taxClass
        e_f = lastInput.onClassFour
        e_zkf = lastInput.childrenConst
        e_bundesland = lastInput.statNumb
        e_r = lastInput.churchTax

        // checkboxes
        kinderlos = lastInput.noChildren
        e_krv = lastInput.pensionIns
        e_av = lastInput.noJobIns

        // about health insurance
        e_barmer = lastInput.healthIns
        e_kvz = lastInput.additionalAmount

        // private health insurance
        isPrivatInsur = lastInput.isPrivateIns
        e_anpkv = lastInput.anpkvPayedPremium
        mitag = lastInput.mitag
        nachweis   = lastInput.nachweis
        e_pkpv = lastInput.pkpvBasicPremium

        // optional taxes
        e_sonstb = lastInput.sonstb
        e_jsonstb = lastInput.jsonstb
        e_vmt = lastInput.vmt
        e_entsch = lastInput.entsch
        e_wfundf = lastInput.wfundf
        e_hinzur = lastInput.hinzur
        selectedOptionKinderZahl = lastInput.selectedOptionKinderZahl
    }
}