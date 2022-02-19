package ir.awlrhm.persiandatepicker.util

import saman.zamani.persiandate.PersianDate


fun convertPersianDateToGregorian(persianDate: String): String? {
    val calendar = PersianDate()
    val date = persianDate.split("/".toRegex()).toTypedArray()
    return if (date.size > 2) {
        val geoDate: IntArray =
            calendar.jalali_to_gregorian(date[0].toInt(), date[1].toInt(), date[2].toInt())
        val year = geoDate[0].toString()
        val month =
            if (geoDate[1].toString().length == 1) "0" + geoDate[1] else geoDate[1].toString()
        val day =
            if (geoDate[2].toString().length == 1) "0" + geoDate[2] else geoDate[2].toString()
        "$year-$month-$day"
    } else calendar.grgYear
        .toString() + "-" + calendar.grgMonth + "-" + calendar.grgDay
}

fun convertGregorianToPersianDate(geoDate: String): String{
    val calendar = PersianDate()
    val date = geoDate.split("-".toRegex()).toTypedArray()
    return if(date.size > 2){
        val persianDate: IntArray = calendar.gregorian_to_jalali(date[0].toInt(), date[1].toInt(), date[2].toInt())
        val year = persianDate[0].toString()
        val month =
            if (persianDate[1].toString().length == 1) "0" + persianDate[1] else persianDate[1].toString()
        val day =
            if (persianDate[2].toString().length == 1) "0" + persianDate[2] else persianDate[2].toString()
        "$year/$month/$day"
    }else
        calendar.shYear.toString() + "/" + calendar.shMonth + "/" + calendar.shDay
}