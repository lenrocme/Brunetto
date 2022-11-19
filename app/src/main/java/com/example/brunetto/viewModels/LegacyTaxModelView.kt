package com.example.brunetto.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class LegacyTaxModelView : ViewModel() {

    // main input
    var e_re4 : Double by mutableStateOf(90000.0)     // brutto lohn
    var e_lzz : Int by mutableStateOf(1)     // zeitraum

    // dropdown menus
    var geb_tag : Double by mutableStateOf(0.0)     // jahr gebohren
    var e_stkl : Double by mutableStateOf(4.0)      // steuer klasse  & faczeigen()
    var e_f : Double by mutableStateOf(0.0)     // only with str class 4, between 0 and 1, no more then 3 digit after comma
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

    // private health insurance
    var e_anpkv : Double by mutableStateOf(0.0)     // call listener pkvwahl()
    var mitag : Boolean by mutableStateOf(false)
    var nachweis : Boolean by mutableStateOf(false)
    var e_pkpv : Double by mutableStateOf(0.0)     // call listener pkvwahl()

    // last points optional
    var e_sonstb : Double by mutableStateOf(0.0)
    var e_jsonstb : Double by mutableStateOf(0.0)
    var e_vmt : Double by mutableStateOf(0.0)
    var e_entsch : Double by mutableStateOf(0.0)
    var e_wfundf : Double by mutableStateOf(0.0)
    var e_hinzur : Double by mutableStateOf(0.0)





}