package com.agm.calendar.androidClient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: Anukool
 * Date: 2/3/13
 * Time: 2:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class AppointmentSetup extends Activity implements View.OnClickListener {

    private static final String SERVER_URL = "http://ec2-54-243-1-47.compute-1.amazonaws.com/";
    private static final String GET_PROVIDER = "PProviderList";
    private static final String GET_OFFICE = "OOfficeList";
    private static final String GET_OPTIONS = "OOptionsList";
    private String officeNames, providerNames, optionsNames;
    private ArrayList<String> officeList, providerList, optionsList ;

    private CalendarListAdapter listAdapter;
    private ExpandableListView expandableList;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.elections);

        Spinner spinner = (Spinner) findViewById(R.id.providerType);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.provider_type, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        //get provider details from the server
        loadData();

        //get reference to the ExpandableListView
        expandableList = (ExpandableListView) findViewById(R.id.providerList);
        //create the adapter by passing your ArrayList data
        listAdapter = new CalendarListAdapter(this, officeList);
        //attach the adapter to the list
        expandableList.setAdapter(listAdapter);

        //listener for child row click
        expandableList.setOnChildClickListener(providerItemClicked);
        //listener for group heading click
        expandableList.setOnGroupClickListener(officeItemClicked);

    }

    private void loadData() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        officeNames = getDataFromServer (GET_OFFICE) ;
        providerNames = getDataFromServer(GET_PROVIDER);
        optionsNames = getDataFromServer(GET_OPTIONS);
        officeList = new ArrayList<String>(Arrays.asList(officeNames.split(" , ")));
        providerList = new ArrayList<String>(Arrays.asList(providerNames.split(" , ")));
        optionsList = new ArrayList<String>(Arrays.asList(optionsNames.split(" , ")));

        System.out.println("Sizes officeNames:" + officeList + "\tproviderNames:" + providerList + "\toptionsNames:" + optionsList);

    }

    private String getDataFromServer (String command){
        String inputLine = null;
        StringBuffer buffer = new StringBuffer();
        try {
            URL serverURL = new URL(SERVER_URL + command);
            URLConnection conn = serverURL.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while ((inputLine = in.readLine()) != null)  buffer.append(inputLine);
            in.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Log.i("Server call for " + SERVER_URL + command, buffer.toString());
        return buffer.toString() ;
    }

    private class CalendarListAdapter extends BaseExpandableListAdapter {
        private Context context;
        private ArrayList<String> officeList;

        public CalendarListAdapter(Context context, ArrayList<String> officeList) {
            this.context = context;
            this.officeList = officeList;
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            String office = officeList.get(groupPosition) ;
            ArrayList<String> providers = getProviderList(office);
            return providers.get(childPosition);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                                 View view, ViewGroup parent) {

            if (view == null) {
                LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = infalInflater.inflate(R.layout.providerrows, parent, false);
            }

            TextView sequence = (TextView) view.findViewById(R.id.sequence);
            System.out.println("childPosition: " + childPosition);
            sequence.setText(childPosition);
            TextView childItem = (TextView) view.findViewById(R.id.childItem);
            childItem.setText(providerList.get(childPosition));

            return view;
        }

        @Override
        public int getChildrenCount(int groupPosition) {

//            ArrayList<DetailInfo> productList = officeList.get(groupPosition).getProductList();
            return providerList.size();

        }

        @Override
        public Object getGroup(int groupPosition) {
            return officeList.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return officeList.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isLastChild, View view,
                                 ViewGroup parent) {

            if (view == null) {
                LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inf.inflate(R.layout.officeheaders, null);
            }

            TextView heading = (TextView) view.findViewById(R.id.heading);
            heading.setText(officeList.get(groupPosition));

            return view;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

    }

     private ArrayList<String> getProviderList (String office) {
         //TODO - revisit once the data is pulled from real data store
         return providerList;
     }


    //our child listener
    private ExpandableListView.OnChildClickListener providerItemClicked =
            new ExpandableListView.OnChildClickListener() {

        public boolean onChildClick(ExpandableListView parent, View v,
                                    int groupPosition, int childPosition, long id) {

            //get the group header
            String headerInfo = officeList.get(groupPosition);
            //get the child info
            String detailInfo =  providerList.get(childPosition);
            //display it or do something with it
            Toast.makeText(getBaseContext(), "Clicked on Detail " + headerInfo + "/" + detailInfo, Toast.LENGTH_LONG).show();
            return false;
        }
    };

    //our group listener
    private ExpandableListView.OnGroupClickListener officeItemClicked =  new ExpandableListView.OnGroupClickListener() {

        public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

            //get the group header
            String headerInfo = officeList.get(groupPosition);
            //display it or do something with it
            Toast.makeText(getBaseContext(), "Child on Header " + headerInfo, Toast.LENGTH_LONG).show();

            return false;
        }
    };

    public void onClick(View v) {

        switch (v.getId()) {

            //add entry to the List
            case R.id.select:
                Intent gotoNextActivity = new Intent(getApplicationContext(), AppointmentCalendar.class);
                startActivity(gotoNextActivity);
                break;
            // More buttons go here (if any) ...
        }
    }

}
