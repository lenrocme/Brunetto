package com.example.brunetto.data.lastInput

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "preferences")
data class LastInput (

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "salaryBrut")
    val salaryBrut: Double,
    @ColumnInfo(name = "timePeriod")
    val timePeriod: Double,
    @ColumnInfo(name = "isProYear")
    val isProYear: Boolean,

    /**
     * Dropdown menus
     * */
    @ColumnInfo(name = "birthday")
    val birthday: Double,
    @ColumnInfo(name = "taxClass")
    val taxClass: Double,
    @ColumnInfo(name = "onClassFour")
    val onClassFour: Double,
    @ColumnInfo(name = "childrenConst")
    val childrenConst: Double,
    @ColumnInfo(name = "statNumb")
    val statNumb: Double,
    @ColumnInfo(name = "churchTax")
    val churchTax: Double,

    /**
     * Checkboxes
     * */
    @ColumnInfo(name = "noChildren")
    val noChildren: Boolean,
    @ColumnInfo(name = "pensionIns")
    val pensionIns: Boolean,
    @ColumnInfo(name = "noJobIns")
    val noJobIns: Boolean,

    /**
     * Health insurance
     * */
    @ColumnInfo(name = "healthIns")
    val healthIns: Double,
    @ColumnInfo(name = "additionalAmount")  // Zusatzbetrag
    val additionalAmount: Double,

    /**
     * Private insurance
     * */
    @ColumnInfo(name = "isPrivateIns")
    val isPrivateIns: Boolean,
    @ColumnInfo(name = "anpkvPayedPremium")
    val anpkvPayedPremium: Double,
    @ColumnInfo(name = "mitag")  // Zusatzbetrag
    val mitag: Double,
    @ColumnInfo(name = "nachweis")
    val nachweis: Boolean,
    @ColumnInfo(name = "pkpvBasicPremium")
    val pkpvBasicPremium: Double,

    /**
     * Optional taxes
     * */
    @ColumnInfo(name = "sonstb")
    val sonstb: Double,
    @ColumnInfo(name = "jsonstb")
    val jsonstb: Double,
    @ColumnInfo(name = "vmt")
    val vmt: Double,
    @ColumnInfo(name = "entsch")
    val entsch: Double,
    @ColumnInfo(name = "wfundf")
    val wfundf: Double,
    @ColumnInfo(name = "hinzur")
    val hinzur: Double,

    /**
     * Selector children count
     * */
    @ColumnInfo(name = "selectedOptionKinderZahl")
    val selectedOptionKinderZahl: String,
)