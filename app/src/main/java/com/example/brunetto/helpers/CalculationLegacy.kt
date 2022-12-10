package com.example.brunetto.helpers

import android.util.Log
import com.example.brunetto.viewModels.LegacyTaxModelView
import com.example.brunetto.viewModels.ReportTaxModelView

class CalculationLegacy(val vmLegacTax: LegacyTaxModelView, val reportTaxModel: ReportTaxModelView) {

    //var e_kvz = 1.3     // 1.3% Zusatzbeitrag
   // var e_re4 = 90000
    var e_sonstb = 0
    var e_jsonstb = 0
    var e_vmt = 0
    var e_lzz = 1
    var e_entsch = 0
    var e_wfundf = 0
    var e_hinzur = 0
    var e_pkpv = 0
    var e_anpkv = 0


    //######################
    var ajahr = 0.0
    var alte = 0.0
    var anp = 0.0
    var anteil1 = 0.0               // was anteil1
    var bmg = 0.0
    var bbgkvpv = 0.0
    var bbgrv = 0.0
    var diff = 0.0
    var efa = 0.0
    var fvb = 0.0
    var fvbso = 0.0
    var fvbz = 0.0
    var fvbzso = 0.0
    var gfb = 0.0;
    var hbalte = 0;
    var hfvb = 0.0;
    var hfvbz = 0.0;
    var hfvbzso = 0.0
    var hoch = 0.0;
    var j = 0.0;
    var jbmg = 0.0           // was jmbg
    var jlfreib = 0.0;
    var jlhinzu = 0.0;
    var jw = 0.0;
    var k = 0.0;
    var kennvmt = 0.0;
    var kfb = 0.0;
    var kvsatzag = 0.0;
    var kvsatzan = 0.0;
    var kvz = 0.0;
    var kztab = 0.0;
    var lstjahr = 0.0;
    var lst1 = 0.0;
    var lst2 = 0.0;
    var lst3 = 0.0;
    var lstoso = 0.0;
    var lstso = 0.0;
    var mist = 0.0;
    var pvsatzag = 0.0;
    var pvsatzan = 0.0;
    var rw = 0.0
    var sap = 0.0;
    var solzsbmg = 0.0;
    var solzszve = 0.0;
    var solzfrei = 0.0;
    var solzj = 0.0;
    var solzmin = 0.0;
    var st = 0.0;
    var st1 = 0.0;
    var st2 = 0.0;
    var stovmt = 0.0;
// definition tab1 usw. s. unten
    var vbezb = 0.0;
    var vbezbso = 0.0;
    var vergl = 0.0;
    var vhb = 0.0;
    var vkv = 0.0;
    var vsp = 0.0;
    var vspn = 0.0;
    var vsp1 = 0.0;
    var vsp2 = 0.0;
    var vsp3 = 0.0;
    var w1stkl5 = 0.0;
    var w2stkl5 = 0.0;
    var w3stkl5 = 0.0;
    var x = 0.0;
    var y = 0.0;
    var zre4 = 0.0;
    var zre4j = 0.0;
    var zre4vp = 0.0;
    var ztabfb = 0.0;
    var zvbez = 0.0;
    var zvbezj = 0.0;
    var zmvb = 0.0;
    var zve = 0.0;
    var zx = 0.0;
    var zzx = 0.0;

    var tab1 = arrayOf<Double>( 0.0,
        0.4,        0.384,        0.368,        0.352,        0.336,        0.32,        0.304,        0.288,
        0.272,        0.256,        0.24,        0.224,        0.208,        0.192,        0.176,        0.16,
        0.152,        0.144,        0.136,        0.128,        0.12,        0.112,        0.104,        0.096,
        0.088,        0.08,        0.072,        0.064,        0.056,        0.048,        0.04,        0.032,
        0.024,        0.016,        0.008,        0.0)

    var tab2 = arrayOf<Int>(  0,
        3000,        2880,        2760,        2640,        2520,        2400,        2280,        2160,
        2040,        1920,        1800,        1680,        1560,        1440,        1320,        1200,
        1140,        1080,        1020,        960,        900,        840,        780,        720,
        660,        600,        540,        480,        420,        360,        300,        240,
        180,        120,        60,        0)

    var tab3 = arrayOf<Int>( 0,
        900,        864,        828,        792,        756,        720,        684,        648,
        612,        576,        540,        504,        468,        432,        396,        360,
        342,        324,        306,        288,        270,        252,        234,        216,
        198,        180,        162,        144,        126,        108,        90,        72,
        54,        36,        18,        0)

    var tab4 = tab1

    var tab5 = arrayOf<Int>(0,
        1900,        1824,        1748,        1672,        1596,        1520,        1444,        1368,
        1292,        1216,        1140,        1064,        988,        912,        836,        760,
        722,        684,        646,        608,        570,        532,        494,        456,
        418,        380,        342,        304,        266,        228,        190,        152,
        114,        76,        38,        0)

    var af = 0.0;
    //var ajahr = 0;
    var alter1 = 0.0; //unn&oumltige Konstante, da Alter mit ajahr bestimmt ist
    var entsch = 0.0;
    var f = 1.0;        // var f
    var jfreib = 0.0;
    var jhinzu = 0.0;
    var jre4 = 0.0;
    var jre4ent = 0.0;
    var jvbez = 0.0;
    var krv = 0.0;
    //var kvz = 0;
    var lzz = 1.0;
    var lzzfreib = 0.0;
    var lzzhinzu = 0.0;
    var pkv = 0.0;
    var pkpv = 500.0;
    var anpkv = 300.0;  // not present in orig
    var pvs = 0.0;
    var pvz = 0.0;
    var r = 0.0;
    var re4 = 0.0;
    var sonstb = 0.0;
    var sonstent = 0.0;
    var sterbe = 0.0;
    var vbez = 0.0;
    var vbezm = 0.0;
    var vbezs = 0.0;
    var vbs = 0.0;
    var vjahr = 2006;
    var vkapa = 0.0;
    var vmt = 0.0;
    var zkf = 0.0;
    //var zmvb = 0;
    var jsonstb = 0.0;
// --- eigene Parameter
    var gebj = 0; // Geburtsjahr
    var kist = 0.0 // Kirchensteuersatz
    var jre4soz = 0.0;
    var vspkv = 0.0;
    var zre4j_rv = 0.0; // bei Vorsorgepauschale zu ber&uumlcksichtigender
// RV-pflichtiger Anteil bei Bruttolohn neben Versorgungsbez&uumlgen

// --- Ausgangsparameter,

    var bk = 0.0;
    var bks = 0.0;
    var bkv = 0.0;
    var lstlzz = 0.0;
    var solzlzz = 0.0;
    var solzs = 0.0;
    var solzv = 0.0;
    var sts = 0.0;
    var stv = 0.0;
    var vkvlzz = 0.0;
    var vkvsonst = 0.0;

// --- Ausgangsparameter DBA

    var vfrb = 0.0;
    var vfrbs1 = 0.0;
    var vfrbs2 = 0.0;
    var wvfrb = 0.0;
    var wvfrbo = 0.0;
    var wvfrbm = 0.0;


    // not writen in js
    var bundesland = 0
    var rvsatzan = 0.0
    var tbsvorv = 0.0
    var pvzusatz = 0.0

    var bemesr = 0.0
    var bemesrganz = 0.0
    var bemesk = 0.0
    var bemeskganz = 0.0

    var aloswert = 0.0
    var aloswertag = 0.0
    var sozabgabe = 0.0

    // also display, ag => tag for arbeitgeber teil
    var rentewert = 0.0
    var rentewertag = 0.0
    var pflegewert = 0.0
    var pflegewertag = 0.0
    var kvwert = 0.0
    var kvwertag = 0.0
    var agsozabgabe = 0.0           // total of arbeitgeber soz abgaben

    // clear
    fun werte() {
        /*ajahr = 0;
        alte = 0;
        anp = 0;
        anteil1 = 0;
        bmg = 0;
        bbgkvpv = 0;
        bbgrv = 0;
        diff = 0;
        efa = 0;
        fvb = 0;
        fvbso = 0;
        fvbz = 0;
        fvbzso = 0;
        gfb = 0;
        hbalte = 0;
        hfvb = 0;
        hfvbz = 0;
        hfvbzso = 0;
        hoch = 0;
        j = 0;
        jmbg = 0;
        jlfreib = 0;
        jlhinzu = 0;
        jw = 0;
        k = 0;
        kennvmt = 0;
        kfb = 0;
        kvsatzag = 0;
        kvsatzan = 0;
        kvz = 0;
        kztab = 0;
        lstjahr = 0;
        lst1 = 0;
        lst2 = 0;
        lst3 = 0;
        lstoso = 0;
        lstso = 0;
        mist = 0;
        pvsatzag = 0;
        pvsatzan = 0;
        rw = 0;
        sap = 0;
        solzsbmg = 0;
        solzszve = 0;
        solzfrei = 0;
        solzj = 0;
        solzmin = 0;
        st = 0;
        st1 = 0;
        st2 = 0;
        stovmt = 0;
        // definition tab1 usw. s. unten
        vbezb = 0;
        vbezbso = 0;
        vergl = 0;
        vhb = 0;
        vkv = 0;
        vsp = 0;
        vspn = 0;
        vsp1 = 0;
        vsp2 = 0;
        vsp3 = 0;
        w1stkl5 = 0;
        w2stkl5 = 0;
        w3stkl5 = 0;
        x = 0;
        y = 0;
        zre4 = 0;
        zre4j = 0;
        zre4vp = 0;
        ztabfb = 0;
        zvbez = 0;
        zvbezj = 0;
        zmvb = 0;
        zve = 0;
        zx = 0;
        zzx = 0;*/
    }

    fun lst2022() {
        mpara();
        mre4jl();
        vbezbso = 0.0
        kennvmt = 0.0
        mre4();
        mre4abz();
        mberech();
        msonst();
        mvmt();
    }

    /**
     * Zuweisung von Werten für bestimmte
     * Sozialversicherungsparameter
     * */
    fun mpara() {
        if (krv < 2) {
            if (krv == 0.0)
                bbgrv = 84600.0
            else
                bbgrv = 81000.0
            rvsatzan = 0.093
            tbsvorv = 0.88
        }
        bbgkvpv = 58050.0      // const
        kvsatzan = kvz / 2 / 100 + 0.07
        kvsatzag = 0.0065 + 0.07            // zusatzbetrag 0.65%
        if (pvs == 1.0) {
            pvsatzan = 0.02025
            pvsatzag = 0.01025
        } else {
            pvsatzan = 0.01525
            pvsatzag = 0.01525
        }

        if (pvz == 1.0) {
            pvsatzan = pvsatzan + 0.0035
        }

        w1stkl5 = 11793.0         // grenz für steurklass V & VI
        w2stkl5 = 29298.0
        w3stkl5 = 222260.0
        gfb = 10347.0             // grundfreibetrag
        solzfrei = 16956.0
    }

    /**
     * Ermittlung des Jahresarbeitslohns und der Freibetr&aumlge
     * */
    fun mre4jl() {
        jlfreib = lzzfreib / 100
        jlhinzu = lzzhinzu / 100
        jfreib = (jlfreib * 100) //f&uumlr mosonst()
        jhinzu = (jlhinzu * 100)

        if (lzz == 1.0) {         // 1 => jahr Zeitraum
            zre4j = re4 / 100;
            zvbezj = vbez / 100;
            //jlfreib = lzzfreib /100
            //jlhinzu = lzzhinzu / 100
        }

        if (lzz == 2.0) {         // 2 => monat Zeitraum
            zre4j = re4 * 12 / 100;
            zvbezj = vbez * 12 / 100;
            //jlfreib = (lzzfreib * 12) /100
            //jlhinzu = (lzzhinzu * 12) / 100
        }

        if (lzz == 3.0) {         // 3 => woche Zeitraum
            zre4j = re4 * 360 / 7 / 100;
            zvbezj = vbez * 360 / 7 / 100;
            //jlfreib = (lzzfreib * 360 / 7) /100
            //jlhinzu = (lzzhinzu * 360 / 7) / 100
        }

        if (lzz == 4.0) {         // 4 => tag Zeitraum
            zre4j = re4 * 360 / 100
            zvbezj = vbez * 360 / 100
            //jlfreib = (lzzfreib * 360) /100
            //jlhinzu = (lzzhinzu * 360) / 100
        }

        // ????
        if (zre4j < 0)
        // nicht im PAP
            zre4j = 0.0

        if (zvbezj <= 0 || vbez <= 0)
            zvbezj = 0.0

        if (af == 0.0)
            f = 1.0
    }

    fun mre4() {
        // //Freibetraege fuer Versorgungsbezuege, Altersentlastungsbetrag (�39b Abs. 2 Satz 3 EStG) PAP Seite 16

        if (zvbezj == 0.0) {
            fvbz = 0.0
            fvb = 0.0;
            fvbzso = 0.0;
            fvbso = 0.0;
        } else {
            if (vjahr < 2006) j = 1.0;
            else {
                if (vjahr < 2040) j = vjahr - 2004.0;
                else j = 36.0
            }
        }
        if (lzz == 1.0) {
            vbezb = vbezm * zmvb + vbezs;
            hfvb = (tab2[j.toInt()] / 12) * zmvb;
            fvbz = Math.ceil(((tab3[j.toInt()] / 12) * zmvb).toDouble());
        } else {
            vbezb = vbezm * 12 + vbezs;
            hfvb = tab2[j.toInt()].toDouble();
            fvbz = tab3[j.toInt()].toDouble();
        }

        fvb = (Math.ceil(Math.floor(vbezb * tab1[j.toInt()] * 100)) / 10000);

        if (fvb > hfvb) fvb = hfvb;

        if (fvb > zvbezj) fvb = zvbezj;

        fvbso = fvb + Math.ceil(Math.floor(vbezbso * tab1[j.toInt()] * 100)) / 10000;

        if (fvbso > tab2[j.toInt()]) fvbso = tab2[j.toInt()].toDouble();

        hfvbzso = (vbezb + vbezbso) / 100 - fvbso;
        fvbzso = Math.ceil(fvbz + vbezbso / 100);

        if (fvbzso > hfvbzso) fvbzso = Math.ceil(hfvbzso);

        if (hfvbzso > tab3[j.toInt()]) fvbzso = Math.ceil(hfvbzso);

        if (fvbzso > tab3[j.toInt()]) fvbzso = tab3[j.toInt()].toDouble();

        hfvbz = vbezb / 100 - fvb;

        if (fvbz > hfvbz) fvbz = Math.ceil(hfvbz.toDouble());

        mre4alte();
    }

    fun mre4alte() {
        // Altersentlastungsbetrag (� 39b Absatz 2 Satz 3 EStG)

        if (alter1 == 0.0)
            alte = 0.0
        else {
            if (ajahr < 2006)
                k = 1.0;
            else {
                if (ajahr < 2040)
                    k = ajahr - 2004;
                else
                    k = 36.0;
            }
        }
        bmg = zre4j - zvbezj;
        alte = Math.ceil(bmg * tab4[k.toInt()]);
        hbalte = tab5[k.toInt()];

        if (alte > hbalte) alte = hbalte.toDouble();
    }

    fun mre4abz() {
        // Ermittlung des Jahresarbeitslohns nach Abzug der Freibetr&aumlge,
        zre4 = zre4j - fvb - alte - jlfreib + jlhinzu;
        if (zre4 < 0)
            zre4 = 0.0;
        zre4vp = zre4j;

        if (kennvmt == 2.0)
            zre4vp = zre4vp - entsch / 100;

        zvbez = zvbezj - fvb;

        if (zvbez < 0) zvbez = 0.0
    }

    fun mberech() {
        //Berechnung f�r laufende Lohnzahlungszeitr�ume,

        mztabfb();
        vfrb = ((anp + fvb + fvbz) * 100)
        mlstjahr();
        wvfrb = (zve - gfb) * 100;
        if (wvfrb < 0) {
            wvfrb = 0.0
        }
        lstjahr = st * f;
        uplstlzz();
        upvkvlzz();
        if (zkf > 0) {
            ztabfb = ztabfb + kfb;
            mre4abz();
            mlstjahr();
            jbmg = st * f;
        } else {
            jbmg = lstjahr;
        }
        msolz();
    }


    fun mztabfb() {
        //Ermittlung der festen Tabellenfreibetraege (ohne Vorsorgepauschale)

        anp = 0.0
        if (zvbez >= 0) {
            if (zvbez < fvbz) fvbz = Math.floor(zvbez)  // ?? double
        }

        if (vmLegacTax.e_stkl < 6) {
            if (zvbez > 0) {
                if (zvbez - fvbz < 102)
                    anp = Math.ceil(zvbez - fvbz)
                else
                    anp = 102.0
            }
        } else {
            fvbz = 0.0;
            fvbzso = 0.0;
        }

        if (vmLegacTax.e_stkl < 6) {
            if (zre4 > zvbez) {
                if (zre4 - zvbez < 1200)
                    anp = Math.ceil(anp + zre4 - zvbez)
                else
                    anp = anp + 1200;
            }
        }

        kztab = 1.0;
        if (vmLegacTax.e_stkl == 1.0) {
            sap = 36.0;
            kfb = zkf * 8388;
        }

        if (vmLegacTax.e_stkl == 2.0) {
            efa = 4008.0;
            sap = 36.0;
            kfb = zkf * 8388;
        }
        if (vmLegacTax.e_stkl == 3.0) {
            kztab = 2.0;
            sap = 36.0;
            kfb = zkf * 8388;
        }
        if (vmLegacTax.e_stkl == 4.0) {
            sap = 36.0;
            kfb = zkf * 4194;
        }
        if (vmLegacTax.e_stkl == 5.0) {
            sap = 36.0;
            kfb = 0.0;
        }
        if (vmLegacTax.e_stkl == 6.0)
            kfb = 0.0;

        ztabfb = efa + anp + sap + fvbz
    }


    fun mlstjahr() {
        //Ermittlung der Jahreslohnsteuer

        upevp();

        if (kennvmt != 1.0) {
            zve = zre4 - ztabfb - vsp;
            upmlst();
        } else {
            zve = zre4 - ztabfb - vsp - vmt / 100 - vkapa / 100;
            if (zve < 0) {
                zve = (zve + vmt / 100 + vkapa / 100) / 5;
                upmlst();
                st = st * 5;
            } else {
                upmlst();
                stovmt = st;
                zve = zve + (vmt + vkapa) / 500;
                upmlst();
                st = (st - stovmt) * 5 + stovmt;
            }
        }
    }

    fun upvkvlzz() {
        upvkv();
        jw = vkv
        upanteil();
        vkvlzz = anteil1;
    }

    fun upvkv() {
        if (pkv > 0) {
            if (vsp2 > vsp3)
                vkv = vsp2 * 100;
            else
                vkv = vsp3 * 100;
        } else vkv = 0.0;
    }

    fun uplstlzz() {
        jw = lstjahr * 100;
        upanteil();
        lstlzz = anteil1;
    }


    fun upmlst() {
        if (zve < 1) {
            zve = 0.0;
            x = 0.0;
        } else
            x = Math.floor(zve / kztab)   // auf Euro abrunden   // datay type ????

        if (vmLegacTax.e_stkl < 5.0)
            uptab22()
        else
            mst5_6()
    }

    fun upevp() {
        // Vorsorgepauschale (�39b Abs. 2 Satz 6 Nr 3 EStG) PAP Seite 26

        if (krv > 1) {
            vsp1 = 0.0;
        } else {
            if (zre4vp > bbgrv) {
                zre4vp = bbgrv;
            }
            vsp1 = tbsvorv * zre4vp
            vsp1 = vsp1 * rvsatzan
        }

        vsp2 = 0.12 * zre4vp;
        if (vmLegacTax.e_stkl == 3.0)
            vhb = 3000.0;
        else
            vhb = 1900.0;
        if (vsp2 > vhb)
            vsp2 = vhb;
        vspn = Math.ceil(vsp1 + vsp2); //auf Euro aufrunden
        mvsp();
        if (vspn > vsp)
            vsp = vspn;
    }

    fun mvsp() {
        //Vorsorgepauschale (�39b Abs. 2 Satz 5 Nr 3 EStG) Vergleichsberechnung
        //fuer Guenstigerpruefung PAP Seite 27

        if (zre4vp > bbgkvpv) {
            zre4vp = bbgkvpv;
        }

        if (pkv > 0) {
            if (vmLegacTax.e_stkl == 6.0) {
                vsp3 = 0.0;
            } else {
                vsp3 = (pkpv * 12) / 100

                if (pkv == 2.0) {
                    vsp3 = vsp3 - zre4vp * (kvsatzag + pvsatzag);
                }
            }
        } else {
            vsp3 = zre4vp * (kvsatzan + pvsatzan);
        }
        vsp = Math.ceil(vsp3 + vsp1);
    }

    fun mst5_6() {
        // Lohnsteuer fuer die Steuerklassen V und VI (� 39b Abs. 2 Satz 7 EStG) PAP Seite 28

        zzx = x;

        if (zzx > w2stkl5) {
            zx = w2stkl5;
            up5_6();
            if (zzx > w3stkl5) {
                st = st + (w3stkl5 - w2stkl5) * 0.42;
                st = Math.floor(st + (zzx - w3stkl5) * 0.45);
            } else
                st = Math.floor(st + (zzx - w2stkl5) * 0.42);
        } else {
            zx = zzx;
            up5_6();
            if (zzx > w1stkl5) {
                vergl = st;
                zx = w1stkl5;
                up5_6();
                hoch = Math.floor(st + (zzx - w1stkl5) * 0.42);
                if (hoch < vergl)
                    st = hoch;
                else
                    st = vergl;
            }
        }
    }

    fun up5_6() {
        x = zx * 1.25;
        uptab22();
        st1 = st;
        x = zx * 0.75;
        uptab22();
        st2 = st;
        diff = (st1 - st2) * 2;
        mist = Math.floor(zx * 0.14);
        if (mist > diff)
            st = mist;
        else
            st = diff;
    }

    fun msolz() {
        // Solidarit&aumltszuschlag, PAP S. 30
        solzfrei = solzfrei * kztab;
        if (jbmg > solzfrei) {
            solzj = Math.floor(jbmg * 5.5) / 100;
            solzmin = ((jbmg - solzfrei) * 11.9) / 100;
            if (solzmin < solzj) solzj = solzmin;
            jw = solzj * 100;
            upanteil();
            solzlzz = anteil1
        } else solzlzz = 0.0;

        if (r > 0) {
            jw = jbmg * 100;
            upanteil();
            bk = anteil1
        } else
            bk = 0.0;
    }

    fun upanteil() {
        // Anteil der Jahresbeitr&aumlge f&uumlr einen LZZ
        // PAP, S. 31

        if (lzz == 1.0) {
            anteil1 = jw;
        }
        if (lzz == 2.0) {
            anteil1 = Math.floor((Math.round((jw * 100) / 12) / 100).toDouble());   //datatype??
        }
        if (lzz == 3.0) {
            anteil1 = Math.floor((Math.round((jw * 700) / 360) / 100).toDouble());  //datatype??
        }
        if (lzz == 4.0) {
            anteil1 = Math.floor(Math.round(jw / 360).toDouble());  //datatype??
        }
    }

    fun msonst() {
        // Berechnung sonstiger Bez&uumlge

        lzz = 1.0;
        if (zmvb == 0.0) zmvb = 12.0;
        if (sonstb == 0.0) {
            vkvsonst = 0.0;
            lstso = 0.0;
            sts = 0.0;
            solzs = 0.0;
            bks = 0.0;
        } else {
            mosonst();
            upvkv();
            vkvsonst = vkv;
            zre4j = (jre4 + sonstb) / 100;
            zvbezj = (jvbez + vbs) / 100;
            vbezbso = sterbe;
            mre4sonst();
            mlstjahr();
            wvfrbm = (zve - gfb) * 100;
            if (wvfrbm < 0) {
                wvfrbm = 0.0;
            }
            upvkv();
            vkvsonst = vkv - vkvsonst;
            lstso = st * 100;
            sts = Math.floor((lstso - lstoso) * f);
            if (sts < 0) sts = 0.0;
            msolzsts();
            if (r > 0)
                bks = sts;
            else
                bks = 0.0;
        }
    }

    fun msolzsts(){
        // neu in PAP 2021: Berechnung des SolZ auf sonstige Bezüge
        if (zkf > 0)
            solzszve = zve - kfb;
        else
            solzszve = zve;

        if (solzszve < 1) {
            solzszve = 0.0
            x = 0.0
        }
        else x = Math.floor((solzszve / kztab).toDouble()); //datatype??

        if (vmLegacTax.e_stkl < 5) uptab22();
        else mst5_6();

        solzsbmg = Math.floor(st * f);

        if (solzsbmg > solzfrei)
            solzs = Math.floor(sts * 5.5) / 100;
        else
            solzs = 0.0;
    }

    fun mvmt() {
        // Berechnung der Verg&uumltung f&uumlr mehrj&aumlhrige T&aumltigkeit
        // PAP, S. 33
        if (vkapa < 0) vkapa = 0.0;
        if (vmt + vkapa > 0) {
            if (lstso == 0.0) {
                mosonst();
                lst1 = lstoso;
            } else lst1 = lstso;

            vbezbso = sterbe + vkapa;
            zre4j = (jre4 + sonstb + vmt + vkapa) / 100;
            zvbezj = (jvbez + vbs + vkapa) / 100;
            kennvmt = 2.0;
            mre4sonst();
            mlstjahr();
            lst3 = st * 100;
            mre4abz();
            zre4vp = zre4vp - jre4ent / 100 - sonstent / 100;
            kennvmt = 1.0;
            mlstjahr();
            lst2 = st * 100;
            stv = lst2 - lst1;
            lst3 = lst3 - lst1;

            if (lst3 < stv)
                stv = lst3;
            if (stv < 0)
                stv = 0.0;
            else
                stv = Math.floor(stv * f);

            solzv = Math.floor(stv * 5.5) / 100;
            if (r > 0)
                bkv = stv;
            else
                bkv = 0.0;
        } else {
            stv = 0.0;
            solzv = 0.0;
            bkv = 0.0;
        }
    }

    fun mosonst() {
        // Sonderberechnung ohne sonstige Bez&uumlge f&uumlr Berechnung sonstige Bez&uumlge/Verg&uumltung
        // mehrj&aumlhrige T&aumltigkeit.

        zre4j = jre4 / 100;
        zvbezj = jvbez / 100;
        jlfreib = jfreib / 100;
        jlhinzu = jhinzu / 100;
        mre4();
        mre4abz();
        zre4vp = zre4vp - jre4ent / 100;
        mztabfb();
        vfrbs1 = (anp + fvb + fvbz) * 100;
        mlstjahr();
        wvfrbo = (zve - gfb) * 100;
        if (wvfrbo < 0) {
            wvfrbo = 0.0;
        }
        lstoso = st * 100;
    }

    fun mre4sonst() {
        // Sonderberechnung mit sonstigen Bez&uumlge f&uumlr Berechnung sonstige Bez&uumlge/Verg&uumltung
        // mehrj&aumlhrige T&aumltigkeit. PAP, S. 35

        mre4();
        fvb = fvbso;
        mre4abz();
        zre4vp = zre4vp - jre4ent / 100 - sonstent / 100;
        fvbz = fvbzso;
        mztabfb();
        vfrbs2 = (anp + fvb + fvbz) * 100 - vfrbs1;
    }

    fun uptab22() {
        //Tarifliche Einkommensteuero

        if (x <= gfb) st = 0.0;
        else {
            if (x < 14927) {
                y = (x - gfb) / 10000;
                rw = y * 1088.7;
                rw = rw + 1400;
                st = Math.floor(rw * y);
            } else {
                if (x < 58597) {
                    y = (x - 14926) / 10000;
                    rw = y * 206.43;
                    rw = rw + 2397;
                    rw = rw * y;
                    st = Math.floor(rw + 869.32);
                } else {
                    if (x < 277826)
                        st = Math.floor(x * 0.42 - 9336.45);
                    else
                        st = Math.floor(x * 0.45 - 17671.2);
                }
            }
        }
        st = st * kztab;
    }

    fun jahranteil() {
        // eigene Funktion zum Hochrechnen auf Jahreswerte

        if (lzz == 1.0)
            anteil1 = jw;

        if (lzz == 2.0)
            anteil1 = Math.floor(jw * 12);

        if (lzz == 3.0)
            anteil1 = Math.floor((jw * 360) / 7);

        if (lzz == 4.0)
            anteil1 = Math.floor(jw * 360);
    }

    // ###########################

    fun hasPrivateKrankVers() {
        if (pkpv > 0.0 || anpkv > 0.0) {
            return;
        }
    }

    fun setData() {     // Calc

        // #####

        // #####
        kvz = vmLegacTax.e_kvz
        re4 = vmLegacTax.e_re4 * 100        // brutto lohn
        var re4sozlzz = re4
        sonstb = vmLegacTax.e_sonstb * 100      // optional
        jsonstb = vmLegacTax.e_jsonstb * 100    // optional
        vmt = vmLegacTax.e_vmt * 100            // optional
        lzz = vmLegacTax.e_lzz                 // optional
        entsch = vmLegacTax.e_entsch * 100      // optional
        lzzfreib = vmLegacTax.e_wfundf * 100    // optional
        lzzhinzu = vmLegacTax.e_hinzur * 100    // optional
        pkpv = vmLegacTax.e_pkpv * 100
        anpkv = vmLegacTax.e_anpkv * 1200
        //pkpv = e_pkpv.toDouble() * 100
        //anpkv = e_anpkv.toDouble() * 1200

        pkv = 0.0
        if (vmLegacTax.isPrivatInsur)
            if (vmLegacTax.mitag)
                pkv = 2.0
            else
                pkv = 1.0

        ajahr = vmLegacTax.geb_tag // from view must
        ajahr = 2022.0 // from view must
        alter1 = 0.0
        if (ajahr < 2018)
            alter1 = 1.0


        var stkl = vmLegacTax.e_stkl
        f = 1.0
        if (vmLegacTax.e_stkl == 4.0)
            f = vmLegacTax.e_f // flimit()
        af = 0.0
        if (f > 0.0 && f < 1.0)
            af = 1.0
        else
            f =  1.0

        lzz = vmLegacTax.e_lzz
        var lzzsoz = lzz

        zkf = vmLegacTax.e_zkf  // * 0.5, not need multiply by 1/2, bc its take direct value

        // if ( zkf > 0)
        //      uncheck kinder    done!

        // kirchsteuer
        //if (e_r > 0 )         done!
        r = vmLegacTax.e_r

        // ignore, working only with optional data
        if (stkl == 6.0 && lzzhinzu > 0) {
            lzzhinzu = 0.0
        }


        // important after set ui, to put to work also for steuer cl 2
        /*if (stkl == 2 && zkf == 0) {          done!
            alert("Bei Steuerklasse II muss ein Kinderfreibetrag angegeben werden!");
            document.eingabe.e_zkf.selectedIndex = 2;
            document.eingabe.kinderlos[0].checked;
            document.eingabe.e_zkf.focus();         done!
            Calc();   ??????
        }*/

        kist = vmLegacTax.e_r * 0.01        // kirch steuer

        krv = 0.0

        bundesland = vmLegacTax.e_bundesland // it's already  + 1 from view

        if (
         bundesland == 4 ||
         bundesland == 5 ||
         bundesland == 9 ||
         bundesland == 14 ||
         bundesland == 15 ||
         bundesland == 17
        )
            krv = 1.0

        if (!vmLegacTax.e_krv)
            krv = 2.0           // to find??

        pvs = 0.0

        if (bundesland == 14)
            pvs = 1.0

        pvzusatz = 0.0
        pvz = 0.0

        if (vmLegacTax.kinderlos && vmLegacTax.e_zkf == 0.0) {
            pvzusatz = 0.35
            pvz = 1.0
        }

        mre4jl()
        jre4 = zre4j * 100 + jsonstb // Voraussichtlicher Jahreslohn f&uuml;r Sonstige Bez&uuml;ge
        jvbez = zvbezj * 100  // darin enthaltene Versorgungsbez&uuml;ge
        jre4soz = zre4j * 100  // Ber&uuml;cksichtigung schon abgerechneter                                                        Einmalzahlungen/Jahr
        lst2022()  //Aufruf PAP Berechnung


        var steuer = Math.floor(lstlzz + sts + stv) / 100;
        var soli = Math.floor(solzlzz + solzs + solzv + 0.0001) / 100;
        var kisteuer = Math.floor((bk + bks + bkv) * kist) / 100;
        lstlzz = Math.floor(lstlzz) / 100;
        sts = Math.floor(sts) / 100;
        stv = Math.floor(stv) / 100;

        sozberech()

        var stganz = steuer + soli + kisteuer // sum steuer
        var netto = ((re4sozlzz + sonstb + vmt) / 100 - sozabgabe - stganz) * 100 / 100


        if (vmLegacTax.isProYear) {
            reportTaxModel.netSalary = netto
            reportTaxModel.netSalaryMonthly = netto / 12
        } else {
            reportTaxModel.netSalary = netto * 12
            reportTaxModel.netSalaryMonthly = netto
        }

        reportTaxModel.taxes = steuer
        reportTaxModel.taxesByBrutto = lstlzz
        reportTaxModel.oneTimePay = sts
        reportTaxModel.multiYearEmploy = stv
        reportTaxModel.solidaritat = soli
        reportTaxModel.churchTax = kisteuer
        reportTaxModel.sumTax = steuer + kisteuer + soli

        reportTaxModel.pension = rentewert
        reportTaxModel.unemployed = aloswert
        reportTaxModel.medInsurance = kvwert
        reportTaxModel.careInsurance = pflegewert
        reportTaxModel.socialSum = sozabgabe

        reportTaxModel.pensionCompany = rentewertag
        reportTaxModel.unemployedCompany = aloswertag
        reportTaxModel.medInsuranceCompany = kvwertag
        reportTaxModel.careInsuranceCompany = pflegewertag
        reportTaxModel.socialSumCompany = agsozabgabe

        reportTaxModel.totalLoadCompany = agsozabgabe + vmLegacTax.e_re4 + sts

        Log.d("taxes", "steuer:  " + steuer)
        Log.d("taxes", "davon für Brutolohn:  " + lstlzz)
        Log.d("taxes", "für Einmalzahlung:  " + sts)
        Log.d("taxes", "für mehrjährige Tätigkeit:  " + stv)
        Log.d("taxes", "soli:  " + soli)
        Log.d("taxes", "kisteuer:  " + kisteuer)
        Log.d("taxes", "###Summe der Steuern:  " + (steuer + kisteuer + soli))
        Log.d("taxes", "renteVers:  " + rentewert)
        Log.d("taxes", "arbeitslosVerse:  " + aloswert)
        Log.d("taxes", "krankVers:  " + kvwert)
        Log.d("taxes", "pflegeVers:  " + pflegewert)
        Log.d("taxes", "sozabgabe:  " + sozabgabe)
        Log.d("taxes", "Nettolohn:  " + netto)

        Log.d("taxes", "Arebitgeberanteil renteVers:  " + rentewertag)
        Log.d("taxes", "Arebitgeberanteil arbeitslosVerse:  " + aloswertag)
        Log.d("taxes", "Arebitgeberanteil krankVers:  " + kvwertag)
        Log.d("taxes", "Arebitgeberanteil pflegeVers:  " + pflegewertag)
        Log.d("taxes", "Arebitgeberanteil sozabgabe:  " + agsozabgabe)
        Log.d("taxes", "Gesamtbelastung Arbeitgeber:  " + (agsozabgabe + vmLegacTax.e_re4 + sts))

    }


    fun sozberech() {

        lzz = vmLegacTax.e_lzz
        var pflege = 1.525;
        var pflege_ag = pflege;

        if (bundesland == 14) {
            pflege = 2.025;
            pflege_ag = 1.025;
        }

        var kvsatz = vmLegacTax.e_barmer

        Log.d("taxes", "IsPrivate insurance:  " + vmLegacTax.isPrivatInsur)
        if (vmLegacTax.isPrivatInsur)
            kvsatz = 0.0
        else
            anpkv = 0.0

        // wtf
        bemesberech(); // ermitteln
        jw = bemesk
        var bemeskoso = bemesk
        upanteil();
        var bemesklzz = anteil1;
        jw = bemesr
        var bemesroso = bemesr;
        upanteil();
        var bemesrlzz = anteil1;

        // wtf

        jre4soz = jre4soz + sonstb + jsonstb

        bemesberech(); // ermitteln
        bemeskganz = bemesk;
        bemesrganz = bemesr;
        bemesk =
            Math.round(bemesklzz + Math.max(bemeskganz - (bemeskoso + jsonstb), 0.0)) / 100.0
        bemesr =
            Math.round(bemesrlzz + Math.max(bemesrganz - (bemesroso + jsonstb.toInt()), 0.0)) / 100.0
        val rente = 9.3;
        val alos = 1.2;
        kvz = vmLegacTax.e_kvz

        var pkvbemes = Math.round(((7.3 + 0.65 + 1.525) / 100) * 5805000); // 7,3% + 1,525% + 0.55% Zusatzbeitrag in 2020
        if (vmLegacTax.e_bundesland == 14)
            pkvbemes = Math.round(((7.3 + 0.65 + 1.025) / 100) * 5805000); // 7,3% + 1,025% + 0.55% Zusatzbeitrag in 2020
        if (kvsatz == 0.0)
            bemesk = 0.0

        rentewert = Math.round(bemesr * rente) / 100.0
        rentewertag = rentewert;
        kvwert = Math.round((bemesk * kvsatz) / 2 + (bemesk * kvz) / 2) / 100.0
        kvwertag = kvwert
        pflegewert = Math.round(bemesk * pflege + bemesk * pvzusatz) / 100.0
        pflegewertag = Math.round(bemesk * pflege_ag) / 100.0


        if (anpkv > 0) {
            if (vmLegacTax.mitag)
                kvwertag = Math.round(Math.min(anpkv / 2, pkvbemes.toDouble()) * 100) / 100.0
            pflegewertag = 0.0
            pflegewert = 0.0
            if (anpkv == 0.0 || pkv == 1.0)
                kvwertag = 0.0

            jw = Math.round(anpkv - kvwertag).toDouble()
            upanteil();
            kvwert = Math.floor(anteil1) / 100
            jw = kvwertag.toDouble();
            upanteil();
            kvwertag = Math.floor(anteil1) / 100
        }
        aloswert = Math.round(bemesr * alos) / 100.0
        aloswertag = aloswert;
        if (krv == 2.0) {
            rentewert = 0.0;
            rentewertag = 0.0;
        }

        if (!vmLegacTax.e_av) {
            aloswert = 0.0;
            aloswertag = 0.0;
        }
        agsozabgabe =
            Math.floor((rentewertag + kvwertag + pflegewertag + aloswertag) * 100) / 100
        sozabgabe =
            Math.floor((rentewert + kvwert + pflegewert + aloswert) * 1000) / 1000
    }

    fun bemesberech() {
        bemesk = Math.min(5805000.0, jre4soz)

        if (krv == 1.0)
            bemesr = Math.min(8100000.0, jre4soz)
        else
            bemesr = Math.min(8460000.0, jre4soz)
    }

}

