package com.agm.calendar.server.service;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Anukool
 * Date: 2/2/13
 * Time: 10:53 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class CalendarService {
    List<String> nameList = new LinkedList<String>();

    public void setNameList(List<String> names) {
        System.out.println("Names: " + names);
        for (String name : names) {
            nameList.add(name);
        }
    }

    public List<String> getNameList() {
        return nameList;
    }

    @Override
    public String toString() {
        StringBuffer list = new StringBuffer();
        Iterator loop = nameList.iterator();
        if (loop.hasNext()) {
            list.append(loop.next());
            while (loop.hasNext()) {
                list.append(",").append(loop.next());
            }
        }
        return list.toString();
    }

}
