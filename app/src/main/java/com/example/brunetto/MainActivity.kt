package com.example.brunetto

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
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
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.brunetto.helpers.*
import com.example.brunetto.ui.theme.BrunettoTheme
import com.example.brunetto.viewModels.LegacyTaxModelView
import com.example.brunetto.viewModels.ReportTaxModelView
import com.example.brunetto.viewModels.TaxViewModel
import java.math.BigDecimal
import java.math.RoundingMode

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
    val reportTaxModel = ReportTaxModelView()
    val mainCalcTax = CalculationLegacy(taxViewModel, reportTaxModel)

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
            Header(taxViewModel, reportTaxModel)
            Body(taxViewModel, mainCalcTax)
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
fun Header(taxViewModel: LegacyTaxModelView, reportTaxModel: ReportTaxModelView) {
    var isReportExtended by remember { mutableStateOf(false) }
    Card(
        elevation = 5.dp,
        modifier = Modifier
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
            .fillMaxWidth()
            //.padding(bottom = getPaddingCards()),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Spacer(
                modifier = Modifier
                    .height(percentHeight(adaptHeight(.035f, .05f, 0.065f)) - 19.dp)
                    .fillMaxWidth()
            )

            ForReportTax("Netto jährlich", reportTaxModel.netSalary)
            ForReportTax("Netto monatlich", reportTaxModel.netSalaryMonthly)
            if (!isReportExtended) {
                Icon(
                    Icons.Default.KeyboardArrowDown,
                    modifier = Modifier
                        .size(35.dp)
                        .clickable(true) {
                            isReportExtended = !isReportExtended
                        },
                    contentDescription = "Clear",
                    //tint = MaterialTheme.myColors.main_350,
                )
            }
            if (isReportExtended) {
                ReportTax(taxViewModel, reportTaxModel)
                Icon(
                    Icons.Default.KeyboardArrowUp,
                    modifier = Modifier
                        .size(40.dp)
                        .clickable(true) {
                            isReportExtended = !isReportExtended
                        },
                    contentDescription = "Clear",
                    //tint = MaterialTheme.myColors.main_350,
                )
            }
        }
    }
}

@Composable
fun ReportTax(taxViewModel: LegacyTaxModelView, reportTaxModel: ReportTaxModelView) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        ForReportTax("Lohnsteuer", reportTaxModel.taxes)

        if (reportTaxModel.taxesByBrutto != 0.0)
            ForReportTax("für Bruttolohn", reportTaxModel.taxesByBrutto)

        if (reportTaxModel.oneTimePay != 0.0)
            ForReportTax("für Einmalzahlung", reportTaxModel.oneTimePay)

        if (reportTaxModel.multiYearEmploy != 0.0)
            ForReportTax("für mehrjährige Tätigkeit", reportTaxModel.multiYearEmploy)

        ForReportTax("Solidaritätszuschlag", reportTaxModel.solidaritat)
        ForReportTax("${taxViewModel.e_krv} Kirchensteuer", reportTaxModel.churchTax)
        ForReportTax("Summe der Steuern", reportTaxModel.sumTax)
        ForReportTax("Krankenversicherung", reportTaxModel.medInsurance)
        ForReportTax("Arbeitslosenversicherung", reportTaxModel.unemployed)
        ForReportTax("Rentenversicherung", reportTaxModel.pension)
        ForReportTax("Pflegeversicherung", reportTaxModel.careInsurance)
        ForReportTax("Summe Sozialversicherung", reportTaxModel.socialSum)

        Text(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth(),
            text = "Arbeitgeberanteil",
            textAlign = TextAlign.Center,
        )
        ForReportTax("Krankenversicherung", reportTaxModel.medInsuranceCompany)
        ForReportTax("Arbeitslosenversicherung", reportTaxModel.unemployedCompany)
        ForReportTax("Rentenversicherung", reportTaxModel.pensionCompany)
        ForReportTax("Pflegeversicherung", reportTaxModel.careInsuranceCompany)
        ForReportTax("Summe Arbeitgeberanteil", reportTaxModel.socialSumCompany)

        ForReportTax("Gesamtbelastung Arbeitgeber", reportTaxModel.totalLoadCompany)
    }
}

@Composable
fun ForReportTax(labelName : String, labelValue : Double) {
    val bd = BigDecimal(labelValue)
    val formattedLabelValue = bd.setScale(2, RoundingMode.FLOOR)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Bottom,
    ) {
        Text(
            modifier = Modifier.width(percentWidth(.49f)),
            text = labelName,
            textAlign = TextAlign.Right,
        )
        Text(
            modifier = Modifier.width(percentWidth(.02f)),
            text = ": ",
            textAlign = TextAlign.Center,
        )
        Text(
            modifier = Modifier.width(120.dp),
            text = "$formattedLabelValue Euro",
            textAlign = TextAlign.Right,
        )
        Text(
            modifier = Modifier.width(percentWidth(.49f) - 120.dp),
            text = "",
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
fun Body(taxViewModel: LegacyTaxModelView, mainCalcTaxLegacy: CalculationLegacy) {
    val spaceBetweenCards = percentHeight(.011f)
    val state = rememberScrollState()
    val calcTaxViewModel = TaxViewModel()
    val mainCalcTax = CalcTax()
    val focusManager = LocalFocusManager.current

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
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
                .fillMaxHeight(1f)
                .verticalScroll(state),
        ) {

            /*OutlinedTextField(
                value = bruttoLohn,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                onValueChange = {
                    bruttoLohn = it
                })*/
            Spacer(modifier = Modifier
                .height(spaceBetweenCards)
                .fillMaxWidth())
            /**
             * The Card for the "Steur class" and when the option 4 is selected => active "Ehegattenfaktor"
             * */
            SteuerClass(taxViewModel, mainCalcTaxLegacy)
            /**
             * The Card with "Bundesland" drop down menu & and checkbox for "kirchsteur"
             * */
            DropDownMenu_Bundesland("Bundesland", taxViewModel, mainCalcTaxLegacy)
            /**
             * The Card for the checkboxes like "kinderlos" and > 23 & "renteversicherung" & "arbeitslosversicherung"
             * */
            CheckBoxes(taxViewModel, mainCalcTaxLegacy)
            /**
             * The Card for "krankversicherungen" , gesetzliche & private
             * */
            Card_KrankVers(taxViewModel, mainCalcTaxLegacy)
            /**
             * The Card for optional bezüge, top one
             * */
            Card_Optional_Top(taxViewModel, mainCalcTaxLegacy)
            /**
             * The Card for optional bezüge, midle one
             * */
            Card_Optional_Midle(taxViewModel, mainCalcTaxLegacy)
            /**
             * The Card for optional bezüge, bottom one
             * */
            Card_Optional_Bottom(taxViewModel, mainCalcTaxLegacy)

            /*Button(
                onClick = {
                    mainCalcTaxLegacy.setData()
                 /*   Log.d("taxes", "Zeitraum: " + taxViewModel.e_lzz)
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
               */
                }) {
            }*/
        }
    }
    mainCalcTaxLegacy.setData()
}

@Composable
fun SteuerClass(taxViewModel: LegacyTaxModelView, mainCalcTaxLegacy: CalculationLegacy) {
    val focusManager = LocalFocusManager.current
    val mContext = LocalContext.current
    var selectedOption by remember { mutableStateOf(1) }
    var valueEhegattenfaktor by remember { mutableStateOf("") }
    var txtValueLohn by remember { mutableStateOf(taxViewModel.e_re4.toString()) }
    var isMonatlich by remember { mutableStateOf(taxViewModel.e_lzz == 2.0) }

    Card(
        elevation = 5.dp,
        modifier = Modifier
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
            .fillMaxWidth()
            // .height(percentHeight(.15f))
            .padding(horizontal = percentWidth(.06f))
            .padding(vertical = getPaddingCards()),
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
                            taxViewModel.isProYear = false
                            mainCalcTaxLegacy.setData()
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
                            taxViewModel.isProYear = false
                        } else {
                            taxViewModel.e_lzz = 1.0    // 1 stand for year
                            taxViewModel.isProYear = true
                        }
                        mainCalcTaxLegacy.setData()
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
                            taxViewModel.isProYear = true
                            mainCalcTaxLegacy.setData()
                        }
                )
            }
            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = txtValueLohn,
                onValueChange = {
                    txtValueLohn = filterUserInput(it, txtValueLohn)
                    taxViewModel.e_re4 = getDoubleValFromInput(txtValueLohn)
                    mainCalcTaxLegacy.setData()
                },
                label = { Text(text = "Bruttolohn") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                keyboardActions = KeyboardActions(onDone = {
                    focusManager.clearFocus()
                }),
                singleLine = true,
                maxLines = 1,
                trailingIcon = {
                    if (txtValueLohn != "")
                        Icon(
                            Icons.Default.Clear,
                            modifier = Modifier
                                .clickable(true){
                                    txtValueLohn = ""
                                    taxViewModel.e_re4 = 0.0
                                    //mainCalcTaxLegacy.setData()   // not change the calc when empty
                                },
                            contentDescription = "Clear",
                            //tint = MaterialTheme.myColors.main_350,
                        )
                },
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
                    color = SetColorSteuerClassByIsSelected(taxViewModel, 1),
                    modifier = Modifier
                        .clickable(true) {
                            selectedOption = 1
                            taxViewModel.e_stkl = selectedOption.toDouble()
                            mainCalcTaxLegacy.setData()
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
                    color = SetColorSteuerClassByIsSelected(taxViewModel, 2),
                    modifier = Modifier
                        .clickable(true) {
                            if (selectedOption != 2) {
                                selectedOption = 2
                                taxViewModel.e_stkl = selectedOption.toDouble()

                                // case of stclass 2
                                taxViewModel.kinderlos = false
                                if (taxViewModel.selectedOptionKinderZahl == "--") {
                                    Toast
                                        .makeText(
                                            mContext,
                                            "Mit der Steuerklasse II muss ein Kinderfreibetrag angegeben werden!",
                                            Toast.LENGTH_LONG
                                        )
                                        .show()
                                    taxViewModel.e_zkf = 0.5
                                    taxViewModel.selectedOptionKinderZahl = "0.5"
                                } else {
                                    taxViewModel.selectedOptionKinderZahl =
                                        taxViewModel.e_zkf.toString()
                                }
                                mainCalcTaxLegacy.setData()
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
                    color = SetColorSteuerClassByIsSelected(taxViewModel, 3),
                    modifier = Modifier
                        .clickable(true) {
                            selectedOption = 3
                            taxViewModel.e_stkl = selectedOption.toDouble()
                            mainCalcTaxLegacy.setData()
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
                    color = SetColorSteuerClassByIsSelected(taxViewModel, 4),
                    modifier = Modifier
                        .clickable(true) {
                            selectedOption = 4
                            taxViewModel.e_stkl = selectedOption.toDouble()
                            mainCalcTaxLegacy.setData()
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
                    color = SetColorSteuerClassByIsSelected(taxViewModel, 5),
                    modifier = Modifier
                        .clickable(true) {
                            selectedOption = 5
                            taxViewModel.e_stkl = selectedOption.toDouble()
                            mainCalcTaxLegacy.setData()
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
                    color = SetColorSteuerClassByIsSelected(taxViewModel, 6),
                    modifier = Modifier
                        .clickable(true) {
                            selectedOption = 6
                            taxViewModel.e_stkl = selectedOption.toDouble()
                            mainCalcTaxLegacy.setData()
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
                        valueEhegattenfaktor = filterUserInput(it, valueEhegattenfaktor)
                        var inputDouble = 1.0
                        if (!valueEhegattenfaktor.isEmpty())
                            inputDouble = filterUserInput(it, valueEhegattenfaktor).toDouble()
                        if (inputDouble in 0.001..1.0) {
                            taxViewModel.e_f = getDoubleValFromInput(valueEhegattenfaktor, true)
                            mainCalcTaxLegacy.setData()
                        } else {
                            taxViewModel.e_f = 1.0
                            Toast.makeText(mContext,
                                "Ehegattenfaktor: \n Nur Werte zwischen 0.001 und 1.0 sind akzeptabel",
                                Toast.LENGTH_LONG).show()
                        }
                    },
                    label = { Text(text = "Ehegattenfaktor") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    keyboardActions = KeyboardActions(onDone = {
                        focusManager.clearFocus()
                    }),
                    singleLine = true,
                    maxLines = 1,
                    trailingIcon = {
                        if (valueEhegattenfaktor != "")
                            Icon(
                                Icons.Default.Clear,
                                modifier = Modifier
                                    .clickable(true){
                                        valueEhegattenfaktor = ""
                                        taxViewModel.e_f = 1.0
                                        mainCalcTaxLegacy.setData()
                                    },
                                contentDescription = "Clear",
                                //tint = MaterialTheme.myColors.main_350,
                            )
                    },
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DropDownMenu_Bundesland(
    textLabel: String,
    taxViewModel: LegacyTaxModelView,
    mainCalcTaxLegacy: CalculationLegacy
) {
    var expanded by remember { mutableStateOf(false) }
    var checkedState by remember { mutableStateOf(true) }

    val optionsDropMenu = listOf("Baden-Württemberg", "Bayern", "Berlin(West)", "Berlin(Ost)", "Brandenburg",
        "Bremen/Bremerhaven", "Hamburg", "Hessen", "Mecklenburg-Vorpommern", "Niedersachsen", "Nordrhein-Westfalen",
        "Rheinland-Pfalz", "Saarland", "Sachsen", "Sachsen-Anhalt", "Schleswig-Holstein", "Thüringen",)
    var selectedOptionLand by remember { mutableStateOf(optionsDropMenu[0]) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = percentWidth(.06f))
            .padding(vertical = getPaddingCards()),
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
                                mainCalcTaxLegacy.setData()
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
                        mainCalcTaxLegacy.setData()
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
                        { checkedState = !checkedState
                            mainCalcTaxLegacy.setData()
                        }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CheckBoxes(taxViewModel: LegacyTaxModelView, mainCalcTaxLegacy: CalculationLegacy) {
    val mContext = LocalContext.current
    val optionsDropMenu = listOf("--", "0.5", "1.0", "1.5", "2.0", "2.5", "3.0", "3.5", "4.0",
        "4.5", "5.0", "5.5", "6.0", "6.5", "7.0", "7.5")
    val textLabel = "Kinderfreibeträge"
    var expanded by remember { mutableStateOf(false) }
    //var selectedOptionText by remember { mutableStateOf(optionsDropMenu[0]) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = percentWidth(.06f))
            .padding(vertical = getPaddingCards()),
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
                                mainCalcTaxLegacy.setData()
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
                            if (it) {
                                taxViewModel.selectedOptionKinderZahl = optionsDropMenu[0]
                                taxViewModel.e_zkf = 0.0
                            }
                        } else {
                            Toast.makeText(
                                mContext,
                                "Mit der Steuerklasse II muss ein Kinderfreibetrag angegeben werden!",
                                Toast.LENGTH_LONG).show()
                        }
                        mainCalcTaxLegacy.setData()
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
                            mainCalcTaxLegacy.setData()
                        })
            }
            Row(verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = taxViewModel.e_krv,
                    onCheckedChange = {
                        taxViewModel.e_krv = it
                        mainCalcTaxLegacy.setData()
                    },
                    )
                Text(
                    text = "Renteversicherungspflichtig",
                    modifier = Modifier
                        .clickable(interactionSource = MutableInteractionSource(), indication = null)
                        { taxViewModel.e_krv = !taxViewModel.e_krv
                            mainCalcTaxLegacy.setData()})
            }
            Row(verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = taxViewModel.e_av,
                    onCheckedChange = {
                        taxViewModel.e_av = it
                        mainCalcTaxLegacy.setData()
                    },
                    )
                Text(
                    text = "Arbeitslosenversicherungspflichtig",
                    modifier = Modifier
                        .clickable(interactionSource = MutableInteractionSource(), indication = null)
                        { taxViewModel.e_av = !taxViewModel.e_av
                            mainCalcTaxLegacy.setData()}
                    )
            }
        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Card_KrankVers(taxViewModel: LegacyTaxModelView, mainCalcTaxLegacy: CalculationLegacy) {
    val focusManager = LocalFocusManager.current
    var expanded by remember { mutableStateOf(false) }
    var isKrankVersGesetzlich by remember { mutableStateOf(true) }

    val optionsDropMenu = listOf("0.0", "14.0", "14.6")
    var selectedOption by remember { mutableStateOf(optionsDropMenu[0]) }

    var txtValueZusatzbeitrag by remember { mutableStateOf(taxViewModel.e_kvz.toString()) }
    var txtValueBeitrag by remember { mutableStateOf(taxViewModel.e_anpkv.toString()) }
    var txtValueGrundSicherung by remember { mutableStateOf(taxViewModel.e_pkpv.toString()) }

    Card(
        modifier = Modifier
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
            .fillMaxWidth()
            .padding(horizontal = percentWidth(.06f))
            .padding(vertical = getPaddingCards()),
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
                        ) { taxViewModel.isPrivatInsur = false
                            mainCalcTaxLegacy.setData()
                            Log.d("taxes", "IsPrivate insurance:  " + taxViewModel.isPrivatInsur)}
                    )
                Switch(
                    modifier = Modifier
                        .padding(horizontal = 20.dp),
                    checked = taxViewModel.isPrivatInsur,
                    enabled = true,
                    onCheckedChange = {
                        taxViewModel.isPrivatInsur = it
                        if (!taxViewModel.isPrivatInsur) {
                            taxViewModel.e_barmer = 14.6
                            taxViewModel.e_kvz = 1.3
                        } else {
                            taxViewModel.e_barmer = 0.0
                            taxViewModel.e_kvz = 0.0
                        }
                        mainCalcTaxLegacy.setData()
                        Log.d("taxes", "IsPrivate insurance:  " + taxViewModel.isPrivatInsur)
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
                        ) { taxViewModel.isPrivatInsur = true
                            mainCalcTaxLegacy.setData()}
                )
            }
            if (!taxViewModel.isPrivatInsur) {
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
                                        mainCalcTaxLegacy.setData()
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
                                txtValueZusatzbeitrag = filterUserInput(it, txtValueZusatzbeitrag)
                                taxViewModel.e_kvz = getDoubleValFromInput(txtValueZusatzbeitrag)
                                mainCalcTaxLegacy.setData()
                            },
                            label = { Text(text = "Zusatzbeitrag") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            keyboardActions = KeyboardActions(onDone = {
                                focusManager.clearFocus()
                            }),
                            singleLine = true,
                            maxLines = 1,
                            trailingIcon = {
                                if (txtValueZusatzbeitrag != "")
                                    Icon(
                                        Icons.Default.Clear,
                                        modifier = Modifier
                                            .clickable(true){
                                                txtValueZusatzbeitrag = ""
                                                taxViewModel.e_kvz = 0.0
                                                mainCalcTaxLegacy.setData()
                                            },
                                        contentDescription = "Clear",
                                        //tint = MaterialTheme.myColors.main_350,
                                    )
                            },
                        )
                    }
                }
            } else {
                TextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = txtValueBeitrag,
                    onValueChange = {
                        txtValueBeitrag = filterUserInput(it, txtValueBeitrag)
                        taxViewModel.e_anpkv = getDoubleValFromInput(txtValueBeitrag)
                        mainCalcTaxLegacy.setData()
                    },
                    label = { Text(text = "Beitrag / Monat") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    keyboardActions = KeyboardActions(onDone = {
                        focusManager.clearFocus()
                    }),
                    singleLine = true,
                    maxLines = 1,
                    trailingIcon = {
                        if (txtValueBeitrag != "")
                            Icon(
                                Icons.Default.Clear,
                                modifier = Modifier
                                    .clickable(true){
                                        txtValueBeitrag = ""
                                        taxViewModel.e_anpkv = 0.0
                                        mainCalcTaxLegacy.setData()
                                    },
                                contentDescription = "Clear",
                                //tint = MaterialTheme.myColors.main_350,
                            )
                    },
                )
                TextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = txtValueGrundSicherung,
                    onValueChange = {
                        txtValueGrundSicherung = filterUserInput(it, txtValueGrundSicherung)
                        taxViewModel.e_pkpv = getDoubleValFromInput(txtValueGrundSicherung)
                        mainCalcTaxLegacy.setData()
                    },
                    label = { Text(text = "Grundsicherung / Monat") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    keyboardActions = KeyboardActions(onDone = {
                        focusManager.clearFocus()
                    }),
                    singleLine = true,
                    maxLines = 1,
                    trailingIcon = {
                        if (txtValueGrundSicherung != "")
                            Icon(
                                Icons.Default.Clear,
                                modifier = Modifier
                                    .clickable(true){
                                        txtValueGrundSicherung = ""
                                        taxViewModel.e_pkpv = 0.0
                                        mainCalcTaxLegacy.setData()
                                    },
                                contentDescription = "Clear",
                                //tint = MaterialTheme.myColors.main_350,
                            )
                    },
                )
                Row(verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = taxViewModel.mitag,
                        onCheckedChange = {
                            taxViewModel.mitag = it
                            mainCalcTaxLegacy.setData()
                        },
                    )
                    Text(
                        text = "mit Arbeitgeberzuscuss",
                        modifier = Modifier
                            .clickable(interactionSource = MutableInteractionSource(), indication = null)
                            { taxViewModel.mitag = !taxViewModel.mitag
                                mainCalcTaxLegacy.setData()})
                }
                Row(verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = taxViewModel.nachweis,
                        onCheckedChange = {
                            taxViewModel.nachweis = it
                            mainCalcTaxLegacy.setData()
                        },
                    )
                    Text(
                        text = "ohne Nachweis",
                        modifier = Modifier
                            .clickable(interactionSource = MutableInteractionSource(), indication = null)
                            { taxViewModel.nachweis = !taxViewModel.nachweis
                                mainCalcTaxLegacy.setData()})
                }
            }
        }
    }
}

@Composable
fun Card_Optional_Top(taxViewModel: LegacyTaxModelView, mainCalcTaxLegacy: CalculationLegacy) {
    val focusManager = LocalFocusManager.current
    var txtValueEinmalBezuege by remember{ mutableStateOf("") }
    var txtValueAbgerecnhet by remember{ mutableStateOf("") }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = percentWidth(.06f))
            .padding(vertical = getPaddingCards()),
        elevation = 5.dp
    ) {
        Column() {
            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = txtValueEinmalBezuege,
                onValueChange = {
                    txtValueEinmalBezuege = filterUserInput(it, txtValueEinmalBezuege)
                    taxViewModel.e_sonstb = getDoubleValFromInput(txtValueEinmalBezuege)
                    mainCalcTaxLegacy.setData()
                },
                label = { Text(text = "Einmal Bezüge") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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
                                    taxViewModel.e_sonstb = 0.0
                                    mainCalcTaxLegacy.setData()
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
                    txtValueAbgerecnhet = filterUserInput(it, txtValueAbgerecnhet)
                    taxViewModel.e_jsonstb = getDoubleValFromInput(txtValueAbgerecnhet)
                    mainCalcTaxLegacy.setData()
                },
                label = { Text(text = "schon abgerenchete Einmalbezüge") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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
                                    taxViewModel.e_jsonstb = 0.0
                                    mainCalcTaxLegacy.setData()
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
fun Card_Optional_Midle(taxViewModel: LegacyTaxModelView, mainCalcTaxLegacy: CalculationLegacy) {
    val focusManager = LocalFocusManager.current
    var txtValueBetugeMehrJahr by remember{ mutableStateOf("") }
    var txtValueEntscheidungZahlung by remember{ mutableStateOf("") }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = percentWidth(.06f))
            .padding(vertical = getPaddingCards()),
        elevation = 5.dp
    ) {
        Column() {
            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = txtValueBetugeMehrJahr,
                onValueChange = {
                    txtValueBetugeMehrJahr = filterUserInput(it, txtValueBetugeMehrJahr)
                    taxViewModel.e_vmt = getDoubleValFromInput(txtValueBetugeMehrJahr)
                    mainCalcTaxLegacy.setData()
                },
                label = { Text(text = "Bezüge aus mehrjährige Tätigkeit") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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
                                    taxViewModel.e_vmt = 0.0
                                    mainCalcTaxLegacy.setData()
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
                    txtValueEntscheidungZahlung = filterUserInput(it, txtValueEntscheidungZahlung)
                    taxViewModel.e_entsch = getDoubleValFromInput(txtValueEntscheidungZahlung)
                    mainCalcTaxLegacy.setData()
                },
                label = { Text(text = "davon Entschädigungszahlung") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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
                                    taxViewModel.e_entsch = 0.0
                                    mainCalcTaxLegacy.setData()
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
fun Card_Optional_Bottom(taxViewModel: LegacyTaxModelView, mainCalcTaxLegacy: CalculationLegacy) {
    val focusManager = LocalFocusManager.current
    var txtValueLstkarte by remember{ mutableStateOf("") }
    var txtValueHinzurechnugsBetrag by remember{ mutableStateOf("") }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = percentWidth(.06f))
            .padding(vertical = getPaddingCards()),
        elevation = 5.dp
    ) {
        Column() {
            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = txtValueLstkarte,
                onValueChange = {
                    txtValueLstkarte = filterUserInput(it, txtValueLstkarte)
                    taxViewModel.e_wfundf = getDoubleValFromInput(txtValueLstkarte)
                    mainCalcTaxLegacy.setData()
                },
                label = { Text(text = "Freibetrag aus LStKarte") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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
                                    taxViewModel.e_wfundf = 0.0
                                    mainCalcTaxLegacy.setData()
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
                    txtValueHinzurechnugsBetrag = filterUserInput(it, txtValueHinzurechnugsBetrag)
                    taxViewModel.e_hinzur = getDoubleValFromInput(txtValueHinzurechnugsBetrag)
                    mainCalcTaxLegacy.setData()
                },
                label = { Text(text = "Hinzurechnungsbetrag") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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
                                    taxViewModel.e_hinzur = 0.0
                                    mainCalcTaxLegacy.setData()
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

/**
 * Set the color as selected by selecting steuer class or vice-versa
 * @param taxViewModel The view model with actual status of selected steuer class
 * @param selectedSteuerClass The selected steuer class by user
 * @return The color for the selected steuer class or color for not selected steuer class
 * */
private fun SetColorSteuerClassByIsSelected(taxViewModel : LegacyTaxModelView, selectedSteuerClass: Int): Color {
    if (taxViewModel.e_stkl == selectedSteuerClass.toDouble())
        return Color.Gray
    else
        return Color.LightGray
}

/**
 * Transform user input to double
 * @param filteredInput The already filtered user input
 * @param isClassFour The true value in case if class 4 is selected
 * @return The double value from user input, when this it a number. Otherwise return 0.0
 * */
private fun getDoubleValFromInput(filteredInput : String, isClassFour : Boolean = false) : Double {
    var asValueWhenEmptyField = 0.0
    if (isClassFour)
        asValueWhenEmptyField = 1.0
    return if (!filteredInput.isEmpty())
        try {
            filteredInput.toDouble()
        } catch(e : NumberFormatException) {
            asValueWhenEmptyField
        }
    else
        asValueWhenEmptyField
}

/**
 * Filter the user input with regex to allow only numbers of type double
 * @param userInput The user input
 * @param existingInput The already filtered existing input or empty string
 * @return The double value of inputted string
 * */
private fun filterUserInput(userInput : String, existingInput : String): String {
    var valueFromInput = ""

    if (userInput.length in 1..15 && userInput[0] != '.' && userInput.count { it == '.' } < 2) {
        valueFromInput = userInput.replace("[^0-9.]".toRegex(), "")
        return valueFromInput
    }
    else if (userInput.isEmpty())
        return ""
    return existingInput
}

/**
 * Set space between cards of main screen
 * @return The constant as part of the screen from percent value
 * */
private fun getPaddingCards(): Dp {
    return percentHeight(.011f)
}