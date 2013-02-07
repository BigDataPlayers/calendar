package com.agm.calendar.androidClient;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;

public class AppointmentCalendar extends Activity implements com.agm.calendar.androidClient.calendarView.CalendarView.OnClickListener {
    /**
     * Called when the activity is first created.
     */
    private Calendar cal = Calendar.getInstance();
    private int displayedMonth = cal.get(Calendar.MONTH); //current month;
    private int displayedYear = cal.get(Calendar.YEAR); //current year;
//    private boolean initialDisplayComplete = false;
    CalendarView calView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_orig);
        calView = (CalendarView) findViewById(R.id.calendarView);
        calView.setOnClickListener(this);
        calView.setShowWeekNumber(false);
//        new UpdateMonth(-1).onClick(calView);
        new UpdateMonth(1).onClick(calView);
        calView.setDate(Calendar.getInstance().getTimeInMillis());

        try {
            Class<?> cvClass = calView.getClass();
            Field field = cvClass.getDeclaredField("mMonthName");
            field.setAccessible(true);
            TextView tv = (TextView) field.get(calView);
//            tv.setTextColor(Color.rgb(1,182,92));
            tv.setTextColor(Color.RED);
            tv.setBackgroundColor(Color.YELLOW);
//            Field list = cvClass.getDeclaredField("mDayLabels");
//            list.setAccessible(true);
//            String[] lables = (String[]) list.get(calView);
//            System.out.println("Date Labels [" + lables.length + "]:" );
//            System.out.println("Date Labels [" + Arrays.asList(lables) + "]:" );

//            field = cvClass.getDeclaredField("mListView");
//            field.setAccessible(true);
            ListView lv = (ListView) field.get(calView);
            lv.setBackgroundColor(Color.RED);
            int count = lv.getChildCount();
            System.out.println("Date Labels count [" + count + "]:");
            for (int i = 0; i < count; i++) {
//            if (count >=5) lv.getChildAt(5).setBackgroundColor(Color.GREEN) ;
                System.out.println(" view at count [" + count + "]:" + lv.getChildAt(count));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        ImageButton nextButton = (ImageButton) findViewById(R.id.buttonNext);
        ImageButton prevButton = (ImageButton) findViewById(R.id.buttonPrev);
        Button todayButton = (Button) findViewById(R.id.buttonToday);

        nextButton.setOnClickListener(new UpdateMonth(1));
        prevButton.setOnClickListener(new UpdateMonth(-1));
        todayButton.setOnClickListener(new UpdateMonth(0));
        //set the first day of month in focus and call OnSelectedDayChange()


    }

    @Override
    public void onClick(View view) {
        int year, month, dayofMonth ;

        CalendarView currentView = (CalendarView) view ;

        Calendar currentCal = Calendar.getInstance()       ;
        currentCal.setTime(new Date (currentView.getDate()));
        year =  currentCal.get(Calendar.YEAR) ;
        month = currentCal.get(Calendar.MONTH) ;
        dayofMonth = currentCal.get(Calendar.DAY_OF_MONTH) ;

        System.out.println("Selected Year:" + year + " Month:" + month + " Day:" + dayofMonth);
//        if (initialDisplayComplete) {
            Intent gotoNextActivity = new Intent(getApplicationContext(), AppointmentSlotMap.class);
            startActivity(gotoNextActivity);

            //call server to get the opening slots

            //update the display with available slots
//        }
    }

    private class UpdateMonth implements View.OnClickListener {
        int monthIncr;
        Calendar monthCal = Calendar.getInstance();

        private UpdateMonth(int incr) {
            monthIncr = incr;
        }

        @Override
        public void onClick(View view) {
//            initialDisplayComplete = false;
            if (monthIncr == 0) {
                monthCal = Calendar.getInstance();
            } else {
                displayedMonth += monthIncr;

                if (displayedMonth < 0) {
                    displayedMonth += 12;
                    displayedYear -= 1;
                }
                if (displayedMonth > 11) {
                    displayedMonth -= 12;
                    displayedYear += 1;
                }
                monthCal.set(displayedYear, displayedMonth, Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
            }

            calView.setDate(monthCal.getTimeInMillis());

//            initialDisplayComplete = true;
        }
    }

}