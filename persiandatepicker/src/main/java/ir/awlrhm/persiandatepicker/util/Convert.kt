package ir.awlrhm.persiandatepicker.util

import saman.zamani.persiandate.PersianDate


fun convertPersianDateToGeo(persianDate: String): String? {
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