package com.malferma.brunetto.ui.theme

import androidx.compose.material.Colors
import androidx.compose.ui.graphics.Color

data class CustomColors(
    val material: Colors,
    val bg_card: Color,
    val main_100: Color,
    val main_200: Color,
    val main_300: Color,
    val main_350: Color,
    val main_400: Color,
    val main_420: Color,
    val main_450: Color,
    val main_500: Color,
    val main_600: Color,

    val CL_BackGround: Color,
    val bg_SumTax: Color,
    val focusLine: Color,
    val iconButton: Color,
    val checkedCheckbox: Color,
    val unCheckedCheckbox: Color,
    val txtIconBtn: Color,
    val switchThumb: Color,
    val switchTrack: Color,
    val bgTaxClass: Color,
    val bgTaxClassSelect: Color,

    /**
     * Font colors
     * */
    val fontTaxButton: Color,
    val fontC_100: Color,
    val fontC_200: Color,
    val fontC_300: Color,
    val fontHeader: Color,
    val fontLabelHeadTax: Color,
    val fontLabelCard: Color,
    val fontCheckedCheckbox: Color,
    val fontUnCheckedCheckbox: Color,

    /**
     * Bottom nav bar
     * */
    var CL_BottomNavMenuNORMAL: Color,
    var CL_BottomNavMenuSELECTED: Color,
    var CL_BottomNavMenu: Color,
) {
    val primary: Color get() = material.primary
    val primaryVariant: Color get() = material.primaryVariant
    val secondary: Color get() = material.secondary
    val secondaryVariant: Color get() = material.secondaryVariant
    val background: Color get() = material.background
    val surface: Color get() = material.surface
    val error: Color get() = material.error
    val onPrimary: Color get() = material.onPrimary
    val onSecondary: Color get() = material.onSecondary
    val onBackground: Color get() = material.onBackground
    val onSurface: Color get() = material.onSurface
    val onError: Color get() = material.onError
    val isLight: Boolean get() = material.isLight
}