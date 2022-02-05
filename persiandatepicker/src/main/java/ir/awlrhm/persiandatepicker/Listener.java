package ir.awlrhm.persiandatepicker;

import ir.awlrhm.persiandatepicker.util.PersianCalendar;

/**
 * Created by aliabdolahi on 1/23/17.
 */

@Deprecated
public interface Listener {

    @Deprecated
    void onDateSelected(PersianCalendar persianCalendar);

    @Deprecated
    void onDismissed();
}
