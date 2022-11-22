package com.example.brunetto

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
        Row(modifier = Modifier
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier.width(percentWidth(.5f)),
                text = "Netto jährlich:",
                textAlign = TextAlign.Right,
            )
            Text(
                modifier = Modifier.width(percentWidth(.5f)),
                text = " 90000.00 €",
                textAlign = TextAlign.Left,
            )
        }
        Row(modifier = Modifier
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier.width(percentWidth(.5f)),
                text = "Netto monatlich:",
                textAlign = TextAlign.Right,
            )
            Text(
                modifier = Modifier.width(percentWidth(.5f)),
                text = " 8000.00 €",
                textAlign = TextAlign.Left,
            )
        }
    }
}

@Composable
fun Body(taxViewModel: LegacyTaxModelView) {
    val spaceBetweenCards = percentHeight(.022f)
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
                .height(spaceBetweenCards)
                .fillMaxWidth())
            /*OutlinedTextField(
                value = bruttoLohn,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                onValueChange = {
                    bruttoLohn = it
                })*/

            /**
             * The Card for the "Steur class" and when the option 4 is selected => active "Ehegattenfaktor"
             * */
            SteuerClass(taxViewModel)
            Spacer(modifier = Modifier
                .height(spaceBetweenCards)
                .fillMaxWidth())
            /**
             * The Card with "Bundesland" drop down menu & and checkbox for "kirchsteur"
             * */
            DropDownMenu_Bundesland("Bundesland", taxViewModel)
            Spacer(modifier = Modifier
                .height(spaceBetweenCards)
                .fillMaxWidth())
            /**
             * The Card for the checkboxes like "kinderlos" and > 23 & "renteversicherung" & "arbeitslosversicherung"
             * */
            CheckBoxes(taxViewModel)
            Spacer(modifier = Modifier
                .height(spaceBetweenCards)
                .fillMaxWidth())
            /**
             * The Card for "krankversicherungen" , gesetzliche & private
             * */
            Card_KrankVers(taxViewModel)
            Spacer(modifier = Modifier
                .height(spaceBetweenCards)
                .fillMaxWidth())
            /**
             * The Card for optional bezüge, top one
             * */
            Card_Optional_Top(taxViewModel)
            Spacer(modifier = Modifier
                .height(spaceBetweenCards)
                .fillMaxWidth())
            /**
             * The Card for optional bezüge, midle one
             * */
            Card_Optional_Midle(taxViewModel)
            Spacer(modifier = Modifier
                .height(spaceBetweenCards)
                .fillMaxWidth())
            /**
             * The Card for optional bezüge, bottom one
             * */
            Card_Optional_Bottom(taxViewModel)
            Spacer(modifier = Modifier
                .height(spaceBetweenCards)
                .fillMaxWidth())
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
                    Log.d("taxes", "Zeitraum: " + taxViewModel.e_lzz)
                    Log.d("taxes", "inputed lohn: " + taxViewModel.e_re4)
                    Log.d("taxes", "steurclass: " + taxViewModel.e_stkl)
                    Log.d("taxes", "when steuer 4, value: " + taxViewModel.e_f)
                    Log.d("taxes", "zahl der kinder: " + taxViewModel.e_zkf)
                    Log.d("taxes", "Bundesland: " + taxViewModel.e_bundesland)
                    Log.d("taxes", "Kirchsteuer: " + taxViewModel.e_r)
                    Log.d("taxes", "KinderLos: " + taxViewModel.kinderlos)
                    Log.d("taxes", "renteversicherungspflichtig: " + taxViewModel.e_krv)
                    Log.d("taxes", "arbeitslosenversicherungspflichtig: " + taxViewModel.e_av)
                    Log.d("taxes", "krankenversicherung beitragssatz: " + taxViewModel.e_barmer)
                    Log.d("taxes", "Zusatzbeitrag: " + taxViewModel.e_kvz)
                    Log.d("taxes", "Beitra / Monat: " + taxViewModel.e_anpkv)
                    Log.d("taxes", "Grundsicherung / Monat: " + taxViewModel.e_pkpv)
                    Log.d("taxes", "mit Arbeitgeberzushuss: " + taxViewModel.mitag)
                    Log.d("taxes", "ohne Nachweis: " + taxViewModel.nachweis)

                    Log.d("taxes", "Einmal Bezüge: " + taxViewModel.e_sonstb)
                    Log.d("taxes", "schon abgerenchete Einmalbezüge: " + taxViewModel.e_jsonstb)
                    Log.d("taxes", "Bezüge aus mehrjährige Tätigkeit: " + taxViewModel.e_vmt)
                    Log.d("taxes", "davon Entschädigungszahlung: " + taxViewModel.e_entsch)
                    Log.d("taxes", "Freibetrag aus LStKarte: " + taxViewModel.e_wfundf)
                    Log.d("taxes", "Hinzurechnungsbetrag: " + taxViewModel.e_hinzur)
                }) {
            }
        }
    }
}

@Composable
fun SteuerClass(taxViewModel: LegacyTaxModelView) {
    val mContext = LocalContext.current
    var selectedOption by remember { mutableStateOf(1) }
    var valueEhegattenfaktor by remember { mutableStateOf("") }
    var txtValueLohn by remember { mutableStateOf(taxViewModel.e_re4.toString()) }
    var isMonatlich by remember { mutableStateOf(true) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            // .height(percentHeight(.15f))
            .padding(horizontal = percentWidth(.06f)),
        elevation = 5.dp
    ) {
        Column() {
            Row(modifier = Modifier
                .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(text = "Monatlich",
                    modifier = Modifier
                        .clickable(
                            interactionSource = MutableInteractionSource(),
                            indication = null
                        ) { isMonatlich = true
                            taxViewModel.e_lzz = 2.0
                        }
                )
                Switch(
                    modifier = Modifier
                        .padding(horizontal = 20.dp),
                    checked = !isMonatlich,
                    enabled = true,
                    onCheckedChange = {
                        isMonatlich = !it
                        if (isMonatlich) {
                            taxViewModel.e_lzz = 2.0    // 2 stand for month
                        } else {
                            taxViewModel.e_lzz = 1.0    // 1 stand for year
                        }
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.LightGray,
                        uncheckedThumbColor = Color.LightGray,
                        checkedTrackColor = Color.Gray,
                        uncheckedTrackColor = Color.Gray,
                        checkedTrackAlpha = 1.0f,
                        uncheckedTrackAlpha = 1.0f
                    )
                )
                Text(text = "Jährlich",
                    modifier = Modifier
                        .clickable(
                            interactionSource = MutableInteractionSource(),
                            indication = null
                        ) { isMonatlich = false
                            taxViewModel.e_lzz = 1.0
                        }
                )
            }
            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = txtValueLohn,
                onValueChange = {
                    txtValueLohn = it
                    if (!it.isEmpty())
                        taxViewModel.e_re4 = it.toDouble()
                    else
                        taxViewModel.e_re4 = 1.0
                },
                label = { Text(text = "Zusatzbeitrag") },
            )
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
                            if (selectedOption != 2) {
                                selectedOption = 2
                                taxViewModel.e_stkl = selectedOption.toDouble()

                                // case of stclass 2
                                taxViewModel.kinderlos = false
                                if (taxViewModel.selectedOptionKinderZahl == "--") {
                                    Toast.makeText(
                                        mContext,
                                        "Mit der Steuerklasse II muss ein Kinderfreibetrag angegeben werden!",
                                        Toast.LENGTH_LONG).show()
                                    taxViewModel.e_zkf = 0.5
                                    taxViewModel.selectedOptionKinderZahl = "0.5"
                                } else {
                                    taxViewModel.selectedOptionKinderZahl = taxViewModel.e_zkf.toString()
                                }
                            }
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
            Spacer(modifier = Modifier
                .height(10.dp)
                .fillMaxWidth())
            if (selectedOption == 4) {
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
            .padding(horizontal = percentWidth(.06f)),
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
            Row(verticalAlignment = Alignment.CenterVertically
            ) {
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
                                "Kirchensteur: 9%",
                    modifier = Modifier
                        .clickable(interactionSource = MutableInteractionSource(), indication = null)
                        { checkedState = !checkedState }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CheckBoxes(taxViewModel: LegacyTaxModelView) {
    val mContext = LocalContext.current
    val optionsDropMenu = listOf("--", "0.5", "1.0", "1.5", "2.0", "2.5", "3.0", "3.5", "4.0",
        "4.5", "5.0", "5.5", "6.0", "6.5", "7.0", "7.5")
    val textLabel = "Kinderfreibeträge"
    var expanded by remember { mutableStateOf(false) }
    //var selectedOptionText by remember { mutableStateOf(optionsDropMenu[0]) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = percentWidth(.06f)),
        elevation = 5.dp
    ) {
        Column() {
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
                    value = taxViewModel.selectedOptionKinderZahl,
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
                                if (selectionOption == "--" && taxViewModel.e_stkl == 2.0) {
                                    Toast.makeText(
                                        mContext,
                                        "Mit der Steuerklasse II muss ein Kinderfreibetrag angegeben werden!",
                                        Toast.LENGTH_LONG).show()
                                } else {
                                    taxViewModel.selectedOptionKinderZahl = selectionOption
                                    if (optionsDropMenu[0] == selectionOption) {
                                        // set false to checkbox with no children
                                        taxViewModel.e_zkf = 0.0
                                    } else {
                                        taxViewModel.kinderlos = false
                                        taxViewModel.e_zkf =
                                            taxViewModel.selectedOptionKinderZahl.toDouble()  // change children numbers
                                    }
                                    expanded = false
                                }
                            }
                        ) {
                            Text(text = selectionOption)
                        }
                    }
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = taxViewModel.kinderlos,
                    onCheckedChange = {
                        if(taxViewModel.e_stkl != 2.0) {
                            taxViewModel.kinderlos = it
                            if (it)
                                taxViewModel.selectedOptionKinderZahl = optionsDropMenu[0]
                        } else {
                            Toast.makeText(
                                mContext,
                                "Mit der Steuerklasse II muss ein Kinderfreibetrag angegeben werden!",
                                Toast.LENGTH_LONG).show()
                        }
                    },
                    )
                Text(
                    text = "Kinderlos und älter als 23",
                    modifier = Modifier
                        .clickable(interactionSource = MutableInteractionSource(), indication = null)
                        {
                            if (taxViewModel.e_stkl != 2.0) {
                                taxViewModel.kinderlos = !taxViewModel.kinderlos
                                if (taxViewModel.kinderlos) {
                                    taxViewModel.selectedOptionKinderZahl = optionsDropMenu[0]
                                    taxViewModel.e_zkf = 0.0
                                }
                            } else {
                                Toast.makeText(
                                    mContext,
                                    "Mit der Steuerklasse II muss ein Kinderfreibetrag angegeben werden!",
                                    Toast.LENGTH_LONG).show()
                            }
                        })
            }
            Row(verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = taxViewModel.e_krv,
                    onCheckedChange = {
                        taxViewModel.e_krv = it
                    },
                    )
                Text(
                    text = "Renteversicherungspflichtig",
                    modifier = Modifier
                        .clickable(interactionSource = MutableInteractionSource(), indication = null)
                        { taxViewModel.e_krv = !taxViewModel.e_krv })
            }
            Row(verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = taxViewModel.e_av,
                    onCheckedChange = {
                        taxViewModel.e_av = it
                    },
                    )
                Text(
                    text = "Arbeitslosenversicherungspflichtig",
                    modifier = Modifier
                        .clickable(interactionSource = MutableInteractionSource(), indication = null)
                        { taxViewModel.e_av = !taxViewModel.e_av }
                    )
            }
        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Card_KrankVers(taxViewModel: LegacyTaxModelView) {
    var expanded by remember { mutableStateOf(false) }
    var isKrankVersGesetzlich by remember { mutableStateOf(true) }

    val optionsDropMenu = listOf("0.0", "14.0", "14.6")
    var selectedOption by remember { mutableStateOf(optionsDropMenu[0]) }

    var txtValueZusatzbeitrag by remember { mutableStateOf(taxViewModel.e_kvz.toString()) }
    var txtValueBeitrag by remember { mutableStateOf(taxViewModel.e_anpkv.toString()) }
    var txtValueGrundSicherung by remember { mutableStateOf(taxViewModel.e_pkpv.toString()) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = percentWidth(.06f)),
        elevation = 5.dp
    ) {
        Column() {
            Row(modifier = Modifier
                .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = "Krankenversicherung")
            }
            Row(modifier = Modifier
                .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(text = "Gesetzliche",
                    modifier = Modifier
                        .clickable(
                            interactionSource = MutableInteractionSource(),
                            indication = null
                        ) { isKrankVersGesetzlich = true }
                    )
                Switch(
                    modifier = Modifier
                        .padding(horizontal = 20.dp),
                    checked = !isKrankVersGesetzlich,
                    enabled = true,
                    onCheckedChange = {
                        isKrankVersGesetzlich = !it
                        if (isKrankVersGesetzlich) {
                            taxViewModel.e_barmer = 14.6
                            taxViewModel.e_kvz = 1.3
                        } else {
                            taxViewModel.e_barmer = 0.0
                            taxViewModel.e_kvz = 0.0
                        }
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.LightGray,
                        uncheckedThumbColor = Color.LightGray,
                        checkedTrackColor = Color.Gray,
                        uncheckedTrackColor = Color.Gray,
                        checkedTrackAlpha = 1.0f,
                        uncheckedTrackAlpha = 1.0f
                    )
                )
                Text(text = "Private",
                    modifier = Modifier
                        .clickable(
                            interactionSource = MutableInteractionSource(),
                            indication = null
                        ) { isKrankVersGesetzlich = false }
                )
            }
            if (isKrankVersGesetzlich) {
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
                            value = taxViewModel.e_barmer.toString(),
                            onValueChange = {},
                            label = { Text(text = "Krankenversicherung Beitragssatz") },
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
                                        expanded = false
                                        taxViewModel.e_barmer = selectedOption.toDouble()
                                    }
                                ) {
                                    Text(text = selectionOption)
                                }
                            }
                        }
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        //Text(text = "Kirchensteur:")
                        TextField(
                            modifier = Modifier
                                .fillMaxWidth(),
                            value = txtValueZusatzbeitrag,
                            onValueChange = {
                                txtValueZusatzbeitrag = it
                                if (!it.isEmpty())
                                    taxViewModel.e_kvz = it.toDouble()
                                else
                                    taxViewModel.e_kvz = 0.0
                            },
                            label = { Text(text = "Zusatzbeitrag") },
                        )
                    }
                }
            } else {
                TextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = txtValueBeitrag,
                    onValueChange = {
                        txtValueBeitrag = it
                        if (!it.isEmpty())
                            taxViewModel.e_anpkv = it.toDouble()
                        else
                            taxViewModel.e_anpkv = 0.0
                    },
                    label = { Text(text = "Beitrag / Monat") },
                )
                TextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = txtValueGrundSicherung,
                    onValueChange = {
                        txtValueGrundSicherung = it
                        if (!it.isEmpty())
                            taxViewModel.e_pkpv = it.toDouble()
                        else
                            taxViewModel.e_pkpv = 0.0
                    },
                    label = { Text(text = "Grundsicherung / Monat") },
                )
                Row(verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = taxViewModel.mitag,
                        onCheckedChange = {
                            taxViewModel.mitag = it
                        },
                    )
                    Text(
                        text = "mit Arbeitgeberzuscuss",
                        modifier = Modifier
                            .clickable(interactionSource = MutableInteractionSource(), indication = null)
                            { taxViewModel.mitag = !taxViewModel.mitag })
                }
                Row(verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = taxViewModel.nachweis,
                        onCheckedChange = {
                            taxViewModel.nachweis = it
                        },
                    )
                    Text(
                        text = "ohne Nachweis",
                        modifier = Modifier
                            .clickable(interactionSource = MutableInteractionSource(), indication = null)
                            { taxViewModel.nachweis = !taxViewModel.nachweis })
                }
            }
        }
    }
}

@Composable
fun Card_Optional_Top(taxViewModel: LegacyTaxModelView) {
    val focusManager = LocalFocusManager.current
    var txtValueEinmalBezuege by remember{ mutableStateOf("") }
    var txtValueAbgerecnhet by remember{ mutableStateOf("") }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = percentWidth(.06f)),
        elevation = 5.dp
    ) {
        Column() {
            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = txtValueEinmalBezuege,
                onValueChange = {
                    if (it.length <= 15 && it[0] != '.' && it.filter { it == '.' }.count() < 2) {
                        txtValueEinmalBezuege = it.replace("[^0-9.]".toRegex(), "")
                        if (!it.isEmpty())
                            taxViewModel.e_sonstb = txtValueEinmalBezuege.toDouble()
                        else
                            taxViewModel.e_sonstb = 0.0
                    }
                },
                label = { Text(text = "Einmal Bezüge") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                keyboardActions = KeyboardActions(onDone = {
                    focusManager.clearFocus()
                }),
                singleLine = true,
                maxLines = 1,
                trailingIcon = {
                    if (txtValueEinmalBezuege != "")
                        Icon(
                            Icons.Default.Clear,
                            modifier = Modifier
                                .clickable(true){
                                    txtValueEinmalBezuege = ""
                                },
                            contentDescription = "Clear",
                            //tint = MaterialTheme.myColors.main_350,
                        )
                },
            )
            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = txtValueAbgerecnhet,
                onValueChange = {
                    if (it.length <= 15 && it[0] != '.' && it.filter { it == '.' }.count() < 2) {
                        txtValueAbgerecnhet = it.replace("[^0-9.]".toRegex(), "")
                        if (!it.isEmpty())
                            taxViewModel.e_jsonstb = txtValueAbgerecnhet.toDouble()
                        else
                            taxViewModel.e_jsonstb = 0.0
                    }
                },
                label = { Text(text = "schon abgerenchete Einmalbezüge") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                keyboardActions = KeyboardActions(onDone = {
                    focusManager.clearFocus()
                }),
                singleLine = true,
                maxLines = 1,
                trailingIcon = {
                    if (txtValueAbgerecnhet != "")
                        Icon(
                            Icons.Default.Clear,
                            modifier = Modifier
                                .clickable(true){
                                    txtValueAbgerecnhet = ""
                                },
                            contentDescription = "Clear",
                            //tint = MaterialTheme.myColors.main_350,
                        )
                },
            )
        }
    }
}

@Composable
fun Card_Optional_Midle(taxViewModel: LegacyTaxModelView) {
    val focusManager = LocalFocusManager.current
    var txtValueBetugeMehrJahr by remember{ mutableStateOf("") }
    var txtValueEntscheidungZahlung by remember{ mutableStateOf("") }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = percentWidth(.06f)),
        elevation = 5.dp
    ) {
        Column() {
            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = txtValueBetugeMehrJahr,
                onValueChange = {
                    if (it.length <= 15 && it[0] != '.' && it.filter { it == '.' }.count() < 2) {
                        txtValueBetugeMehrJahr = it.replace("[^0-9.]".toRegex(), "")
                        if (!it.isEmpty())
                            taxViewModel.e_vmt = txtValueBetugeMehrJahr.toDouble()
                        else
                            taxViewModel.e_vmt = 0.0
                    }
                },
                label = { Text(text = "Bezüge aus mehrjährige Tätigkeit") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                keyboardActions = KeyboardActions(onDone = {
                    focusManager.clearFocus()
                }),
                singleLine = true,
                maxLines = 1,
                trailingIcon = {
                    if (txtValueBetugeMehrJahr != "")
                        Icon(
                            Icons.Default.Clear,
                            modifier = Modifier
                                .clickable(true){
                                    txtValueBetugeMehrJahr = ""
                                },
                            contentDescription = "Clear",
                            //tint = MaterialTheme.myColors.main_350,
                        )
                },
            )
            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = txtValueEntscheidungZahlung,
                onValueChange = {
                    if (it.length <= 15 && it[0] != '.' && it.filter { it == '.' }.count() < 2) {
                        txtValueEntscheidungZahlung = it.replace("[^0-9.]".toRegex(), "")
                        if (!it.isEmpty())
                            taxViewModel.e_entsch = txtValueEntscheidungZahlung.toDouble()
                        else
                            taxViewModel.e_entsch = 0.0
                    }
                },
                label = { Text(text = "davon Entschädigungszahlung") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                keyboardActions = KeyboardActions(onDone = {
                    focusManager.clearFocus()
                }),
                singleLine = true,
                maxLines = 1,
                trailingIcon = {
                    if (txtValueEntscheidungZahlung != "")
                        Icon(
                            Icons.Default.Clear,
                            modifier = Modifier
                                .clickable(true){
                                    txtValueEntscheidungZahlung = ""
                                },
                            contentDescription = "Clear",
                            //tint = MaterialTheme.myColors.main_350,
                        )
                },
            )
        }
    }
}

@Composable
fun Card_Optional_Bottom(taxViewModel: LegacyTaxModelView) {
    val focusManager = LocalFocusManager.current
    var txtValueLstkarte by remember{ mutableStateOf("") }
    var txtValueHinzurechnugsBetrag by remember{ mutableStateOf("") }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = percentWidth(.06f)),
        elevation = 5.dp
    ) {
        Column() {
            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = txtValueLstkarte,
                onValueChange = {
                    if (it.length <= 15 && it[0] != '.' && it.filter { it == '.' }.count() < 2) {
                        txtValueLstkarte = it.replace("[^0-9.]".toRegex(), "")
                        if (!it.isEmpty())
                            taxViewModel.e_wfundf = txtValueLstkarte.toDouble()
                        else
                            taxViewModel.e_wfundf = 0.0
                    }
                },
                label = { Text(text = "Freibetrag aus LStKarte") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                keyboardActions = KeyboardActions(onDone = {
                    focusManager.clearFocus()
                }),
                singleLine = true,
                maxLines = 1,
                trailingIcon = {
                    if (txtValueLstkarte != "")
                        Icon(
                            Icons.Default.Clear,
                            modifier = Modifier
                                .clickable(true){
                                    txtValueLstkarte = ""
                                },
                            contentDescription = "Clear",
                            //tint = MaterialTheme.myColors.main_350,
                        )
                },
            )
            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = txtValueHinzurechnugsBetrag,
                onValueChange = { it ->
                    if (it.length <= 15 && it[0] != '.' && it.filter { it == '.' }.count() < 2) {
                        txtValueHinzurechnugsBetrag = it.replace("[^0-9.]".toRegex(), "")
                        if (!it.isEmpty())
                            taxViewModel.e_hinzur = txtValueHinzurechnugsBetrag.toDouble()
                        else
                            taxViewModel.e_hinzur = 0.0
                    }
                },
                label = { Text(text = "Hinzurechnungsbetrag") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                keyboardActions = KeyboardActions(onDone = {
                    focusManager.clearFocus()
                }),
                singleLine = true,
                maxLines = 1,
                trailingIcon = {
                    if (txtValueHinzurechnugsBetrag != "")
                        Icon(
                            Icons.Default.Clear,
                            modifier = Modifier
                                .clickable(true){
                                    txtValueHinzurechnugsBetrag = ""
                                },
                            contentDescription = "Clear",
                            //tint = MaterialTheme.myColors.main_350,
                        )
                },
            )
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