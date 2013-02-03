package com.agm.calendar.server;

import com.agm.calendar.server.service.CalendarService;
import com.bdc.container.bootstrap.Server;
import com.bdc.container.webservice.ResultProcessor;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: Anukool
 * Date: 2/2/13
 * Time: 9:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class CalendarServer implements ResultProcessor {
    public String getResult(String input) {
        CalendarService service ;
        System.out.println("Input: " + input);
        try {
            service = (CalendarService) Server.getInstance().getContext().getBean(input);
        }
        catch (Exception ex) {
//                System.out.println("Service not found");
                return "<HTML>Invalid input URL verb: " + input + "</HTML>";
            }
        return service.toString();
    }

    public File getFile(String aid) {
        return null;
    }
}
