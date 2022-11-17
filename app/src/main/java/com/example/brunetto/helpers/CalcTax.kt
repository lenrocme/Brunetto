package com.example.brunetto.helpers

import com.example.brunetto.models.taxes.ConstTax
import com.example.brunetto.viewModels.TaxViewModel

class CalcTax {
    // Predefined const taxes
    private val constTax : ConstTax = ConstTax()



    fun setCalculatedTaxes(yearsBrut: Double, calcTaxViewMod: TaxViewModel) {
        setConstTaxes(yearsBrut, calcTaxViewMod)
        setTotalSocialVers(calcTaxViewMod)
    }

    /**
     *  Calculate the taxes with only predefined & not changeable coff
     *  @param yearsBrut The sum of loan for the entire year
     *  @param calcTaxViewMod The view-model for the screen taxes
     * */
    private fun setConstTaxes(yearsBrut: Double, calcTaxViewMod: TaxViewModel) {
        calcTaxViewMod.rentenVers = yearsBrut * constTax.rentenVers / 100.0
        calcTaxViewMod.arbeitslosenVers = yearsBrut * constTax.arbeitslosenVers / 100.0
        calcTaxViewMod.krankVers = yearsBrut * constTax.krankVers / 100.0
        calcTaxViewMod.kvZusatzBetr = yearsBrut * constTax.kvZusatzBetr / 100.0
        calcTaxViewMod.plfegVers = yearsBrut * constTax.plfegVers / 100.0
    }

    /**
     *  Calculate the total taxes of social assurance part
     *  @param calcTaxViewMod The view-model for the screen taxes
     * */
    private fun setTotalSocialVers(calcTaxViewMod: TaxViewModel) {
        calcTaxViewMod.totalSocialVers =
            calcTaxViewMod.rentenVers +
            calcTaxViewMod.arbeitslosenVers +
            calcTaxViewMod.krankVers +
            calcTaxViewMod.kvZusatzBetr +
            calcTaxViewMod.plfegVers
    }
}