package com.example.brunetto.ui

import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.brunetto.ui.theme.myColors



object UiElem{
    @Composable
    fun colorsOfTextField() = TextFieldDefaults.textFieldColors(
        //textColor = MaterialTheme.myColors.fontC_100,
        //disabledTextColor = disabledTextColor,
        backgroundColor = Color.White,
        cursorColor = MaterialTheme.myColors.main_450,
        focusedIndicatorColor = MaterialTheme.myColors.main_450,
        //unfocusedIndicatorColor = ,

        //errorCursorColor = errorCursorColor,
    )
}
