package com.example.persiandatepicker

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ir.awlrhm.persiandatepicker.PersianDatePicker
import ir.awlrhm.persiandatepicker.util.PersianCalendar
import ir.awlrhm.persiandatepicker.util.convertGregorianToPersianDate
import ir.awlrhm.persiandatepicker.view.dialog.Listener
import ir.awlrhm.persiandatepicker.view.dialog.PersianDatePickerDialog

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val datePicker = findViewById<PersianDatePicker>(R.id.ggDatePicker)

        datePicker.setMaxYear(1450)
        datePicker.setOnDateChangedListener(object: PersianDatePicker.OnDateChangedListener{
            override fun onDateChanged(newYear: Int, newMonth: Int, newDay: Int) {
//                Toast.makeText(this@MainActivity, "$newYear/$newMonth/$newDay", Toast.LENGTH_LONG).show()
            }
        })

        findViewById<Button>(R.id.btnDate).setOnClickListener {
            /*val persianCalendar = datePicker.getPersianDate()
            val chosenDate = persianCalendar?.persianYear
                .toString() + "/" + persianCalendar?.persianMonth + "/" + persianCalendar?.persianDay*/

            Toast.makeText(this@MainActivity, convertGregorianToPersianDate("2022-02-28"), Toast.LENGTH_LONG).show()
//            initDatePicker()
        }

    }

    private fun initDatePicker() {
        val picker: PersianDatePickerDialog = PersianDatePickerDialog(
            this
        )
            .setPositiveButtonString("تایید")
            .setNegativeButton("بیخیال")
            .setTodayButton("امروز")
            .setTodayButtonVisible(true)
            .setMinYear(1380)
            .setMaxYear(1500)
            .setTitleColor(Color.BLUE)
            .setActionTextColor(Color.RED)
            .setTitleType(PersianDatePickerDialog.DAY_MONTH_YEAR)
            .setCancelable(false)
            .setShowInBottomSheet(true)
            .setListener(object : Listener {
                override fun onDateSelected(persianCalendar: PersianCalendar) {
                    Toast.makeText(this@MainActivity,
                        persianCalendar.persianYear
                            .toString() + "/" + persianCalendar.persianMonth + "/" + persianCalendar.persianDay,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onDismissed() {}
            })
        picker.show()
    }
}