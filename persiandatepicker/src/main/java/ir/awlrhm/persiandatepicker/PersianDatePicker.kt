package ir.awlrhm.persiandatepicker

import android.content.Context
import android.content.res.Resources.NotFoundException
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.NumberPicker
import android.widget.NumberPicker.OnValueChangeListener
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import ir.awlrhm.persiandatepicker.api.PersianPickerDate
import ir.awlrhm.persiandatepicker.date.PersianDateImpl
import ir.awlrhm.persiandatepicker.util.PersianCalendar
import ir.awlrhm.persiandatepicker.util.PersianCalendarConstants
import ir.awlrhm.persiandatepicker.util.PersianCalendarUtils
import ir.awlrhm.persiandatepicker.util.PersianHelper
import ir.awlrhm.persiandatepicker.view.PersianNumberPicker
import ir.hamsaa.persiandatepicker.R
import java.util.*

class PersianDatePicker(context: Context, attrs: AttributeSet): LinearLayout(context, attrs) {

    private var persianDate: PersianPickerDate? = null
    private var selectedMonth = 0
    private var selectedYear = 0
    private var selectedDay = 0
    private var displayMonthNames = false
    private var mListener: OnDateChangedListener? = null
    private var yearNumberPicker: PersianNumberPicker? = null
    private var monthNumberPicker: PersianNumberPicker? = null
    private var dayNumberPicker: PersianNumberPicker? = null

    private var minYear = 0
    private var maxYear = 0
    private var maxMonth = 0
    private var maxDay = 0

    private var displayDescription = false
    private var descriptionTextView: TextView? = null
    private var typeFace: Typeface? = null
    private var dividerColor = 0
    private var yearRange = 0
    
    init {
        val view: View = LayoutInflater.from(context).inflate(R.layout.sl_persian_date_picker, this)

        yearNumberPicker = view.findViewById(R.id.yearNumberPicker)
        monthNumberPicker = view.findViewById(R.id.monthNumberPicker)
        dayNumberPicker = view.findViewById(R.id.dayNumberPicker)
        descriptionTextView = view.findViewById(R.id.descriptionTextView)


        yearNumberPicker?.setFormatter { i -> PersianHelper.toPersianNumber(i.toString() + "") }

        monthNumberPicker?.setFormatter { i -> PersianHelper.toPersianNumber(i.toString() + "") }

        dayNumberPicker?.setFormatter { i -> PersianHelper.toPersianNumber(i.toString() + "") }

        persianDate = PersianDateImpl()

        updateVariablesFromXml(context, attrs)

        updateViewData()
    }

    private fun updateVariablesFromXml(context: Context, attrs: AttributeSet) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.PersianDatePicker, 0, 0)
        yearRange = a.getInteger(R.styleable.PersianDatePicker_yearRange, 10)
        /*
         * Initializing yearNumberPicker min and max values If minYear and
         * maxYear attributes are not set, use (current year - 10) as min and
         * (current year + 10) as max.
         */minYear =
            a.getInt(R.styleable.PersianDatePicker_minYear, persianDate!!.persianYear - yearRange)
        maxYear =
            a.getInt(R.styleable.PersianDatePicker_maxYear, persianDate!!.persianYear + yearRange)
        displayMonthNames = a.getBoolean(R.styleable.PersianDatePicker_displayMonthNames, false)
        /*
         * displayDescription
         */displayDescription =
            a.getBoolean(R.styleable.PersianDatePicker_displayDescription, false)
        selectedDay =
            a.getInteger(R.styleable.PersianDatePicker_selectedDay, persianDate!!.persianDay)
        selectedYear =
            a.getInt(R.styleable.PersianDatePicker_selectedYear, persianDate!!.persianYear)
        selectedMonth =
            a.getInteger(R.styleable.PersianDatePicker_selectedMonth, persianDate!!.persianMonth)

        // if you pass selected year before min year, then we need to push min year to before that
        if (minYear > selectedYear) {
            minYear = selectedYear - yearRange
        }
        if (maxYear < selectedYear) {
            maxYear = selectedYear + yearRange
        }
        a.recycle()
    }

    override fun setBackgroundColor(@ColorInt color: Int) {
        yearNumberPicker!!.setBackgroundColor(color)
        monthNumberPicker!!.setBackgroundColor(color)
        dayNumberPicker!!.setBackgroundColor(color)
    }

    fun setBackgroundDrawable(@DrawableRes drawableBg: Int) {
        yearNumberPicker!!.setBackgroundResource(drawableBg)
        monthNumberPicker!!.setBackgroundResource(drawableBg)
        dayNumberPicker!!.setBackgroundResource(drawableBg)
    }

    fun setMaxYear(maxYear: Int) {
        this.maxYear = maxYear
        updateViewData()
    }

    fun setMaxMonth(maxMonth: Int) {
        this.maxMonth = maxMonth
        updateViewData()
    }

    fun setMaxDay(maxDay: Int) {
        this.maxDay = maxDay
        updateViewData()
    }

    fun setMinYear(minYear: Int) {
        this.minYear = minYear
        updateViewData()
    }

    fun setTypeFace(typeFace: Typeface?) {
        this.typeFace = typeFace
        updateViewData()
    }

    fun setDividerColor(@ColorInt color: Int) {
        dividerColor = color
        updateViewData()
    }

    private fun setDividerColor(picker: NumberPicker, color: Int) {
        val pickerFields = NumberPicker::class.java.declaredFields
        for (pf in pickerFields) {

            if (pf.name == "mSelectionDivider") {
                pf.isAccessible = true
                try {
                    val colorDrawable = ColorDrawable(color)
                    pf[picker] = colorDrawable
                } catch (e: IllegalArgumentException) {
                    e.printStackTrace()
                } catch (e: NotFoundException) {
                    e.printStackTrace()
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                }
                break
            }
        }
    }


    private fun updateViewData() {
        if (typeFace != null) {
            yearNumberPicker!!.setTypeFace(typeFace)
            monthNumberPicker!!.setTypeFace(typeFace)
            dayNumberPicker!!.setTypeFace(typeFace)
        }
        if (dividerColor > 0) {
            setDividerColor(yearNumberPicker!!, dividerColor)
            setDividerColor(monthNumberPicker!!, dividerColor)
            setDividerColor(dayNumberPicker!!, dividerColor)
        }
        yearNumberPicker!!.minValue = minYear
        yearNumberPicker!!.maxValue = maxYear
        if (selectedYear > maxYear) {
            selectedYear = maxYear
        }
        if (selectedYear < minYear) {
            selectedYear = minYear
        }
        yearNumberPicker!!.value = selectedYear
        yearNumberPicker!!.setOnValueChangedListener(dateChangeListener)

        /*
         * initialing monthNumberPicker
         */monthNumberPicker!!.minValue = 1
        monthNumberPicker!!.maxValue = if (maxMonth > 0) maxMonth else 12
        if (displayMonthNames) {
            monthNumberPicker!!.displayedValues = PersianCalendarConstants.persianMonthNames
        }
        require(!(selectedMonth < 1 || selectedMonth > 12)) {
            String.format(
                "Selected month (%d) must be between 1 and 12",
                selectedMonth
            )
        }
        monthNumberPicker!!.value = selectedMonth
        monthNumberPicker!!.setOnValueChangedListener(dateChangeListener)

        /*
         * initializing dayNumberPicker
         */dayNumberPicker!!.minValue = 1
        setDayNumberPickerMaxValue(31)
        require(!(selectedDay > 31 || selectedDay < 1)) {
            String.format(
                "Selected day (%d) must be between 1 and 31",
                selectedDay
            )
        }
        if (selectedMonth in 7..11 && selectedDay == 31) {
            selectedDay = 30
        } else {
            val isLeapYear: Boolean = PersianCalendarUtils.isPersianLeapYear(selectedYear)
            if (isLeapYear && selectedDay == 31) {
                selectedDay = 30
            } else if (selectedDay > 29) {
                selectedDay = 29
            }
        }
        dayNumberPicker!!.value = selectedDay
        dayNumberPicker!!.setOnValueChangedListener(dateChangeListener)
        if (displayDescription) {
            descriptionTextView!!.visibility = VISIBLE
            descriptionTextView!!.text = persianDate!!.persianLongDate
        }
    }

    var dateChangeListener =
        OnValueChangeListener { picker, oldVal, newVal ->
            val year = yearNumberPicker!!.value
            val isLeapYear: Boolean = PersianCalendarUtils.isPersianLeapYear(year)
            val month = monthNumberPicker!!.value
            val day = dayNumberPicker!!.value
            if (month < 7) {
                dayNumberPicker!!.minValue = 1
                setDayNumberPickerMaxValue(31)
            } else if (month < 12) {
                if (day == 31) {
                    dayNumberPicker!!.value = 30
                }
                dayNumberPicker!!.minValue = 1
                setDayNumberPickerMaxValue(30)
            } else if (month == 12) {
                if (isLeapYear) {
                    if (day == 31) {
                        dayNumberPicker!!.value = 30
                    }
                    dayNumberPicker!!.minValue = 1
                    setDayNumberPickerMaxValue(30)
                } else {
                    if (day > 29) {
                        dayNumberPicker!!.value = 29
                    }
                    dayNumberPicker!!.minValue = 1
                    setDayNumberPickerMaxValue(29)
                }
            }
            persianDate!!.setDate(
                year,
                month,
                day
            )

            // Set description
            if (displayDescription) {
                descriptionTextView!!.text = persianDate!!.persianLongDate
            }
            if (mListener != null) {
                mListener?.onDateChanged(year, month, day)
            }
        }

    fun setDayNumberPickerMaxValue(value: Int) {
        if (monthNumberPicker!!.value === maxMonth) {
            if (maxDay > 0) {
                dayNumberPicker!!.maxValue = maxDay
            } else {
                dayNumberPicker!!.maxValue = value
            }
        } else {
            dayNumberPicker!!.maxValue = value
        }
    }

    fun setOnDateChangedListener(onDateChangedListener: OnDateChangedListener) {
        mListener = onDateChangedListener
    }

    /**
     * The callback used to indicate the user changed the date.
     * A class that wants to be notified when the date of PersianDatePicker
     * changes should implement this interface and register itself as the
     * listener of date change events using the PersianDataPicker's
     * setOnDateChangedListener method.
     */
    interface OnDateChangedListener {
        /**
         * Called upon a date change.
         *
         * @param newYear  The year that was set.
         * @param newMonth The month that was set (1-12)
         * @param newDay   The day of the month that was set.
         */
        fun onDateChanged(newYear: Int, newMonth: Int, newDay: Int)
    }

    fun getDisplayDate(): Date {
        return persianDate!!.gregorianDate
    }

    fun setDisplayDate(displayDate: Date?) {
        persianDate!!.setDate(displayDate)
        setDisplayPersianDate(persianDate)
    }

    /**
     * @return [PersianCalendar] that indicate current calendar state
     * @Deprecated Use getPersianDate() instead
     */
    @Deprecated("")
    fun getDisplayPersianDate(): PersianCalendar? {
        val persianCalendar = PersianCalendar()
        persianCalendar.setPersianDate(
            persianDate!!.persianYear,
            persianDate!!.persianMonth,
            persianDate!!.persianDay
        )
        return persianCalendar
    }

    fun getPersianDate(): PersianPickerDate? {
        return persianDate
    }

    /**
     * @Deprecated Use setDisplayPersianDate(PersianPickerDate displayPersianDate) instead
     */
    @Deprecated("")
    fun setDisplayPersianDate(displayPersianDate: PersianCalendar) {
        val persianPickerDate: PersianPickerDate = PersianDateImpl()
        persianPickerDate.setDate(
            displayPersianDate.persianYear,
            displayPersianDate.persianMonth,
            displayPersianDate.persianDay
        )
        setDisplayPersianDate(persianPickerDate)
    }

    fun setDisplayPersianDate(displayPersianDate: PersianPickerDate?) {
        persianDate!!.setDate(displayPersianDate?.timestamp)
        val year = persianDate!!.persianYear
        val month = persianDate!!.persianMonth
        val day = persianDate!!.persianDay
        selectedYear = year
        selectedMonth = month
        selectedDay = day

        // if you pass selected year before min year, then we need to push min year to before that
        if (minYear > selectedYear) {
            minYear = selectedYear - yearRange
            yearNumberPicker!!.minValue = minYear
        }

        // if you pass selected year after max year, then we need to push max year to after that
        if (maxYear < selectedYear) {
            maxYear = selectedYear + yearRange
            yearNumberPicker!!.maxValue = maxYear
        }
        yearNumberPicker!!.post { yearNumberPicker!!.value = year }
        monthNumberPicker!!.post { monthNumberPicker!!.value = month }
        dayNumberPicker!!.post { dayNumberPicker!!.value = day }
    }

    override fun onSaveInstanceState(): Parcelable? {
        // begin boilerplate code that allows parent classes to save state
        val superState = super.onSaveInstanceState()
        val ss = SavedState(superState)
        // end
        ss.datetime = getDisplayDate().time
        return ss
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        // begin boilerplate code so parent classes can restore state
        if (state !is SavedState) {
            super.onRestoreInstanceState(state)
            return
        }
        val ss = state
        super.onRestoreInstanceState(ss.superState)
        // end
        setDisplayDate(Date(ss.datetime))
    }

    internal class SavedState : BaseSavedState {
        var datetime: Long = 0

        constructor(superState: Parcelable?) : super(superState)
        private constructor(`in`: Parcel) : super(`in`) {
            datetime = `in`.readLong()
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeLong(datetime)
        }
          
    }
}