package com.agm.calendar.androidClient;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.*;
import android.app.Activity;
import android.os.Bundle;
import com.agm.calendar.androidClient.adapter.SlotViewAdapter;
import com.agm.calendar.androidClient.helper.CalendarConstants;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Created with IntelliJ IDEA.
 * User: a09726a
 * Date: 2/5/13
 * Time: 10:21 AM
 * To change this template use File | Settings | File Templates.
 */
public class AppointmentBooking extends Activity {

    GridView gridView;
    Button selectButton;
    String mProvider, mOffice, mVisitType;

    public AppointmentBooking() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calenderandslots);
        Bundle bundle = getIntent().getExtras();

        mProvider = bundle.getString("PROVIDER");
        mOffice = bundle.getString("OFFICE");
        mVisitType = bundle.getString("VISIT_TYPE");

        this.setTitle("Appointment for " + mVisitType + " with " + mProvider + " at " + mOffice);
        String[] slotsDesc = createSlotText();

        gridView = (GridView) findViewById(R.id.gridView_slot);
        selectButton = (Button) findViewById(R.id.slot_select);
        GridLayout gridHeader = (GridLayout) findViewById(R.id.gridView_headers);

        gridView.setVisibility(View.GONE);
        selectButton.setVisibility(Button.GONE);
        gridHeader.setVisibility(GridLayout.GONE);

        gridView.setAdapter(new SlotViewAdapter(this, slotsDesc));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
//                System.out.println("RadioButton Clicked..." + ((RadioButton) v.findViewById(R.id.grid_item_radio)).getText());
//                Toast.makeText(getApplicationContext(), ((RadioButton) v.findViewById(R.id.grid_item_radio)).getText(), Toast.LENGTH_SHORT).show();
            }
        });

        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Select button's parent:" + view.getParent());
            }
        });

        ImageButton fb = (ImageButton) findViewById(R.id.imageButton_fb);
        fb.setOnClickListener(new BrowserLauncher("https://plus.google.com/106629310080483954246/about?gl=us&hl=en%3Fgl%3Dus"));

        ImageButton yahoo = (ImageButton) findViewById(R.id.imageButton_fb);
        fb.setOnClickListener(new BrowserLauncher("http://local.yahoo.com/details?id=88607324&stx=dentist&csz=Princeton+Junction+NJ"));

        ImageButton yelp = (ImageButton) findViewById(R.id.imageButton_fb);
        fb.setOnClickListener(new BrowserLauncher("//http://www.yelp.com/biz/vsmile-dental-princeton-junction"));
    }

    private String[] createSlotText() {
        String[] slots = new String[CalendarConstants.SLOT_COUNT];
        int slotStart = CalendarConstants.SLOT_START;
        long startTime, endTime;
        String from, to;
        SimpleDateFormat format = new SimpleDateFormat("hh:mm aa");

        Calendar startCal = Calendar.getInstance();

        startCal.set(Calendar.HOUR_OF_DAY, slotStart);
        startCal.set(Calendar.MINUTE, 0);
        Date startDate = startCal.getTime();
        Date endDate = new Date();

        startTime = startCal.getTimeInMillis();

        for (int i = 0; i < CalendarConstants.SLOT_COUNT; i++) {
            endTime = startTime + (CalendarConstants.SLOT_DURATION * 60 * 1000);
            endDate.setTime(endTime);

            from = format.format(startDate);
            to = format.format(endDate);
            slots[i] = from + " - " + to;
//            System.out.println("New Slot:" + slots[i]);
            startTime = endTime;
            startDate.setTime(startTime);
        }

        return slots;
    }

    public View getParentView() {
        return findViewById(R.id.calendarandslots);
    }

    private class BrowserLauncher implements View.OnClickListener {
        String URL;
         BrowserLauncher (String url) {
            URL = url ;
        }
        @Override
        public void onClick(View view) {
            Intent browserIntent = new Intent("android.intent.action.VIEW",
                    Uri.parse(URL));
            startActivity(browserIntent);
        }
    }
}
