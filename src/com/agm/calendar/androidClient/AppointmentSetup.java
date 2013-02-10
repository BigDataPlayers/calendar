package com.agm.calendar.androidClient;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Layout;
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
import java.util.Arrays;
import java.util.Iterator;

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
    private ArrayList<String> officeList, providerList, optionsList;

    private CalendarListAdapter listAdapter;
    private ExpandableListView expandableList;
    private ListView listV;
    private String mProvider;
    private String mOffice;
    private String mVisitType ;
    LinearLayout lastProvider;
    RelativeLayout lastOffice ;
    TextView lastVisit, visitLabel;
    Button selectButton ;

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
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0 ){
                    expandableList.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });

//        spinner.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Spinner spnr = (Spinner) view;
//                if (spnr.getSelectedItemPosition() > 0 ){
//                    expandableList.setVisibility(View.VISIBLE);
//                }
//            }
//        });
        loadData();

        //get reference to the ExpandableListView
        expandableList = (ExpandableListView) findViewById(R.id.providerList);
        //create the adapter by passing your ArrayList data
        listAdapter = new CalendarListAdapter(this, providerList);
        //attach the adapter to the list
        expandableList.setAdapter(listAdapter);
        //listener for child row click
        expandableList.setOnChildClickListener(providerItemClicked);
        //listener for group heading click
        expandableList.setOnGroupClickListener(officeItemClicked);
        expandableList.setVisibility(View.GONE);

        //prepare listview for options
        visitLabel = (TextView) findViewById(R.id.textView_visitType);
        visitLabel.setVisibility(TextView.GONE);

        listV = (ListView) findViewById(R.id.optionList);
        listV.setAdapter(new ArrayAdapter<String>(this, R.layout.visitoptions, optionsList));
        listV.setTextFilterEnabled(true);
        listV.setOnItemClickListener(new ListListener());
        listV.setVisibility(View.GONE);

        selectButton = (Button)findViewById(R.id.select_options);
        selectButton.setOnClickListener(new SelectListener());
        selectButton.setVisibility(Button.GONE);

    }

    public String getVisitType() {
        return mVisitType;
    }

    public void setVisitType(String mVisitType) {
        this.mVisitType = mVisitType;
    }

    public String getProvider() {
        return mProvider;
    }

    public void setProvider(String mProvider) {
        this.mProvider = mProvider;
    }

    public String getOffice() {
        return mOffice;
    }

    public void setOffice(String mOffice) {
        this.mOffice = mOffice;
    }

    private void loadData() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        officeNames = getDataFromServer(GET_OFFICE);
        providerNames = getDataFromServer(GET_PROVIDER);
        optionsNames = getDataFromServer(GET_OPTIONS);
        officeList = new ArrayList<String>(Arrays.asList(officeNames.split(",")));
        providerList = new ArrayList<String>(Arrays.asList(providerNames.split(",")));
        optionsList = new ArrayList<String>(Arrays.asList(optionsNames.split(",")));

    }

    private String getDataFromServer(String command) {
        String inputLine = null;
        StringBuffer buffer = new StringBuffer();
        try {
            URL serverURL = new URL(SERVER_URL + command);
            URLConnection conn = serverURL.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while ((inputLine = in.readLine()) != null) buffer.append(inputLine);
            in.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Log.i("Server call for " + SERVER_URL + command, buffer.toString());
        return buffer.toString();
    }

    private class CalendarListAdapter extends BaseExpandableListAdapter {
        private Context context;
        private ArrayList<String> ddProviderList;

        public CalendarListAdapter(Context context, ArrayList<String> list) {
            this.context = context;
            this.ddProviderList = list;
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            String office = ddProviderList.get(groupPosition);
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
            sequence.setText((childPosition + 1) + ">\t");
            TextView childItem = (TextView) view.findViewById(R.id.childItem);
            childItem.setText(officeList.get(childPosition));

            return view;
        }

        @Override
        public int getChildrenCount(int groupPosition) {

//            ArrayList<DetailInfo> productList = ddProviderList.get(groupPosition).getProductList();
            return officeList.size();

        }

        @Override
        public Object getGroup(int groupPosition) {
            return ddProviderList.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return ddProviderList.size();
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
            heading.setText(ddProviderList.get(groupPosition));

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

    private ArrayList<String> getProviderList(String office) {
        //TODO - revisit once the data is pulled from real data store
        return officeList;
    }


    //our child listener
    private ExpandableListView.OnChildClickListener providerItemClicked =
            new ExpandableListView.OnChildClickListener() {

                public boolean onChildClick(ExpandableListView parent, View v,
                                            int groupPosition, int childPosition, long id) {

                    //get the group header
                    mProvider = providerList.get(groupPosition);
                    //get the child info
                    mOffice = officeList.get(childPosition);
                    //display it or do something with it
                    if (lastOffice != null) {
                        lastOffice.setBackgroundColor(Color.TRANSPARENT);
                    }
                    lastOffice = (RelativeLayout) v;
                    lastOffice.setBackgroundColor(Color.BLUE);
                    Toast.makeText(getBaseContext(), "Clicked on Detail " + mProvider + "/" + mOffice, Toast.LENGTH_LONG).show();

                    visitLabel.setVisibility(TextView.VISIBLE);
                    listV.setVisibility(View.VISIBLE);

                    return false;
                }
            };

    //our group listener
    private ExpandableListView.OnGroupClickListener officeItemClicked = new ExpandableListView.OnGroupClickListener() {

        public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

            //get the group header
            mProvider = providerList.get(groupPosition);
            if (lastProvider != null) {
                lastProvider.setBackgroundColor(Color.TRANSPARENT);
            }
            lastProvider = (LinearLayout) v ;
            lastProvider.setBackgroundColor(Color.BLUE);
            //display it or do something with it
            Toast.makeText(getBaseContext(), "Child on Header " + mProvider, Toast.LENGTH_LONG).show();
            return false;
        }
    };

    public void onClick(View v) {

    }

    private class SelectListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (mProvider == null || mOffice == null || mVisitType == null) {
                Toast.makeText(getApplicationContext(),
                        "Provider, Office or Visit Type is not selected. Please select and click selectButton button",
                        Toast.LENGTH_LONG).show();
            } else {
                Intent nextActivity = new Intent(getApplicationContext(), AppointmentBooking.class);
                nextActivity.putExtra("PROVIDER", mProvider);
                nextActivity.putExtra("OFFICE", mOffice);
                nextActivity.putExtra("VISIT_TYPE", mVisitType);
                startActivity(nextActivity);
            }
        }
    }

    private class ListListener implements AdapterView.OnItemClickListener {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // When clicked, show a toast with the TextView text
            if (lastVisit !=null ) {
                lastVisit.setBackgroundColor(Color.TRANSPARENT);
            }
            lastVisit = (TextView) view;
            mVisitType = (String) lastVisit.getText()  ;
            lastVisit.setBackgroundColor(Color.BLUE);

            Toast.makeText(getApplicationContext(), mVisitType, Toast.LENGTH_SHORT).show();
//                #33050a

            selectButton.setVisibility(Button.VISIBLE);
        }
    }
}
