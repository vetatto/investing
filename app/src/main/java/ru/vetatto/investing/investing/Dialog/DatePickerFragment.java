package ru.vetatto.investing.investing.Dialog;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.List;

import ru.vetatto.investing.investing.GraphicEditActiv.GraphicLegendAdapter;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{

    public interface onDatePickerDialogChangeListener {
        void onDatePickerDialogClicked(String date);
    }
    private onDatePickerDialogChangeListener mDatePickerDialogChangeListener;
    public void setOnDatePickerDialogChangeListener(onDatePickerDialogChangeListener l) {
        mDatePickerDialogChangeListener = l;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
         Calendar newDate = Calendar.getInstance();
        DatePickerDialog tpd = new DatePickerDialog(getActivity(), this, newDate.get(Calendar.YEAR),newDate.get(Calendar.MONTH), newDate.get(Calendar.DAY_OF_MONTH));
        return tpd;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        int myYear = year;
        int myMonth2 = monthOfYear;
        int myDay = dayOfMonth;
        String myMonth="";
        switch(myMonth2) {
            case 0:myMonth="01";
                break;
            case 1:myMonth="02";
                break;
            case 2:myMonth="03";
                break;
            case 3:myMonth="04";
                break;
            case 4:myMonth="05";
                break;
            case 5:myMonth="06";
                break;
            case 6:myMonth="07";
                break;
            case 7:myMonth="08";
                break;
            case 8:myMonth="09";
                break;
            case 9:myMonth="10";
                break;
            case 10:myMonth="11";
                break;
            case 11:myMonth="12";
                break;
        }
        mDatePickerDialogChangeListener.onDatePickerDialogClicked(myDay + "." + myMonth + "." + myYear);
    }
    }

