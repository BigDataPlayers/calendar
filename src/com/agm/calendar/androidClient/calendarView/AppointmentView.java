package com.agm.calendar.androidClient.calendarView;

import com.agm.calendar.androidClient.R;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.TableLayout;


/**
 * Created with IntelliJ IDEA.
 * User: a09726a
 * Date: 2/5/13
 * Time: 4:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class AppointmentView extends TableLayout {
    private GridView apptTable ;
    private ListAdapter adapter;
    private int slotCount;
    public AppointmentView(final Context context) {
        this(context, null);
    }

    /**
     * РљРѕРЅСЃС‚СЂСѓРєС‚РѕСЂ.
     *
     * @param context РєРѕРЅС‚РµРєСЃС‚
     * @param attrs Р°С‚СЂРёР±СѓС‚С‹
     */
    public AppointmentView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.slots, this, true);

        apptTable = ((GridView) findViewById(R.id.slot_gridView));
        adapter = new AppointmentsGridAdapter(inflater, apptTable);
        apptTable.setAdapter(adapter);

    }


    class AppointmentsGridAdapter implements ListAdapter {

        public AppointmentsGridAdapter (LayoutInflater inflater, GridView view){

        }

        @Override
        public int getViewTypeCount () {
            return slotCount ;
        }

        public void registerDataSetObserver(android.database.DataSetObserver dataSetObserver) {

        }

        public void unregisterDataSetObserver(android.database.DataSetObserver dataSetObserver){
            return ;
        }

        public int getCount()
        {
            return slotCount ;
        }

        public Object getItem(int i){
         return 0;
        }

        public long getItemId(int i) {return 0;}

        public boolean hasStableIds() {
            return false ;
        }

        public android.view.View getView(int i, android.view.View view, android.view.ViewGroup viewGroup) {
            return null ;
        }

        public int getItemViewType(int i) {
            return i;
        }

        public boolean isEmpty() {
            return false;
        }

        public boolean areAllItemsEnabled() {return false;}

        public boolean isEnabled(int i) {return false;}
    }
}
