package itcrowd.com.numberpicker;


import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.widget.EditText;
import android.widget.NumberPicker;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import itcrowd.com.numberpicker.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding mBinder;
    String mDates[];
    Calendar mRightNow = Calendar.getInstance();

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setTheDates();
        setTheHours();
        changeTimeAccordingToDate();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void setTheHours() {
        mBinder.hourPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        if (mRightNow.get(Calendar.HOUR_OF_DAY) + 2 > 23 ||
                mRightNow.get(Calendar.HOUR_OF_DAY) + 2 < 8) {
            mBinder.hourPicker.setEnabled(false);
            mBinder.hourPicker.setMinValue(0);
            mBinder.hourPicker.setMaxValue(0);
        } else {
            mBinder.hourPicker.setEnabled(true);
            mBinder.hourPicker.setMinValue(mRightNow.get(Calendar.HOUR_OF_DAY) + 2);
            mBinder.hourPicker.setMaxValue(23);
        }
        mBinder.hourPicker.setWrapSelectorWheel(false);
        mBinder.hourPicker.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return String.format(Locale.ROOT, "%02d:00", value);
            }
        });

        //Below code so the first hour of the hourPicker is shown
        try {
            Field f = NumberPicker.class.getDeclaredField("mInputText");
            f.setAccessible(true);
            EditText inputText = (EditText) f.get(mBinder.hourPicker);
            inputText.setFilters(new InputFilter[0]);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    private void setTheDates() {
        mDates = getDates();
        mBinder.datePicker.setMinValue(0);
        mBinder.datePicker.setMaxValue(mDates.length - 1);
        mBinder.datePicker.setWrapSelectorWheel(false);
        mBinder.datePicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        mBinder.datePicker.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return mDates[value];
            }
        });
        mBinder.datePicker.setDisplayedValues(mDates);
    }

    private void changeTimeAccordingToDate() {
        mBinder.datePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if (newVal >= 1) {
                    mBinder.hourPicker.setWrapSelectorWheel(false);
                    mBinder.hourPicker.setEnabled(true);
                    mBinder.hourPicker.setMinValue(8);
                    mBinder.hourPicker.setValue(8);
                    mBinder.hourPicker.setMaxValue(23);
                } else if (newVal <= 0) {
                    if (mRightNow.get(Calendar.HOUR_OF_DAY) + 2 > 23 ||
                            mRightNow.get(Calendar.HOUR_OF_DAY) + 2 < 8) {
                        mBinder.hourPicker.setWrapSelectorWheel(false);
                        mBinder.hourPicker.setEnabled(false);
                        mBinder.hourPicker.setMinValue(0);
                        mBinder.hourPicker.setMaxValue(0);
                    } else {
                        mBinder.hourPicker.setEnabled(true);
                        mBinder.hourPicker.setMinValue(mRightNow.get(Calendar.HOUR_OF_DAY) + 2);
                        mBinder.hourPicker.setMaxValue(23);
                    }
                    mBinder.hourPicker.setWrapSelectorWheel(false);
                }
            }
        });

        mBinder.hourPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mBinder.hourPicker.setWrapSelectorWheel(false);
            }
        });
    }

    private String[] getDates() {
        Calendar c = Calendar.getInstance();
        List<String> dates = new ArrayList<>();
        DateFormat df = new SimpleDateFormat("EEE, dd MMM", Locale.getDefault());
        dates.add(df.format(c.getTime()));

        for (int i = 0; i < 30; i++) {
            c.add(Calendar.DATE, 1);
            dates.add(df.format(c.getTime()));
        }

        return dates.toArray(new String[dates.size() - 1]);
    }
}
