package com.example.brunetto.models.taxes

data class ConstTax(
    val rentenVers : Float = .093f,
    val arbeitslosenVers : Float = .012f,
    val krankVers : Float = .073f,
    val kvZusatzBetr : Float = .0065f,
    val plfegVers : Float = .01525f,
)
