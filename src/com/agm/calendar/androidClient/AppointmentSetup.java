package com.agm.calendar.androidClient;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Anukool
 * Date: 2/3/13
 * Time: 2:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class AppointmentSetup extends Activity implements View.OnClickListener {

    private static final String SERVER_URL = "http://ec2-54-243-1-47.compute-1.amazonaws.com";
    private static final String GET_PROVIDER = "PProviderList";
    private static final String GET_OFFICE = "OOfficeList";
    private static final String GET_OPTIONS = "OOptionsList";
    private String officeList, providerList, optionsList ;

    private MyListAdapter listAdapter;
    private ExpandableListView myList;


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
        myList = (ExpandableListView) findViewById(R.id.providerList);
        //create the adapter by passing your ArrayList data
        listAdapter = new MyListAdapter(this, officeList);
        //attach the adapter to the list
        myList.setAdapter(listAdapter);

    }

    private void loadData() {
        officeList = getDataFromServer (GET_OFFICE) ;
        providerList = getDataFromServer(GET_PROVIDER);
        optionsList = getDataFromServer(GET_OPTIONS);
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
        Log.i("Server call for " + command, buffer.toString());
        return buffer.toString() ;
    }

    private class MyListAdapter extends BaseExpandableListAdapter {
        private Context context;
        private ArrayList<String> officeList;

        public MyListAdapter(Context context, ArrayList<String> officeList) {
            this.context = context;
            this.officeList = officeList;
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            ArrayList<String> providerList = officeList.get(groupPosition).getProductList();
            return providerList.get(childPosition);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                                 View view, ViewGroup parent) {

            DetailInfo detailInfo = (DetailInfo) getChild(groupPosition, childPosition);
            if (view == null) {
                LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = infalInflater.inflate(R.layout.child_row, null);
            }

            TextView sequence = (TextView) view.findViewById(R.id.sequence);
            sequence.setText(detailInfo.getSequence().trim() + ") ");
            TextView childItem = (TextView) view.findViewById(R.id.childItem);
            childItem.setText(detailInfo.getName().trim());

            return view;
        }

        @Override
        public int getChildrenCount(int groupPosition) {

            ArrayList<DetailInfo> productList = officeList.get(groupPosition).getProductList();
            return productList.size();

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

            HeaderInfo headerInfo = (HeaderInfo) getGroup(groupPosition);
            if (view == null) {
                LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inf.inflate(R.layout.group_heading, null);
            }

            TextView heading = (TextView) view.findViewById(R.id.heading);
            heading.setText(headerInfo.getName().trim());

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


}
