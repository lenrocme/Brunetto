package com.example.brunetto

import android.util.Log
import com.example.brunetto.viewModels.TaxViewModel
import java.text.DecimalFormat


fun globLogs(calcTaxViewMod: TaxViewModel) {
    val dec = DecimalFormat("0.00")
    Log.d("taxes", "totalSocialVers:  " + calcTaxViewMod.totalSocialVers.toString())
    Log.d("taxes", "rentenVers:  " + calcTaxViewMod.rentenVers.toString())
    Log.d("taxes", "arbeitslosenVers:  " + calcTaxViewMod.arbeitslosenVers.toString())
    Log.d("taxes", "krankVers:  " + calcTaxViewMod.krankVers.toString())
    Log.d("taxes", "kvZusatzBetr:  " + dec.format(calcTaxViewMod.kvZusatzBetr))
    Log.d("taxes", "plfegVers:  " + calcTaxViewMod.plfegVers.toString())
}