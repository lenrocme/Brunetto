package com.malferma.brunetto.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable

val MaterialTheme.myColors: CustomColors
    @Composable
    @ReadOnlyComposable
    get() = LocalColors.current