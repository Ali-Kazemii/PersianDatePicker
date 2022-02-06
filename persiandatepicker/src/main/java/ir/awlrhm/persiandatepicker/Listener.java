package ir.awlrhm.persiandatepicker;

import ir.awlrhm.persiandatepicker.util.PersianCalendar;

/**
 * Created by Ali_Kazemi on 01/18/2019.
 */

@Deprecated
public interface Listener {

    @Deprecated
    void onDateSelected(PersianCalendar persianCalendar);

    @Deprecated
    void onDismissed();
}
