package com.example.brunetto.ui.theme

import androidx.compose.material.darkColors
import androidx.compose.material.lightColors

val LightColorPalette = CustomColors(
    material = lightColors(),
    bg_card = Light_neumorphism_0,
    main_100 = Light_neumorphism_100,
    main_200 = Light_neumorphism_200,
    main_300 = Light_neumorphism_300,
    main_350 = Light_neumorphism_350,
    main_400 = Light_neumorphism_400,
    main_420 = Light_neumorphism_420,
    main_450 = Light_neumorphism_450,
    main_500 = Light_neumorphism_500,
    main_600 = Light_neumorphism_600,

    bg_SumTax = Light_neumorphism_400,
    focusLine = Light_neumorphism_400,
    /**
     * Font colors
     * */
    fontC_100 = fontC_100d,
    fontC_200 = fontC_200d,
    fontC_300 = fontC_300d,
    /**
     * Main background
     * */
    CL_BackGround = COLOR_BackGround,
    /**
     * Bottom nav bar
     * */
    CL_BottomNavMenuNORMAL = COLOR_BottomNavMenuNORMAL,
    CL_BottomNavMenuSELECTED = COLOR_BottomNavMenuSELECTED,
    CL_BottomNavMenu = COLOR_BottomNavMenu,
)

val DarkColorPalette = CustomColors(
    material = darkColors(),
    bg_card = Dark_neumorphism_0,
    main_100 = Dark_neumorphism_100,
    main_200 = Dark_neumorphism_200,
    main_300 = Dark_neumorphism_300,
    main_350 = Dark_neumorphism_350,
    main_400 = Dark_neumorphism_400,
    main_420 = Dark_neumorphism_420,
    main_450 = Dark_neumorphism_450,
    main_500 = Dark_neumorphism_500,
    main_600 = Dark_neumorphism_600,

    bg_SumTax = Light_neumorphism_450,
    focusLine = Light_neumorphism_420,
    /**
     * Font colors
     * */
    fontC_100 = fontC_100l,
    fontC_200 = fontC_200l,
    fontC_300 = fontC_300l,
    /**
     * Main background
     * */
    CL_BackGround = Dark_COLOR_BackGround,
    /**
     * Bottom nav bar
     * */
    CL_BottomNavMenuNORMAL = Dark_COLOR_BottomNavMenuNORMAL,
    CL_BottomNavMenuSELECTED = Dark_COLOR_BottomNavMenuSELECTED,
    CL_BottomNavMenu = Dark_COLOR_BottomNavMenu,
)

/*
private val DarkColorPalette = darkColors(
    primary = Purple200,
    primaryVariant = Purple700,
    secondary = Teal200
)

private val LightColorPalette = lightColors(
    primary = Purple500,
    primaryVariant = Purple700,
    secondary = Teal200
)*/
/*
@Composable
fun BrunettoTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}*/