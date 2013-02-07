package com.agm.calendar.androidClient.calendarView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import com.agm.calendar.androidClient.R;

/**
 * Created with IntelliJ IDEA.
 * User: a09726a
 * Date: 2/6/13
 * Time: 3:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class SlotViewAdapter extends BaseAdapter {
    private Context context;
    String [] slotValues ;

    public SlotViewAdapter (Context contxt, String[] mobileValues){
        context = contxt;
        slotValues = mobileValues;
    }

    public android.view.View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView ;

//        if (convertView == null) {
            gridView = inflater.inflate(R.layout.slots, null);
            RadioButton button = (RadioButton) gridView.findViewById(R.id.grid_item_radio);
            button.setText(slotValues[position]);
            System.out.println("Completed Radio Button:" + slotValues[position]);
//        } else {
//            gridView = convertView ;
//        }

        return gridView;
    }
    public int getCount()
    {
        return slotValues.length ;
    }

    public Object getItem(int i){
        return 0;
    }

    public long getItemId(int i) {return 0;}

}
