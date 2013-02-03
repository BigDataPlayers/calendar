package com.agm.calendar.server.cache;

import com.bdc.container.cache.CacheManager;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Anukool
 * Date: 2/2/13
 * Time: 10:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class TimeSlots implements Serializable{
    Date slot ;
    String name;
    String provider;
    String office;
    String type;

    public TimeSlots (String off,String provd,String type, Date date){
        office = off ;
        provider = provd;
        this.type = type;
        slot = date;

        CacheManager.put("TimeSlots", getKey(), new ValueImpl(this));

    }

    public String getKey(){
        StringBuffer buffer = new StringBuffer();
        Calendar cal = Calendar.getInstance();
        cal.setTime(slot);

        buffer.append(office).append(provider).append(type)
                .append(cal.get(Calendar.YEAR))
                .append(cal.get(Calendar.MONTH))
                .append(cal.get(Calendar.DAY_OF_MONTH));

        return buffer.toString() ;
    }

    public Date getSlot() {
        return slot;
    }

    public void setSlot(Date slot) {
        this.slot = slot;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
