package com.hadiubaidillah.todo;

import java.time.ZoneId;
import java.time.ZonedDateTime;


public class Testing {

    public static void main(String[] args) {
        // Get the current time in UTC
        ZonedDateTime utcTime = ZonedDateTime.now(ZoneId.of("UTC"));

        // Convert to epoch milliseconds
        long epochMilli = utcTime.toInstant().toEpochMilli();

        System.out.println("Epoch milliseconds in UTC: " + epochMilli);
    }

}
