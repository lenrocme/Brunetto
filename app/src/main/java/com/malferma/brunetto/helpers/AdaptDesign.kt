package com.malferma.brunetto.helpers

import android.content.res.Resources
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp



/**
 *  Get the width of the active screen
 *  @return The width of the screen
 * */
private fun getScreenWidth(): Dp {
    val density = Resources.getSystem().displayMetrics.density
    val pxl = Resources.getSystem().displayMetrics.widthPixels
    return (pxl / density).dp
}

/**
 *  Get the height of the active screen
 *  @return The height of the screen
 * */
private fun getScreenHeight(): Dp {
    val density = Resources.getSystem().displayMetrics.density
    val pxl = Resources.getSystem().displayMetrics.heightPixels
    return (pxl / density).dp
}

/**
 *  Calculate the percent of the width of the screen
 *  @param coefficient The percent value
 *  @return The value in Dp corresponding to the percentage of width of the screen
 * */
fun percentWidth(coefficient: Float): Dp {
    return getScreenWidth() * coefficient
}

/**
 *  Calculate the percent of the height of the screen
 *  @param coefficient The percent value
 *  @return The value in Dp corresponding to the percentage of height of the screen
 * */
fun percentHeight(coefficient: Float): Dp {
    return getScreenHeight() * coefficient
}

/**
 *  Adapt the corresponding value regard the width of the screen
 *  @param small The value of width for the display with small width < 320
 *  @param middle The value of width for the display with middle width < 600
 *  @param upMiddle The value of width for the display with upMiddle width > 600
 *  @return The parameter value(width)  corresponding to the width of actual screen
 * */
fun adaptWidth(small: Dp, middle: Dp, upMiddle: Dp): Dp {
    val smallWidth = 320.dp
    //val middleWidth = 380.dp
    val upMiddleWidth = 600.dp
    val screenWidth = getScreenWidth()
    return if (smallWidth > screenWidth)
        small
    else if (upMiddleWidth < screenWidth)
        upMiddle
    else
        middle
}

/**
 *  Adapt the corresponding value regard the width of the screen
 *  @see adaptWidth overloading
 *  @param small The value of width for the display with small width < 320
 *  @param middle The value of width for the display with middle width < 600
 *  @param upMiddle The value of width for the display with upMiddle width > 600
 *  @return The parameter value(width) corresponding to the width of actual screen
 * */
fun adaptWidth(small: Float, middle: Float, upMiddle: Float): Float {
    val smallWidth = 320.dp
    //val middleWidth = 380.dp
    val upMiddleWidth = 600.dp
    val screenWidth = getScreenWidth()
    return if (smallWidth > screenWidth)
        small
    else if (upMiddleWidth < screenWidth)
        upMiddle
    else
        middle
}

/**
 *  Adapt the corresponding value regard the height of the screen
 *  @param small The value of height for the display with small height
 *  @param middle The value of height for the display with middle height
 *  @param upMiddle The value of height for the display with upMiddle height
 *  @return The parameter value(height) corresponding to the height of actual screen
 * */
fun adaptHeight(small: Dp, middle: Dp, upMiddle: Dp): Dp {
    val smallHeight = 680.dp
    //val middleHeight = 380.dp
    val upMiddleHeight = 800.dp
    val screenHeight = getScreenHeight()
    return if (smallHeight > screenHeight)
        small
    else if (upMiddleHeight < screenHeight)
        upMiddle
    else
        middle
}

/**
 *  Adapt the corresponding value regard the height of the screen
 *  @see adaptHeight overloading
 *  @param small The value of height for the display with small height
 *  @param middle The value of height for the display with middle height
 *  @param upMiddle The value of height for the display with upMiddle height
 *  @return The parameter value(height) corresponding to the height of actual screen
 * */
fun adaptHeight(small: Float, middle: Float, upMiddle: Float): Float {
    val smallHeight= 680.dp
    //val middleHeight = 380.dp
    val upMiddleHeight = 800.dp
    val screenHeight = getScreenHeight()
    return if (smallHeight > screenHeight)
        small
    else if (upMiddleHeight < screenHeight)
        upMiddle
    else
        middle
}

/**
 *  Adapt the font size corresponding to the size of active display
 *  @param small The value of font size for the display with small width
 *  @param middle The value of font size  for the display with middle width
 *  @param upMiddle The value of font size  for the display with upMiddle width
 *  @return The parameter value(font-size) corresponding to the width of actual screen
 * */
fun adaptFont(small: TextUnit, middle: TextUnit, upMiddle: TextUnit): TextUnit {
    val smallWidth = 320.dp
    //val middleWidth = 380.dp
    val upMiddleWidth = 600.dp
    val screenWidth = getScreenWidth()
    return if (smallWidth > screenWidth)
        small
    else if (upMiddleWidth < screenWidth)
        upMiddle
    else
        middle
}