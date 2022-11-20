package com.example.brunetto

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.brunetto.helpers.*
import com.example.brunetto.ui.theme.BrunettoTheme
import com.example.brunetto.viewModels.LegacyTaxModelView
import com.example.brunetto.viewModels.TaxViewModel

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
                    MainActivityScreen()
                }
            }
        }
    }
}

@Composable
fun MainActivityScreen() {
    val focusManager = LocalFocusManager.current
    val taxViewModel = LegacyTaxModelView()
    Box(
        modifier = Modifier
            .fillMaxSize()
        /* .background(MaterialTheme.myColors.CL_BackGround)
         .pointerInput(Unit) {
             detectTapGestures(onTap = {
                 focusManager.clearFocus()
             })
         },*/
    ) {
        Column() {
            Header(taxViewModel)
            Body(taxViewModel)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    BrunettoTheme {
        MainActivityScreen()
    }
}

@Composable
fun Header(taxViewModel: LegacyTaxModelView) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Spacer(modifier = Modifier
            .height(percentHeight(adaptHeight(.035f, .05f, 0.065f)) - 19.dp)
            .fillMaxWidth())
        Text(
            text = "Netto jährlich: "
        )
        Text(
            text = "Netto monatlich: "
        )
    }
}

@Composable
fun Body(taxViewModel: LegacyTaxModelView) {
    val state = rememberScrollState()
    val calcTaxViewModel = TaxViewModel()
    val mainCalcTax = CalcTax()
    val focusManager = LocalFocusManager.current
    var bruttoLohn by remember { mutableStateOf("") }

    /**
     *  Option for dropdown menu
     * */
    val optionsKinder = listOf("--", "0.5", "1.0", "1.5", "2.0", "2.5", "3.0", "3.5", "4.0",
        "4.5", "5.0", "5.5", "6.0", "6.5", "7.0", "7.5")

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
                .verticalScroll(state),
        ) {
            Spacer(modifier = Modifier
                .height(percentHeight(.01f))
                .fillMaxWidth())
            OutlinedTextField(
                value = bruttoLohn,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                onValueChange = {
                    bruttoLohn = it
                })

            /**
             * The Card for the "Steur class" and when the option 4 is selected => active "Ehegattenfaktor"
             * */
            SteuerClass(taxViewModel)

            /**
             * The Card with "Kinderfreibeträge" drop down menu
             * */
            DropDownMenu("Kinderfreibeträge", optionsKinder, taxViewModel)

            /**
             * The Card with "Bundesland" drop down menu & and checkbox for "kirchsteur"
             * */
            DropDownMenu_Bundesland("Bundesland", taxViewModel)

            Text(
                text = bruttoLohn
            )
            Button(
                onClick = {
                    mainCalcTax.setCalculatedTaxes(bruttoLohn.toDouble(), calcTaxViewModel)
                    globLogs(calcTaxViewModel)
                }) {

            }

            Button(
                onClick = {
                    //CalculationLegacy().setData()
                    Log.d("taxes", "steurclass: " + taxViewModel.e_stkl)
                    Log.d("taxes", "when steuer 4, value: " + taxViewModel.e_f)
                    Log.d("taxes", "zahl der kinder: " + taxViewModel.e_zkf)
                    Log.d("taxes", "Bundesland: " + taxViewModel.e_bundesland)
                    Log.d("taxes", "Kirchsteuer: " + taxViewModel.e_r)
                }) {

            }
        }
    }
}

@Composable
fun SteuerClass(taxViewModel: LegacyTaxModelView) {
    var selectedOption by remember { mutableStateOf(1) }
    var valueEhegattenfaktor by remember { mutableStateOf("") }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            // .height(percentHeight(.15f))
            .padding(percentWidth(.06f)),
        elevation = 5.dp
    ) {
        Column() {
            Spacer(modifier = Modifier
                .height(10.dp)
                .fillMaxWidth())
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth(),
            ){
                Surface(
                    elevation = 5.dp,
                    shape = CircleShape,
                    color = Color.LightGray,
                    modifier = Modifier
                        .clickable(true) {
                            selectedOption = 1
                            taxViewModel.e_stkl = selectedOption.toDouble()
                        }
                        .width(42.dp)
                        .height(42.dp),
                ){
                    Text(
                        modifier = Modifier
                            .padding(top = 5.dp),
                        text = "I",
                        fontSize = 23.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center)
                }
                Surface(
                    elevation = 5.dp,
                    shape = CircleShape,
                    color = Color.LightGray,
                    modifier = Modifier
                        .clickable(true) {
                            selectedOption = 2
                            taxViewModel.e_stkl = selectedOption.toDouble()
                        }
                        .width(42.dp)
                        .height(42.dp),
                ){
                    Text(
                        modifier = Modifier
                            .padding(top = 5.dp),
                        text = "II",
                        fontSize = 23.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center)
                }
                Surface(
                    elevation = 5.dp,
                    shape = CircleShape,
                    color = Color.LightGray,
                    modifier = Modifier
                        .clickable(true) {
                            selectedOption = 3
                            taxViewModel.e_stkl = selectedOption.toDouble()
                        }
                        .width(42.dp)
                        .height(42.dp),
                ){
                    Text(
                        modifier = Modifier
                            .padding(top = 5.dp),
                        text = "III",
                        fontSize = 23.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center)
                }
                Surface(
                    elevation = 5.dp,
                    shape = CircleShape,
                    color = Color.LightGray,
                    modifier = Modifier
                        .clickable(true) {
                            selectedOption = 4
                            taxViewModel.e_stkl = selectedOption.toDouble()
                        }
                        .width(42.dp)
                        .height(42.dp),
                ){
                    Text(
                        modifier = Modifier
                            .padding(top = 5.dp),
                        text = "IV",
                        fontSize = 23.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center)
                }
                Surface(
                    elevation = 5.dp,
                    shape = CircleShape,
                    color = Color.LightGray,
                    modifier = Modifier
                        .clickable(true) {
                            selectedOption = 5
                            taxViewModel.e_stkl = selectedOption.toDouble()
                        }
                        .width(42.dp)
                        .height(42.dp),
                ){
                    Text(
                        modifier = Modifier
                            .padding(top = 5.dp),
                        text = "V",
                        fontSize = 23.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center)
                }
                Surface(
                    elevation = 5.dp,
                    shape = CircleShape,
                    color = Color.LightGray,
                    modifier = Modifier
                        .clickable(true) {
                            selectedOption = 6
                            taxViewModel.e_stkl = selectedOption.toDouble()
                        }
                        .width(42.dp)
                        .height(42.dp),
                ){
                    Text(
                        modifier = Modifier
                            .padding(top = 5.dp),
                        text = "VI",
                        fontSize = 23.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center)
                }
            }

            if (selectedOption == 4) {
                Spacer(modifier = Modifier
                    .height(10.dp)
                    .fillMaxWidth())
                TextField(  //OutlinedTextField
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = valueEhegattenfaktor,
                    onValueChange = {
                        if (it.isEmpty()) {
                            valueEhegattenfaktor = it
                            taxViewModel.e_f = 1.0
                        }
                        else {
                            valueEhegattenfaktor = it
                            taxViewModel.e_f = valueEhegattenfaktor.toDouble()
                        }
                    },
                    label = { Text(text = "Ehegattenfaktor") },
                )
            }
            Spacer(modifier = Modifier
                .height(10.dp)
                .fillMaxWidth())
        }
    }


}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DropDownMenu(textLabel: String, optionsDropMenu: List<String>, taxViewModel: LegacyTaxModelView) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf(optionsDropMenu[0]) }


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(percentWidth(.06f)),
        elevation = 5.dp
    ) {
        //Text(text = textLabel)
        ExposedDropdownMenuBox(
            modifier = Modifier,
                //.fillMaxWidth()
               // .padding(horizontal = percentWidth(.06f)),
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {
            TextField(  //OutlinedTextField
                modifier = Modifier
                    .fillMaxWidth(),
                readOnly = true,
                value = selectedOptionText,
                onValueChange = {},
                label = { Text(text = textLabel) },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = expanded
                    )
                },
                colors = ExposedDropdownMenuDefaults.textFieldColors()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                },
            ) {
                optionsDropMenu.forEach { selectionOption ->
                    DropdownMenuItem(
                        onClick = {
                            selectedOptionText = selectionOption
                            expanded = false
                            taxViewModel.e_zkf = selectedOptionText.toDouble()  // change children numbers
                        }
                    ) {
                        Text(text = selectionOption)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DropDownMenu_Bundesland(textLabel: String, taxViewModel: LegacyTaxModelView) {
    var expanded by remember { mutableStateOf(false) }
    var checkedState by remember { mutableStateOf(true) }

    val optionsDropMenu = listOf("Baden-Württemberg", "Bayern", "Berlin(West)", "Berlin(Ost)", "Brandenburg",
        "Bremen/Bremerhaven", "Hamburg", "Hessen", "Mecklenburg-Vorpommern", "Niedersachsen", "Nordrhein-Westfalen",
        "Rheinland-Pfalz", "Saarland", "Sachsen", "Sachsen-Anhalt", "Schleswig-Holstein", "Thüringen",)
    var selectedOptionLand by remember { mutableStateOf(optionsDropMenu[0]) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(percentWidth(.06f)),
        elevation = 5.dp
    ) {
        Column()
        {
            //Text(text = textLabel)
            ExposedDropdownMenuBox(
                modifier = Modifier,
                //.fillMaxWidth()
                // .padding(horizontal = percentWidth(.06f)),
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                }
            ) {
                TextField(  //OutlinedTextField
                    modifier = Modifier
                        .fillMaxWidth(),
                    readOnly = true,
                    value = selectedOptionLand,
                    onValueChange = {},
                    label = { Text(text = textLabel) },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = expanded
                        )
                    },
                    colors = ExposedDropdownMenuDefaults.textFieldColors()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {
                        expanded = false
                    },
                ) {
                    optionsDropMenu.forEach { selectionOption ->
                        DropdownMenuItem(
                            onClick = {
                                selectedOptionLand = selectionOption
                                expanded = false
                                taxViewModel.e_bundesland = optionsDropMenu.indexOf(selectedOptionLand) + 1
                                taxViewModel.e_r = SetKirchSteur(checkedState, selectedOptionLand)
                            }
                        ) {
                            Text(text = selectionOption)
                        }
                    }
                }
            }
            Row() {
                Checkbox(
                    checked = checkedState,
                    onCheckedChange = {
                        checkedState = it
                        taxViewModel.e_r = SetKirchSteur(checkedState, selectedOptionLand)
                    },
                    /*colors = CheckboxDefaults.colors(
                        checkedColor = MaterialTheme.myColors.main_400,
                        uncheckedColor = MaterialTheme.myColors.main_350
                    )*/
                )
                //Text(text = "Kirchensteur:")
                Text(
                    text = if (checkedState == false)
                            "Kirchensteur: 0%"
                        else
                            if (selectedOptionLand == "Bayern" || selectedOptionLand == "Baden-Württemberg")
                                "Kirchensteur: 8%"
                            else
                                "Kirchensteur: 9%"
                )
            }
        }
    }
}

/**
 * Set the value to view model for "kirchsteuer"
 * @param checkedState The state of checkbox, true for active "krichsteuer"
 * @param selectedOptionLand The select land as option from drop down menu
 * @return The "kirchsteur" value
 * */
private fun SetKirchSteur(checkedState: Boolean, selectedOptionLand: String) : Double {
    return if (!checkedState)
        0.0
    else
        if (selectedOptionLand == "Bayern" || selectedOptionLand == "Baden-Württemberg")
            8.0
        else
            9.0
}