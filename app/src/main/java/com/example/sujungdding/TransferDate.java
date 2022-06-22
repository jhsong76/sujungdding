package com.example.sujungdding;

import java.util.Calendar;

public class TransferDate {
    Calendar currtime;
    String currYear, currMonth, currDay, currHour, currMin;
    public TransferDate(){
        currtime = Calendar.getInstance();
        currYear = Integer.toString(currtime.get(Calendar.YEAR));
        currMonth = Integer.toString(currtime.get(Calendar.MONTH) + 1);
        if(currMonth.length() < 2) currMonth = "0" + currMonth;
        currDay = Integer.toString(currtime.get(Calendar.DAY_OF_MONTH));
        if(currDay.length() < 2) currDay = "0" + currDay;
        currHour = Integer.toString(currtime.get(Calendar.HOUR_OF_DAY));
        currMin = Integer.toString(currtime.get(Calendar.MINUTE));
    }

    public String TransferedDate(String date, String dateOrDue){
        String finaldate = "";
        if(dateOrDue.equals("date")) {
            String[] trans, transdate, transtime;
            trans = date.split(" ", 2);
            transdate = trans[0].split("-", 3);
            transtime = trans[1].split(":", 3);
            if (!transdate[0].equals(currYear)) {
                finaldate += transdate[0] + "/";
            }

            if (!transdate[1].equals(currMonth) || !transdate[2].equals(currDay) || !transdate[0].equals(currYear)){
                finaldate += transdate[1] + "/";
                finaldate += transdate[2] + " ";
            }

            finaldate += transtime[0] + ":" + transtime[1];
            return finaldate;
        } else if(dateOrDue.equals("due")){
            String [] transdate;
            transdate = date.split("-", 3);
            finaldate += transdate[0] + "/" + transdate[1] + "/" + transdate[2];
            return finaldate;
        }
        return null;
    }
}
