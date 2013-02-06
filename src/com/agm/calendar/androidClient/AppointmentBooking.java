package com.agm.calendar.androidClient;

import android.view.View;
import android.widget.*;
import android.app.Activity;
import android.os.Bundle;
import com.agm.calendar.androidClient.calendarView.CalendarConstants;
import com.agm.calendar.androidClient.calendarView.SlotViewAdapter;


/**
 * Created with IntelliJ IDEA.
 * User: a09726a
 * Date: 2/5/13
 * Time: 10:21 AM
 * To change this template use File | Settings | File Templates.
 */
public class AppointmentBooking extends Activity {

    GridView gridView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calenderandslots);
        String [] slotsDesc = createSlotText();

//        gridView = (GridView) findViewById(R.id.gridView);
//
//        gridView.setAdapter(new SlotViewAdapter(this, slotsDesc));
//
//        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            public void onItemClick(AdapterView<?> parent, View v,
//                                    int position, long id) {
//                Toast.makeText(
//                        getApplicationContext(),
//                        ((RadioButton) v.findViewById(R.id.grid_item_radio))
//                                .getText(), Toast.LENGTH_SHORT).show();
//
//            }
//        });

    }

    private String[] createSlotText (){
        String [] slots = new String[CalendarConstants.SLOT_COUNT];

        for (int i=0; i <=CalendarConstants.SLOT_COUNT; i++ ){
            slots[i] = "Slot #" + (i = 1) ;
        }

        return slots;
    }
}
