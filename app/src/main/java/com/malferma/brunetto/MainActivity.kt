package com.malferma.brunetto

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.ViewModelProvider
import com.malferma.brunetto.data.lastInput.LastInput
import com.malferma.brunetto.data.lastInput.LastInputViewModel
import com.malferma.brunetto.helpers.*
import com.malferma.brunetto.ui.UiElem
import com.malferma.brunetto.ui.theme.*
import com.malferma.brunetto.viewModels.LegacyTaxModelView
import com.malferma.brunetto.viewModels.ReportTaxModelView
import com.malferma.brunetto.viewModels.TaxViewModel
import com.malferma.brunetto.viewModels.UiTaxViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode

class MainActivity : ComponentActivity() {
    private lateinit var mainVm: MainViewModel
    private lateinit var mLastInput: LastInputViewModel
    private lateinit var mLegacyTaxModelView: LegacyTaxModelView
    private lateinit var mTaxModelView: TaxViewModel
    private lateinit var mUiTaxViewModel: UiTaxViewModel
    private var theme : String by mutableStateOf("Default")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.mainVm = MainViewModel()
        this.mLastInput = ViewModelProvider(this)[LastInputViewModel::class.java]
        this.mLegacyTaxModelView = LegacyTaxModelView()
        this.mTaxModelView = TaxViewModel()
        this.mLegacyTaxModelView.mOutputTxt = mTaxModelView
        this.mUiTaxViewModel = UiTaxViewModel()

        // load the last stored data
        this.mLastInput.lastInput.observe(this) { lastInput ->
            if (lastInput != null) {
                // for calc taxes and report
                mLegacyTaxModelView.setDataFromTheDbLastInput(lastInput)
                // for textfield
                mTaxModelView.setDataFromTheDbLastInput(lastInput)
            }
            else { // store default data to the db if table is empty (first load)
                this.initDefaultDataLastInput(mLastInput)
            }
        }

        setContent {
            CustomMaterialTheme() {
                MainActivityScreen(this.mainVm, this.mLegacyTaxModelView, this.mUiTaxViewModel)
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)){view, insets ->
            val bottom = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom
            view.updatePadding(bottom = bottom)
            insets
        }
    }

    override fun onPause() {
        super.onPause()
        val lastInput = mLegacyTaxModelView.getTheEntityLastInput()
        mLastInput.update(lastInput)
    }

    private fun initDefaultDataLastInput(modelLastInputs: LastInputViewModel) {
        modelLastInputs.add(LastInput())
    }
}

@Composable
fun MainActivityScreen(
    mainVm: MainViewModel,
    mLegacyTaxModelView: LegacyTaxModelView,
    mUiTaxViewModel: UiTaxViewModel
) {
    val focusManager = LocalFocusManager.current
    val reportTaxModel = ReportTaxModelView()
    val mainCalcTax = CalculationLegacy(mLegacyTaxModelView, reportTaxModel)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                    mUiTaxViewModel.isTaxReportExtended = false
                    print(mUiTaxViewModel.isTaxReportExtended)
                })
            }
    ) {
        Column() {
            Header(mLegacyTaxModelView, reportTaxModel, mUiTaxViewModel)
            Body(mainVm, mLegacyTaxModelView, mainCalcTax)
        }
    }
}
/*
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val mUiTaxViewModel = UiTaxViewModel()
    CustomMaterialTheme {
        MainActivityScreen(mainVm, LegacyTaxModelView(), mUiTaxViewModel)
    }
}*/

@Composable
fun Header(
    taxViewModel: LegacyTaxModelView,
    reportTaxModel: ReportTaxModelView,
    mUiTaxViewModel: UiTaxViewModel
) {
    val focusManager = LocalFocusManager.current
    var isReportExtended by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .wrapContentSize()
            .background(color = MaterialTheme.myColors.main_450)

    ) {
        Card(
            modifier = Modifier
                .shadow(elevation = 10.dp, shape = RectangleShape)
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
                .fillMaxWidth()
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        focusManager.clearFocus()
                    })
                }
                .clickable(interactionSource = MutableInteractionSource(), indication = null) {
                    mUiTaxViewModel.isTaxReportExtended = !mUiTaxViewModel.isTaxReportExtended
                }
            //.padding(bottom = getPaddingCards()),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.myColors.main_450)
            ) {
                Spacer( // spacer for translucent action bar
                    modifier = Modifier
                        .height(35.dp)
                        .fillMaxWidth()
                )

                Spacer(
                    modifier = Modifier
                        .height(percentHeight(adaptHeight(.035f, .045f, 0.06f)) - 19.dp)
                        .fillMaxWidth()
                )

                HeaderForReportTax("Netto j??hrlich", reportTaxModel.netSalary)
                HeaderForReportTax("Netto monatlich", reportTaxModel.netSalaryMonthly)
                if (!mUiTaxViewModel.isTaxReportExtended) {
                    Icon(
                        Icons.Default.KeyboardArrowDown,
                        modifier = Modifier
                            .size(40.dp)
                            .clickable(
                                interactionSource = MutableInteractionSource(),
                                indication = null
                            ) {
                                mUiTaxViewModel.isTaxReportExtended =
                                    !mUiTaxViewModel.isTaxReportExtended
                            },
                        contentDescription = "Clear",
                        tint = MaterialTheme.myColors.iconButton,
                    )
                }
                if (mUiTaxViewModel.isTaxReportExtended) {
                    ReportTax(taxViewModel, reportTaxModel)
                    Icon(
                        Icons.Default.KeyboardArrowUp,
                        modifier = Modifier
                            .size(40.dp)
                            .clickable(
                                interactionSource = MutableInteractionSource(),
                                indication = null
                            ) {
                                mUiTaxViewModel.isTaxReportExtended =
                                    !mUiTaxViewModel.isTaxReportExtended
                            },
                        contentDescription = "Clear",
                        tint = MaterialTheme.myColors.iconButton,
                    )
                }
            }
        }
    }
}

@Composable
fun ReportTax(taxViewModel: LegacyTaxModelView, reportTaxModel: ReportTaxModelView) {
    val state = rememberScrollState()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .verticalScroll(state)
            .fillMaxWidth(),
    ) {
        Spacer(
            modifier = Modifier
                .weight(.5f)
        )
        LabelOfReportTaxByType("Steuer")
        ForReportTax("Lohnsteuer", reportTaxModel.taxes)

        if (taxViewModel.e_sonstb != 0.0)
            ForReportTax("f??r Bruttolohn", reportTaxModel.taxesByBrutto)

        if (reportTaxModel.oneTimePay != 0.0)
            ForReportTax("f??r Einmalzahlung", reportTaxModel.oneTimePay)

        if (reportTaxModel.multiYearEmploy != 0.0)
            ForReportTax("f??r mehrj??hrige T??tigkeit", reportTaxModel.multiYearEmploy)

        ForReportTax("Solidarit??tszuschlag", reportTaxModel.solidaritat)
        ForReportTax("Kirchensteuer", reportTaxModel.churchTax)
        ForReportTax("Summe der Steuern", reportTaxModel.sumTax, true)

        LabelOfReportTaxByType("Versicherung")
        ForReportTax("Krankenversicherung", reportTaxModel.medInsurance)
        ForReportTax("Arbeitslosenversicherung", reportTaxModel.unemployed)
        ForReportTax("Rentenversicherung", reportTaxModel.pension)
        ForReportTax("Pflegeversicherung", reportTaxModel.careInsurance)
        ForReportTax("Summe Sozialversicherung", reportTaxModel.socialSum, true)

        LabelOfReportTaxByType("Arbeitgeberanteil")
        ForReportTax("Krankenversicherung", reportTaxModel.medInsuranceCompany)
        ForReportTax("Arbeitslosenversicherung", reportTaxModel.unemployedCompany)
        ForReportTax("Rentenversicherung", reportTaxModel.pensionCompany)
        ForReportTax("Pflegeversicherung", reportTaxModel.careInsuranceCompany)
        ForReportTax("Summe Arbeitgeberanteil", reportTaxModel.socialSumCompany)

        ForReportTax("Gesamtbelastung Arbeitgeber", reportTaxModel.totalLoadCompany, true)
        Spacer(
            modifier = Modifier
                .weight(.5f)
        )
    }
}

@Composable
fun HeaderForReportTax(labelName : String, labelValue : Double, isSummary: Boolean = false) {
    val bd = BigDecimal(labelValue)
    val formattedLabelValue = bd.setScale(2, RoundingMode.FLOOR)
    Column() {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.Transparent),
            //.padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom,
        ) {
            Text(
                modifier = Modifier.width(percentWidth(.51f)),
                text = "$labelName: ",
                textAlign = TextAlign.Right,
                color = MaterialTheme.myColors.fontHeader,
                style = MaterialTheme.typography.h2,
            )

            Text(
                modifier = Modifier.width(150.dp),
                text = "$formattedLabelValue Euro",
                textAlign = TextAlign.Right,
                color = MaterialTheme.myColors.fontHeader,
                style = MaterialTheme.typography.h2,
            )
            Spacer(
                modifier = Modifier
                    .width(percentWidth(.49f) - 150.dp)
            )
        }
        Spacer(
            modifier = Modifier
                .height(2.dp)
                .fillMaxWidth()
        )
    }
}

@Composable
fun LabelOfReportTaxByType(lbText: String) {
    Text(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth(),
        text = lbText,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.h4,
        color = MaterialTheme.myColors.fontLabelHeadTax,
        textDecoration = TextDecoration.Underline,
    )
}

@Composable
fun ForReportTax(labelName : String, labelValue : Double, isSummary: Boolean = false) {
    val bd = BigDecimal(labelValue)
    val formattedLabelValue = bd.setScale(2, RoundingMode.FLOOR)
    Column() {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = if (isSummary) MaterialTheme.myColors.bg_SumTax
                    else Color.Transparent
                ),
            //.padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom,
        ) {
            Text(
                modifier = Modifier.width(percentWidth(.6f)),
                text = labelName,
                textAlign = TextAlign.Right,
                style = MaterialTheme.typography.h5,
            )
            Text(
                modifier = Modifier.width(percentWidth(.02f)),
                text = ": ",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h5,
            )
            Text(
                modifier = Modifier.width(120.dp),
                text = "$formattedLabelValue ???",
                textAlign = TextAlign.Right,
                style = MaterialTheme.typography.h5,
            )
            Text(
                modifier = Modifier.width(percentWidth(.38f) - 120.dp),
                text = "",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h5,
            )
        }
        Spacer(
            modifier = Modifier
                .height(2.dp)
                .fillMaxWidth()
        )
    }
}

@Composable
fun Body(
    mainVm: MainViewModel,
    taxViewModel: LegacyTaxModelView,
    mainCalcTaxLegacy: CalculationLegacy
) {
    val spaceBetweenCards = percentHeight(.011f)
    val state = rememberScrollState()
    val calcTaxViewModel = TaxViewModel()
    val focusManager = LocalFocusManager.current

    /**
     *  Option for dropdown menu
     * */
    val optionsKinder = listOf("--", "0.5", "1.0", "1.5", "2.0", "2.5", "3.0", "3.5", "4.0",
        "4.5", "5.0", "5.5", "6.0", "6.5", "7.0", "7.5")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.myColors.CL_BackGround)
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            },
        contentAlignment = Alignment.Center,
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                /* .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )*/
                .fillMaxHeight(1f)
                .verticalScroll(state)
               /* .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        focusManager.clearFocus()
                    })
                },*/
        ) {
            Spacer(modifier = Modifier
                .height(percentWidth(.06f) - 5.dp)
                .fillMaxWidth())
            if (mainVm.pickedYear == "2022") {
                Column() {
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
                     * The Card for optional bez??ge, top one
                     * */
                    Card_Optional_Top(taxViewModel, mainCalcTaxLegacy)
                    /**
                     * The Card for optional bez??ge, midle one
                     * */
                    Card_Optional_Midle(taxViewModel, mainCalcTaxLegacy)
                    /**
                     * The Card for optional bez??ge, bottom one
                     * */
                    Card_Optional_Bottom(taxViewModel, mainCalcTaxLegacy)
                }
            }
            else {
                Box(modifier = Modifier.fillMaxSize()) {
                    Text(text = "in progress...")
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            YearPicker(mainVm)
            Spacer(modifier = Modifier.height(adaptHeight(10.dp, 20.dp, 30.dp)))
        }
    }
    mainCalcTaxLegacy.setData()
}

@Composable
fun YearPicker(mainVm: MainViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(text = "2022",
            modifier = Modifier
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null
                ) {
                    mainVm.pickedYear = "2022"
                    //mainCalcTaxLegacy.setData()
                },
            style = if (mainVm.pickedYear == "2022")
                MaterialTheme.typography.CheckedYear
            else
                MaterialTheme.typography.UnCheckedYear,
        )
        Spacer(modifier = Modifier.width(20.dp))
        Text(text = "/",
            modifier = Modifier
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null
                ) {
                    mainVm.pickedYear = "2023"
                    //mainCalcTaxLegacy.setData()
                },
            style = MaterialTheme.typography.CheckedYear
        )
        Spacer(modifier = Modifier.width(20.dp))
        Text(text = "2023",
            modifier = Modifier
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null
                ) {
                    mainVm.pickedYear = "2023"
                    //mainCalcTaxLegacy.setData()
                },
            style = if (mainVm.pickedYear == "2023")
                MaterialTheme.typography.CheckedYear
            else
                MaterialTheme.typography.UnCheckedYear,
        )
    }
}

@Composable
fun SteuerClass(taxViewModel: LegacyTaxModelView, mainCalcTaxLegacy: CalculationLegacy) {
    val focusManager = LocalFocusManager.current
    val mContext = LocalContext.current
    var selectedOption by remember { mutableStateOf(1) }
    var valueEhegattenfaktor by remember { mutableStateOf("") }

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
        Column(
            modifier = Modifier
                .background(color = MaterialTheme.myColors.bg_card),
        ) {
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
                        ) {
                            taxViewModel.e_lzz = 2.0
                            taxViewModel.isProYear = false
                            mainCalcTaxLegacy.setData()
                        },
                    textDecoration = if (!taxViewModel.isProYear)
                                        TextDecoration.Underline
                                    else
                                        TextDecoration.None,
                    style = MaterialTheme.typography.SwitcherChoice,
                )
                Switch(
                    modifier = Modifier
                        .padding(start = 15.dp, end = 25.dp),
                    checked = taxViewModel.isProYear,
                    enabled = true,
                    onCheckedChange = {
                        taxViewModel.isProYear = it
                        if (!taxViewModel.isProYear)
                            taxViewModel.e_lzz = 2.0    // 2 stand for month
                        else
                            taxViewModel.e_lzz = 1.0    // 1 stand for year

                        mainCalcTaxLegacy.setData()
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MaterialTheme.myColors.switchThumb,
                        uncheckedThumbColor = MaterialTheme.myColors.switchThumb,
                        checkedTrackColor = MaterialTheme.myColors.switchTrack,
                        uncheckedTrackColor = MaterialTheme.myColors.switchTrack,
                        checkedTrackAlpha = 1.0f,
                        uncheckedTrackAlpha = 1.0f
                    )
                )
                Text(text = "J??hrlich",
                    modifier = Modifier
                        .clickable(
                            interactionSource = MutableInteractionSource(),
                            indication = null
                        ) {
                            taxViewModel.e_lzz = 1.0
                            taxViewModel.isProYear = true
                            mainCalcTaxLegacy.setData()
                        },
                    textDecoration = if (taxViewModel.isProYear)
                                        TextDecoration.Underline
                                    else
                                        TextDecoration.None,
                    style = MaterialTheme.typography.SwitcherChoice,
                )
            }
            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = taxViewModel.mOutputTxt.brutSalary,
                onValueChange = {
                    taxViewModel.mOutputTxt.brutSalary = filterUserInput(it, taxViewModel.mOutputTxt.brutSalary)
                    taxViewModel.e_re4 = getDoubleValFromInput(taxViewModel.mOutputTxt.brutSalary)
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
                    if (taxViewModel.mOutputTxt.brutSalary != "")
                        Icon(
                            Icons.Default.Clear,
                            modifier = Modifier
                                .clickable(true){
                                    taxViewModel.mOutputTxt.brutSalary = ""
                                    taxViewModel.e_re4 = 0.0
                                    //mainCalcTaxLegacy.setData()   // not change the calc when empty
                                },
                            contentDescription = "Clear",
                            //tint = MaterialTheme.myColors.main_350,
                        )
                },
                colors = UiElem.colorsOfTextField()
            )
            Spacer(modifier = Modifier
                .height(10.dp)
                .fillMaxWidth())
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
            ){
                Surface(
                    elevation = 5.dp,
                    shape = CircleShape,
                    color = setColorSteuerClassByIsSelected(taxViewModel, 1),
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
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.myColors.fontTaxButton)
                }
                Surface(
                    elevation = 5.dp,
                    shape = CircleShape,
                    color = setColorSteuerClassByIsSelected(taxViewModel, 2),
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
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.myColors.fontTaxButton)
                }
                Surface(
                    elevation = 5.dp,
                    shape = CircleShape,
                    color = setColorSteuerClassByIsSelected(taxViewModel, 3),
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
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.myColors.fontTaxButton)
                }
                Surface(
                    elevation = 5.dp,
                    shape = CircleShape,
                    color = setColorSteuerClassByIsSelected(taxViewModel, 4),
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
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.myColors.fontTaxButton)
                }
                Surface(
                    elevation = 5.dp,
                    shape = CircleShape,
                    color = setColorSteuerClassByIsSelected(taxViewModel, 5),
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
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.myColors.fontTaxButton)
                }
                Surface(
                    elevation = 5.dp,
                    shape = CircleShape,
                    color = setColorSteuerClassByIsSelected(taxViewModel, 6),
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
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.myColors.fontTaxButton)
                }
            }
            Spacer(modifier = Modifier
                .height(10.dp)
                .fillMaxWidth())
            if (taxViewModel.e_stkl == 4.0) {
                TextField(  //OutlinedTextField
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = if (taxViewModel.e_f != 1.0)
                            taxViewModel.e_f.toString()
                        else
                            valueEhegattenfaktor,
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
                            )
                    },
                    colors = UiElem.colorsOfTextField()
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

    val optionsDropMenu = listOf("Baden-W??rttemberg", "Bayern", "Berlin(West)", "Berlin(Ost)", "Brandenburg",
        "Bremen/Bremerhaven", "Hamburg", "Hessen", "Mecklenburg-Vorpommern", "Niedersachsen", "Nordrhein-Westfalen",
        "Rheinland-Pfalz", "Saarland", "Sachsen", "Sachsen-Anhalt", "Schleswig-Holstein", "Th??ringen",)
    var selectedOptionLand by remember { mutableStateOf(optionsDropMenu[0]) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = percentWidth(.06f))
            .padding(vertical = getPaddingCards()),
        elevation = 5.dp
    ) {
        Column(modifier = Modifier
            .background(color = MaterialTheme.myColors.bg_card),)
        {
            //Text(text = textLabel)
            ExposedDropdownMenuBox(
                modifier = Modifier
                    .background(color = MaterialTheme.myColors.main_300),
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
                    value = optionsDropMenu[taxViewModel.e_bundesland - 1],
                    onValueChange = {},
                    label = { Text(text = textLabel) },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = expanded
                        )
                    },
                    colors = UiElem.colorsOfDropDown(),
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
                    checked = taxViewModel.e_r != 0.0,
                    onCheckedChange = {
                        checkedState = it
                        taxViewModel.e_r = SetKirchSteur(checkedState, selectedOptionLand)
                        mainCalcTaxLegacy.setData()
                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor = MaterialTheme.myColors.checkedCheckbox,
                        uncheckedColor = MaterialTheme.myColors.unCheckedCheckbox
                    )
                )
                //Text(text = "Kirchensteur:")
                Text(
                    text = if (taxViewModel.e_r == 0.0)
                            "Kirchensteur: 0%"
                        else
                            if (optionsDropMenu[taxViewModel.e_bundesland - 1] == "Bayern" || optionsDropMenu[taxViewModel.e_bundesland - 1] == "Baden-W??rttemberg")
                                "Kirchensteur: 8%"
                            else
                                "Kirchensteur: 9%",
                    modifier = Modifier
                        .clickable(interactionSource = MutableInteractionSource(), indication = null)
                        { checkedState = !checkedState
                            taxViewModel.e_r = SetKirchSteur(checkedState, selectedOptionLand)
                            mainCalcTaxLegacy.setData()
                        },
                    style = MaterialTheme.typography.Checkbox,
                    color = if (taxViewModel.e_r == 0.0)
                                    MaterialTheme.myColors.fontUnCheckedCheckbox
                            else
                                    MaterialTheme.myColors.fontCheckedCheckbox
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
    val textLabel = "Kinderfreibetr??ge"
    var expanded by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = percentWidth(.06f))
            .padding(vertical = getPaddingCards()),
        elevation = 5.dp
    ) {
        Column(modifier = Modifier
            .background(color = MaterialTheme.myColors.bg_card),) {
            //Text(text = textLabel)
            ExposedDropdownMenuBox(
                modifier = Modifier
                    .background(color = MaterialTheme.myColors.main_300),
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
                    colors = UiElem.colorsOfDropDown(),
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
                    colors = CheckboxDefaults.colors(
                        checkedColor = MaterialTheme.myColors.checkedCheckbox,
                        uncheckedColor = MaterialTheme.myColors.unCheckedCheckbox
                    )
                )
                Text(
                    text = "Kinderlos und ??lter als 23",
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
                        },
                    style = MaterialTheme.typography.Checkbox,
                    color = if (!taxViewModel.kinderlos)
                        MaterialTheme.myColors.fontUnCheckedCheckbox
                    else
                        MaterialTheme.myColors.fontCheckedCheckbox
                    )
            }
            Row(verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = taxViewModel.e_krv,
                    onCheckedChange = {
                        taxViewModel.e_krv = it
                        mainCalcTaxLegacy.setData()
                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor = MaterialTheme.myColors.checkedCheckbox,
                        uncheckedColor = MaterialTheme.myColors.unCheckedCheckbox
                    )
                )
                Text(
                    text = "Renteversicherungspflichtig",
                    modifier = Modifier
                        .clickable(interactionSource = MutableInteractionSource(), indication = null)
                        { taxViewModel.e_krv = !taxViewModel.e_krv
                            mainCalcTaxLegacy.setData()},
                    style = MaterialTheme.typography.Checkbox,
                    color = if (!taxViewModel.e_krv)
                        MaterialTheme.myColors.fontUnCheckedCheckbox
                    else
                        MaterialTheme.myColors.fontCheckedCheckbox
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = taxViewModel.e_av,
                    onCheckedChange = {
                        taxViewModel.e_av = it
                        mainCalcTaxLegacy.setData()
                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor = MaterialTheme.myColors.checkedCheckbox,
                        uncheckedColor = MaterialTheme.myColors.unCheckedCheckbox
                    )
                )
                Text(
                    text = "Arbeitslosenversicherungspflichtig",
                    modifier = Modifier
                        .clickable(interactionSource = MutableInteractionSource(), indication = null)
                        { taxViewModel.e_av = !taxViewModel.e_av
                            mainCalcTaxLegacy.setData()},
                    style = MaterialTheme.typography.Checkbox,
                    color = if (!taxViewModel.e_av)
                        MaterialTheme.myColors.fontUnCheckedCheckbox
                    else
                        MaterialTheme.myColors.fontCheckedCheckbox
                    )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun Card_KrankVers(taxViewModel: LegacyTaxModelView, mainCalcTaxLegacy: CalculationLegacy) {
    val focusManager = LocalFocusManager.current
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val coroutineScope = rememberCoroutineScope()
    var expanded by remember { mutableStateOf(false) }
    val optionsDropMenu = listOf("0.0", "14.0", "14.6")
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
        Column(modifier = Modifier
            .background(color = MaterialTheme.myColors.bg_card),) {
            Row(modifier = Modifier
                .padding(top = 5.dp)
                .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = "Krankenversicherung",
                    color = MaterialTheme.myColors.fontLabelCard,
                    fontSize = 14.sp)
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
                        },
                    textDecoration = if (!taxViewModel.isPrivatInsur)
                                        TextDecoration.Underline
                                    else
                                        TextDecoration.None,
                    style = MaterialTheme.typography.SwitcherChoice,
                    )
                Switch(
                    modifier = Modifier
                        .padding(start = 15.dp, end = 25.dp),
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
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MaterialTheme.myColors.switchThumb,
                        uncheckedThumbColor = MaterialTheme.myColors.switchThumb,
                        checkedTrackColor = MaterialTheme.myColors.switchTrack,
                        uncheckedTrackColor = MaterialTheme.myColors.switchTrack,
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
                            mainCalcTaxLegacy.setData()},
                    textDecoration = if (taxViewModel.isPrivatInsur)
                                        TextDecoration.Underline
                                    else
                                        TextDecoration.None,
                    style = MaterialTheme.typography.SwitcherChoice,
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
                            colors = UiElem.colorsOfDropDown(),
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
                                        taxViewModel.e_barmer = selectionOption.toDouble()
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
                                .fillMaxWidth()
                                .bringIntoViewRequester(bringIntoViewRequester)
                                .onFocusChanged {
                                    if (it.isFocused) {
                                        coroutineScope.launch {
                                            delay(200)
                                            bringIntoViewRequester.bringIntoView()
                                        }
                                    }
                                }
                            ,
                            value = taxViewModel.mOutputTxt.additionalAmount,
                            onValueChange = {
                                taxViewModel.mOutputTxt.additionalAmount = filterUserInput(it, taxViewModel.mOutputTxt.additionalAmount)
                                taxViewModel.e_kvz = getDoubleValFromInput(taxViewModel.mOutputTxt.additionalAmount)
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
                                if (taxViewModel.mOutputTxt.additionalAmount != "")
                                    Icon(
                                        Icons.Default.Clear,
                                        modifier = Modifier
                                            .clickable(true){
                                                taxViewModel.mOutputTxt.additionalAmount = ""
                                                taxViewModel.e_kvz = 0.0
                                                mainCalcTaxLegacy.setData()
                                            },
                                        contentDescription = "Clear",
                                        //tint = MaterialTheme.myColors.main_350,
                                    )
                            },
                            colors = UiElem.colorsOfTextField()
                        )
                    }
                }
            } else {
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .bringIntoViewRequester(bringIntoViewRequester)
                        .onFocusChanged {
                            if (it.isFocused) {
                                coroutineScope.launch {
                                    delay(200)
                                    bringIntoViewRequester.bringIntoView()
                                }
                            }
                        }
                    ,                    value = taxViewModel.mOutputTxt.anpkvPayedPremium,
                    onValueChange = {
                        taxViewModel.mOutputTxt.anpkvPayedPremium = filterUserInput(it, taxViewModel.mOutputTxt.anpkvPayedPremium)
                        taxViewModel.e_anpkv = getDoubleValFromInput(taxViewModel.mOutputTxt.anpkvPayedPremium)
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
                        if (taxViewModel.mOutputTxt.anpkvPayedPremium != "")
                            Icon(
                                Icons.Default.Clear,
                                modifier = Modifier
                                    .clickable(true){
                                        taxViewModel.mOutputTxt.anpkvPayedPremium = ""
                                        taxViewModel.e_anpkv = 0.0
                                        mainCalcTaxLegacy.setData()
                                    },
                                contentDescription = "Clear",
                                //tint = MaterialTheme.myColors.main_350,
                            )
                    },
                    colors = UiElem.colorsOfTextField()
                )
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .bringIntoViewRequester(bringIntoViewRequester)
                        .onFocusChanged {
                            if (it.isFocused) {
                                coroutineScope.launch {
                                    delay(200)
                                    bringIntoViewRequester.bringIntoView()
                                }
                            }
                        }
                    ,                    value = taxViewModel.mOutputTxt.pkpvBasicPremium,
                    onValueChange = {
                        taxViewModel.mOutputTxt.pkpvBasicPremium = filterUserInput(it, taxViewModel.mOutputTxt.pkpvBasicPremium)
                        taxViewModel.e_pkpv = getDoubleValFromInput(taxViewModel.mOutputTxt.pkpvBasicPremium)
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
                        if (taxViewModel.mOutputTxt.pkpvBasicPremium != "")
                            Icon(
                                Icons.Default.Clear,
                                modifier = Modifier
                                    .clickable(true){
                                        taxViewModel.mOutputTxt.pkpvBasicPremium = ""
                                        taxViewModel.e_pkpv = 0.0
                                        mainCalcTaxLegacy.setData()
                                    },
                                contentDescription = "Clear",
                                //tint = MaterialTheme.myColors.main_350,
                            )
                    },
                    colors = UiElem.colorsOfTextField()
                )
                Row(verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = taxViewModel.mitag,
                        onCheckedChange = {
                            taxViewModel.mitag = it
                            mainCalcTaxLegacy.setData()
                        },
                        colors = CheckboxDefaults.colors(
                            checkedColor = MaterialTheme.myColors.checkedCheckbox,
                            uncheckedColor = MaterialTheme.myColors.unCheckedCheckbox
                        )
                    )
                    Text(
                        text = "mit Arbeitgeberzuscuss",
                        modifier = Modifier
                            .clickable(interactionSource = MutableInteractionSource(), indication = null)
                            { taxViewModel.mitag = !taxViewModel.mitag
                                mainCalcTaxLegacy.setData()},
                        style = MaterialTheme.typography.Checkbox,
                        color = if (!taxViewModel.mitag)
                                    MaterialTheme.myColors.fontUnCheckedCheckbox
                                else
                                    MaterialTheme.myColors.fontCheckedCheckbox
                        )
                }
                Row(verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = taxViewModel.nachweis,
                        onCheckedChange = {
                            taxViewModel.nachweis = it
                            mainCalcTaxLegacy.setData()
                        },
                        colors = CheckboxDefaults.colors(
                            checkedColor = MaterialTheme.myColors.checkedCheckbox,
                            uncheckedColor = MaterialTheme.myColors.unCheckedCheckbox
                        )
                    )
                    Text(
                        text = "ohne Nachweis",
                        modifier = Modifier
                            .clickable(interactionSource = MutableInteractionSource(), indication = null)
                            { taxViewModel.nachweis = !taxViewModel.nachweis
                                mainCalcTaxLegacy.setData()},
                        style = MaterialTheme.typography.Checkbox,
                        color = if (!taxViewModel.nachweis)
                                    MaterialTheme.myColors.fontUnCheckedCheckbox
                                else
                                    MaterialTheme.myColors.fontCheckedCheckbox
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Card_Optional_Top(taxViewModel: LegacyTaxModelView, mainCalcTaxLegacy: CalculationLegacy) {
    val focusManager = LocalFocusManager.current
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val coroutineScope = rememberCoroutineScope()
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
                    .fillMaxWidth()
                    .bringIntoViewRequester(bringIntoViewRequester)
                    .onFocusChanged {
                        if (it.isFocused) {
                            coroutineScope.launch {
                                delay(200)
                                bringIntoViewRequester.bringIntoView()
                            }
                        }
                    }
                ,                value = taxViewModel.mOutputTxt.sonstb,
                onValueChange = {
                    taxViewModel.mOutputTxt.sonstb = filterUserInput(it, taxViewModel.mOutputTxt.sonstb)
                    taxViewModel.e_sonstb = getDoubleValFromInput(taxViewModel.mOutputTxt.sonstb)
                    mainCalcTaxLegacy.setData()
                },
                label = { Text(text = "Einmal Bez??ge") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                keyboardActions = KeyboardActions(onDone = {
                    focusManager.clearFocus()
                }),
                singleLine = true,
                maxLines = 1,
                trailingIcon = {
                    if (taxViewModel.mOutputTxt.sonstb != "")
                        Icon(
                            Icons.Default.Clear,
                            modifier = Modifier
                                .clickable(true){
                                    taxViewModel.mOutputTxt.sonstb = ""
                                    taxViewModel.e_sonstb = 0.0
                                    mainCalcTaxLegacy.setData()
                                },
                            contentDescription = "Clear",
                            //tint = MaterialTheme.myColors.main_350,
                        )
                },
                colors = UiElem.colorsOfTextField()
            )
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .bringIntoViewRequester(bringIntoViewRequester)
                    .onFocusChanged {
                        if (it.isFocused) {
                            coroutineScope.launch {
                                delay(200)
                                bringIntoViewRequester.bringIntoView()
                            }
                        }
                    }
                ,                value = taxViewModel.mOutputTxt.jsonstb,
                onValueChange = {
                    taxViewModel.mOutputTxt.jsonstb = filterUserInput(it, taxViewModel.mOutputTxt.jsonstb)
                    taxViewModel.e_jsonstb = getDoubleValFromInput(taxViewModel.mOutputTxt.jsonstb)
                    mainCalcTaxLegacy.setData()
                },
                label = { Text(text = "schon abgerenchete Einmalbez??ge") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                keyboardActions = KeyboardActions(onDone = {
                    focusManager.clearFocus()
                }),
                singleLine = true,
                maxLines = 1,
                trailingIcon = {
                    if (taxViewModel.mOutputTxt.jsonstb != "")
                        Icon(
                            Icons.Default.Clear,
                            modifier = Modifier
                                .clickable(true){
                                    taxViewModel.mOutputTxt.jsonstb = ""
                                    taxViewModel.e_jsonstb = 0.0
                                    mainCalcTaxLegacy.setData()
                                },
                            contentDescription = "Clear",
                            //tint = MaterialTheme.myColors.main_350,
                        )
                },
                colors = UiElem.colorsOfTextField()
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Card_Optional_Midle(taxViewModel: LegacyTaxModelView, mainCalcTaxLegacy: CalculationLegacy) {
    val focusManager = LocalFocusManager.current
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val coroutineScope = rememberCoroutineScope()
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
                    .fillMaxWidth()
                    .bringIntoViewRequester(bringIntoViewRequester)
                    .onFocusChanged {
                        if (it.isFocused) {
                            coroutineScope.launch {
                                delay(200)
                                bringIntoViewRequester.bringIntoView()
                            }
                        }
                    }
                ,                value = taxViewModel.mOutputTxt.vmt,
                onValueChange = {
                    taxViewModel.mOutputTxt.vmt = filterUserInput(it, taxViewModel.mOutputTxt.vmt)
                    taxViewModel.e_vmt = getDoubleValFromInput(taxViewModel.mOutputTxt.vmt)
                    mainCalcTaxLegacy.setData()
                },
                label = { Text(text = "Bez??ge aus mehrj??hrige T??tigkeit") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                keyboardActions = KeyboardActions(onDone = {
                    focusManager.clearFocus()
                }),
                singleLine = true,
                maxLines = 1,
                trailingIcon = {
                    if (taxViewModel.mOutputTxt.vmt != "")
                        Icon(
                            Icons.Default.Clear,
                            modifier = Modifier
                                .clickable(true){
                                    taxViewModel.mOutputTxt.vmt = ""
                                    taxViewModel.e_vmt = 0.0
                                    mainCalcTaxLegacy.setData()
                                },
                            contentDescription = "Clear",
                            //tint = MaterialTheme.myColors.main_350,
                        )
                },
                colors = UiElem.colorsOfTextField()
            )
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .bringIntoViewRequester(bringIntoViewRequester)
                    .onFocusChanged {
                        if (it.isFocused) {
                            coroutineScope.launch {
                                delay(200)
                                bringIntoViewRequester.bringIntoView()
                            }
                        }
                    }
                ,                value = taxViewModel.mOutputTxt.entsch,
                onValueChange = {
                    taxViewModel.mOutputTxt.entsch = filterUserInput(it, taxViewModel.mOutputTxt.entsch)
                    taxViewModel.e_entsch = getDoubleValFromInput(taxViewModel.mOutputTxt.entsch)
                    mainCalcTaxLegacy.setData()
                },
                label = { Text(text = "davon Entsch??digungszahlung") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                keyboardActions = KeyboardActions(onDone = {
                    focusManager.clearFocus()
                }),
                singleLine = true,
                maxLines = 1,
                trailingIcon = {
                    if (taxViewModel.mOutputTxt.entsch != "")
                        Icon(
                            Icons.Default.Clear,
                            modifier = Modifier
                                .clickable(true){
                                    taxViewModel.mOutputTxt.entsch = ""
                                    taxViewModel.e_entsch = 0.0
                                    mainCalcTaxLegacy.setData()
                                },
                            contentDescription = "Clear",
                            //tint = MaterialTheme.myColors.main_350,
                        )
                },
                colors = UiElem.colorsOfTextField()
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Card_Optional_Bottom(taxViewModel: LegacyTaxModelView, mainCalcTaxLegacy: CalculationLegacy) {
    val focusManager = LocalFocusManager.current
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val coroutineScope = rememberCoroutineScope()

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
                    .fillMaxWidth()
                    .bringIntoViewRequester(bringIntoViewRequester)
                    .onFocusChanged {
                        if (it.isFocused) {
                            coroutineScope.launch {
                                delay(200)
                                bringIntoViewRequester.bringIntoView()
                            }
                        }
                    }
                ,
                value = taxViewModel.mOutputTxt.wfundf,
                onValueChange = {
                    taxViewModel.mOutputTxt.wfundf = filterUserInput(it, taxViewModel.mOutputTxt.wfundf)
                    taxViewModel.e_wfundf = getDoubleValFromInput(taxViewModel.mOutputTxt.wfundf)
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
                    if (taxViewModel.mOutputTxt.wfundf != "")
                        Icon(
                            Icons.Default.Clear,
                            modifier = Modifier
                                .clickable(true){
                                    taxViewModel.mOutputTxt.wfundf = ""
                                    taxViewModel.e_wfundf = 0.0
                                    mainCalcTaxLegacy.setData()
                                },
                            contentDescription = "Clear",
                            //tint = MaterialTheme.myColors.main_350,
                        )
                },
                colors = UiElem.colorsOfTextField()
            )
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .bringIntoViewRequester(bringIntoViewRequester)
                    .onFocusChanged {
                        if (it.isFocused) {
                            coroutineScope.launch {
                                delay(200)
                                bringIntoViewRequester.bringIntoView()
                            }
                        }
                    }
                ,
                value = taxViewModel.mOutputTxt.hinzur,
                onValueChange = { it ->
                    taxViewModel.mOutputTxt.hinzur = filterUserInput(it, taxViewModel.mOutputTxt.hinzur)
                    taxViewModel.e_hinzur = getDoubleValFromInput(taxViewModel.mOutputTxt.hinzur)
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
                    if (taxViewModel.mOutputTxt.hinzur != "")
                        Icon(
                            Icons.Default.Clear,
                            modifier = Modifier
                                .clickable(true){
                                    taxViewModel.mOutputTxt.hinzur = ""
                                    taxViewModel.e_hinzur = 0.0
                                    mainCalcTaxLegacy.setData()
                                },
                            contentDescription = "Clear",
                            //tint = MaterialTheme.myColors.main_350,
                        )
                },
                colors = UiElem.colorsOfTextField()
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
        if (selectedOptionLand == "Bayern" || selectedOptionLand == "Baden-W??rttemberg")
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
@Composable
private fun setColorSteuerClassByIsSelected(taxViewModel : LegacyTaxModelView, selectedSteuerClass: Int): Color {
    return if (taxViewModel.e_stkl == selectedSteuerClass.toDouble())
        MaterialTheme.myColors.bgTaxClassSelect
    else
        MaterialTheme.myColors.bgTaxClass
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