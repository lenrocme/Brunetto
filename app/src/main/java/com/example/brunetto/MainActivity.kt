package com.example.brunetto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import com.example.brunetto.ui.theme.BrunettoTheme
import com.example.brunetto.viewModels.CalcTaxViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BrunettoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Main()
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    BrunettoTheme {
        Greeting("Android")
    }
}

@Composable
fun Main() {
    val calcTaxViewModel = CalcTaxViewModel()
    val focusManager = LocalFocusManager.current
    var userInput by remember { mutableStateOf("") }
    Box(
        modifier = Modifier
            .fillMaxSize()
            /*.background(MaterialTheme.myColors.CL_BackGround)
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            },*/
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight(1f)
        ) {
            OutlinedTextField(
                value = userInput,
                onValueChange = {
                    userInput = it
            })
        }
    }
}