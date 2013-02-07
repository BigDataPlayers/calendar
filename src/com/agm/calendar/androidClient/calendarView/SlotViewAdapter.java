package com.agm.calendar.androidClient.calendarView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.Toast;
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
    String[] slotValues;
    int mSelectedPosition = -1;
    RadioButton mSelectedRB = null ;

    public SlotViewAdapter(Context contxt, String[] mobileValues) {
        context = contxt;
        slotValues = mobileValues;
    }

    public android.view.View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

//        View gridView ;

//        if (convertView == null) {
        View gridView = inflater.inflate(R.layout.timeslot, null);
        RadioButton button = (RadioButton) gridView.findViewById(R.id.grid_item_radio);
        button.setText(slotValues[position]);
        button.setId(position);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RadioButton rb = (RadioButton) view;
                System.out.println("RadioButton Clicked..." + rb.getText());
                Toast.makeText(context, rb.getText(), Toast.LENGTH_SHORT).show();
                        if ((position != mSelectedPosition)  && mSelectedRB != null) {
                            mSelectedRB.setChecked(false);
                        }
                        mSelectedPosition = position;
                        mSelectedRB = (RadioButton) view;
                    }
                });
//        System.out.println("Completed Radio Button:" + slotValues[position]);
//        } else {
//            gridView = convertView ;
//        }

        return gridView;
    }

    public int getCount() {
        return slotValues.length;
    }

    public Object getItem(int i) {
        return 0;
    }

    public long getItemId(int i) {
        return 0;
    }

}
