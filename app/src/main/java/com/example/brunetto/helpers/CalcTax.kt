package com.example.brunetto.helpers

import com.example.brunetto.models.taxes.ConstTax
import com.example.brunetto.viewModels.CalcTaxViewModel

class CalcTax {
    // Predefined const taxes
    private val constTax : ConstTax = ConstTax()

    /**
     *  Calculate the taxes with only predefined & not changeable coff
     *  @param yearsBrut The sum of loan for the entire year
     *  @param calcTaxViewMod The view-model for the screen taxes
     * */
    fun setConstTaxes(yearsBrut: Double, calcTaxViewMod: CalcTaxViewModel) {
        calcTaxViewMod.rentenVers = yearsBrut * constTax.rentenVers / 100
        calcTaxViewMod.arbeitslosenVers = yearsBrut * constTax.arbeitslosenVers / 100
        calcTaxViewMod.krankVers = yearsBrut * constTax.krankVers / 100
        calcTaxViewMod.kvZusatzBetr = yearsBrut * constTax.kvZusatzBetr / 100
        calcTaxViewMod.plfegVers = yearsBrut * constTax.plfegVers / 100
    }

    /**
     *  Calculate the total taxes of social assurance part
     *  @param calcTaxViewMod The view-model for the screen taxes
     * */
    fun setTotalSocialVers(calcTaxViewMod: CalcTaxViewModel) {
        calcTaxViewMod.totalSocialVers =
            calcTaxViewMod.rentenVers +
            calcTaxViewMod.arbeitslosenVers +
            calcTaxViewMod.krankVers +
            calcTaxViewMod.kvZusatzBetr +
            calcTaxViewMod.plfegVers
    }
}