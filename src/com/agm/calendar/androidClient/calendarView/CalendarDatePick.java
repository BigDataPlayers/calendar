package com.agm.calendar.androidClient.calendarView;


import com.agm.calendar.androidClient.calendarView.CalendarAdapter.DayCell;

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
