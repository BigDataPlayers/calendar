package com.agm.calendar.androidClient.helper;


import com.agm.calendar.androidClient.adapter.CalendarAdapter.DayCell;

/**
 * listener.
 * @author Sazonov-adm
 *
 */
public interface CalendarDatePick {
	/**
	 * @param dayCell cell.
	 */
	void onDatePicked(DayCell dayCell);
}
