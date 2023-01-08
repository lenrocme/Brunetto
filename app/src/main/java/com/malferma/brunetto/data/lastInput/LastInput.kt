package com.malferma.brunetto.data.lastInput

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lastInput")
data class LastInput (

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id: Int = 2022,

    @ColumnInfo(name = "salaryBrut")
    val salaryBrut: Double = 50000.0,
    @ColumnInfo(name = "timePeriod")
    val timePeriod: Double = 1.0,
    @ColumnInfo(name = "isProYear")
    val isProYear: Boolean = true,

    /**
     * Dropdown menus
     * */
    @ColumnInfo(name = "birthday")
    val birthday: Double = 0.0,
    @ColumnInfo(name = "taxClass")
    val taxClass: Double = 1.0,
    @ColumnInfo(name = "onClassFour")
    val onClassFour: Double = 0.0,
    @ColumnInfo(name = "childrenConst")
    val childrenConst: Double = 0.0,
    @ColumnInfo(name = "statNumb")
    val statNumb: Int = 1,
    @ColumnInfo(name = "churchTax")
    val churchTax: Double = 8.0,

    /**
     * Checkboxes
     * */
    @ColumnInfo(name = "noChildren")
    val noChildren: Boolean = true,
    @ColumnInfo(name = "pensionIns")
    val pensionIns: Boolean = true,
    @ColumnInfo(name = "noJobIns")
    val noJobIns: Boolean = true,

    /**
     * Health insurance
     * */
    @ColumnInfo(name = "healthIns")
    val healthIns: Double = 14.6,
    @ColumnInfo(name = "additionalAmount")  // Zusatzbetrag
    val additionalAmount: Double = 1.3,

    /**
     * Private insurance
     * */
    @ColumnInfo(name = "isPrivateIns")
    val isPrivateIns: Boolean = false,
    @ColumnInfo(name = "anpkvPayedPremium")
    val anpkvPayedPremium: Double = 300.0,
    @ColumnInfo(name = "mitag")  // Zusatzbetrag
    val mitag: Boolean = true,
    @ColumnInfo(name = "nachweis")
    val nachweis: Boolean = false,
    @ColumnInfo(name = "pkpvBasicPremium")
    val pkpvBasicPremium: Double = 500.0,

    /**
     * Optional taxes
     * */
    @ColumnInfo(name = "sonstb")
    val sonstb: Double = 0.0,
    @ColumnInfo(name = "jsonstb")
    val jsonstb: Double = 0.0,
    @ColumnInfo(name = "vmt")
    val vmt: Double = 0.0,
    @ColumnInfo(name = "entsch")
    val entsch: Double = 0.0,
    @ColumnInfo(name = "wfundf")
    val wfundf: Double = 0.0,
    @ColumnInfo(name = "hinzur")
    val hinzur: Double = 0.0,

    /**
     * Selector children count
     * */
    @ColumnInfo(name = "selectedOptionKinderZahl")
    val selectedOptionKinderZahl: String = "--",
)