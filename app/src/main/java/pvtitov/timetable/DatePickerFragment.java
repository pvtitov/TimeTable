package pvtitov.timetable;



import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

import pvtitov.timetable.contracts.Date;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    interface DateConsumer{
        void onDatePicked(Date date);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Дата по умолчанию - текущая дата
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Date date = new Date();
        date.setDay(day);
        date.setMonth(month);
        date.setYear(year);
        DateConsumer dateConsumer = (DateConsumer) getActivity();
        if (dateConsumer != null) dateConsumer.onDatePicked(date);

    }
}