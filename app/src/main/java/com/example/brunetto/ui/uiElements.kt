package com.example.brunetto.ui

import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import com.example.brunetto.ui.theme.myColors

object UiElem{
    @Composable
    fun colorsOfTextField() = TextFieldDefaults.textFieldColors(
        textColor = MaterialTheme.myColors.main_450,
        //disabledTextColor = disabledTextColor,
        backgroundColor = MaterialTheme.myColors.bg_card,
        cursorColor = MaterialTheme.myColors.main_450,
        focusedIndicatorColor = MaterialTheme.myColors.focusLine,
        focusedLabelColor = MaterialTheme.myColors.main_450,
        unfocusedLabelColor = MaterialTheme.myColors.main_420,
        unfocusedIndicatorColor = MaterialTheme.myColors.main_350,
        trailingIconColor = MaterialTheme.myColors.main_450,
        //errorCursorColor = errorCursorColor,
    )

    @Composable
    fun colorsOfDropDown() = TextFieldDefaults.textFieldColors(
        textColor = MaterialTheme.myColors.main_450,
        //disabledTextColor = disabledTextColor,
        backgroundColor = MaterialTheme.myColors.main_300,
        cursorColor = MaterialTheme.myColors.main_450,
        focusedIndicatorColor = MaterialTheme.myColors.main_450,
        focusedLabelColor = MaterialTheme.myColors.main_450,
        unfocusedLabelColor = MaterialTheme.myColors.main_420,
        unfocusedIndicatorColor = MaterialTheme.myColors.main_350,
        trailingIconColor = MaterialTheme.myColors.main_450,
        //errorCursorColor = errorCursorColor,
    )
}
