package com.malferma.brunetto.models.taxes

data class ConstTax(
    // Values are used as percent
    val rentenVers : Double = 9.3,
    val arbeitslosenVers : Double = 1.2,
    val krankVers : Double = 7.3,
    val kvZusatzBetr : Double = 0.65,
    val plfegVers : Double = 1.525,
)
