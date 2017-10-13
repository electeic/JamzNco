package jamilaappinc.grubmate;


import java.util.Date;

/**
 * Created by Jamila on 10/7/2017.
 */

public class DateAndTime {
    Integer mStartTimeHour;
    Integer mStartTimeMinute;
    String mStartTimeDetail;
    Integer mEndTimeHour;
    Integer mEndTimeMinute;
    String mEndTimeDetail;
    Integer mMonth;
    Integer mDay;
    Integer mYear;

    DateAndTime (Integer startTimeHour, Integer startTimeMinute, String startTimeDetail, Integer endTimeHour,
         Integer endTimeMinute, String endTimeDetail, Integer month, Integer day, Integer year){
        mStartTimeHour = startTimeHour;
        mStartTimeMinute = startTimeMinute;
        mStartTimeDetail = startTimeDetail;
        mEndTimeHour = endTimeHour;
        mEndTimeMinute = endTimeMinute;
        mEndTimeDetail = endTimeDetail;
        mMonth = month;
        mDay = day;
        mYear = year;
    }

    public Integer getmDay() { return mDay; }

    public Integer getmYear() { return mYear; }

    public Integer getmMonth() { return mMonth; }

    public void setmDay(Integer day) { mDay = day;}

    public void setmMonth (Integer month) { mMonth = month;}

    public void setmYear (Integer year) { mYear = year;}

    public Integer getmStartTimeHour() { return mStartTimeHour; }

    public Integer getmStartTimeMinute() { return mStartTimeMinute; }

    public void setmStartTimeHour(Integer startTimeHour) { mStartTimeHour = startTimeHour; }

    public void setmStartTimeMinute(Integer startTimeMinute) { mStartTimeMinute = startTimeMinute; }

    public Integer getmEndTimeHour() { return mEndTimeHour; }

    public Integer getmEndTimeMinute() { return mEndTimeMinute; }

    public void setmEndTimeHour(Integer endTimeHour) { mEndTimeHour = endTimeHour; }

    public void setmEndTimeMinute(Integer endTimeMinute) { mEndTimeMinute = endTimeMinute; }

    public String getmEndTimeDetail() {return mEndTimeDetail; }

    public void setmEndTimeDetail(String endTimeDetail){ mEndTimeDetail = endTimeDetail; }
    
    public String getmStartTimeDetail() { return mStartTimeDetail; }

    public void setmStartTimeDetail(String startTimeDetail) { mStartTimeDetail = startTimeDetail; }

    public Boolean isInTime(DateAndTime otherDate){

        if(isSameDate(otherDate)) {
            if (otherDate.getmStartTimeDetail().equals(this.getmStartTimeDetail())) {
                if (otherDate.getmStartTimeHour() < this.getmEndTimeHour()) {
                    if (otherDate.getmEndTimeHour() > this.getmStartTimeHour()) {
                        return Boolean.TRUE;
                    }
                    if (otherDate.getmEndTimeHour() == this.getmStartTimeHour()) {
                        if (otherDate.getmEndTimeMinute() >= this.getmStartTimeMinute()) {
                            return Boolean.TRUE;
                        }
                    }

                } else if (otherDate.getmStartTimeHour() == this.getmEndTimeHour()) {
                    if (otherDate.getmStartTimeMinute() < this.getmEndTimeMinute()) {
                        if (otherDate.getmEndTimeHour() > this.getmStartTimeHour()) {
                            return Boolean.TRUE;
                        }
                        if (otherDate.getmEndTimeHour() == this.getmEndTimeHour()) {
                            if (otherDate.getmEndTimeMinute() >= this.getmStartTimeMinute()) {
                                return Boolean.TRUE;
                            }
                        }
                    }
                }
            }
            if (otherDate.getmEndTimeDetail().equals(this.getmEndTimeDetail())) {
                if (otherDate.getmStartTimeHour() < this.getmEndTimeHour()) {
                    if (otherDate.getmEndTimeHour() > this.getmStartTimeHour()) {
                        return Boolean.TRUE;
                    }
                    if (otherDate.getmEndTimeHour() == this.getmStartTimeHour()) {
                        if (otherDate.getmEndTimeMinute() >= this.getmStartTimeMinute()) {
                            return Boolean.TRUE;
                        }
                    }

                } else if (otherDate.getmStartTimeHour() == this.getmEndTimeHour()) {
                    if (otherDate.getmStartTimeMinute() < this.getmEndTimeMinute()) {
                        if (otherDate.getmEndTimeHour() > this.getmStartTimeHour()) {
                            return Boolean.TRUE;
                        }
                        if (otherDate.getmEndTimeHour() == this.getmEndTimeHour()) {
                            if (otherDate.getmEndTimeMinute() >= this.getmStartTimeMinute()) {
                                return Boolean.TRUE;
                            }
                        }
                    }
                }
            }
        }
        return Boolean.FALSE;
    }

    public Boolean isSameDate(DateAndTime otherDate){
        if(otherDate.getmMonth().equals(this.getmMonth())){
            if(otherDate.getmDay() == this.getmDay()){
                if(otherDate.getmYear() == this.getmYear()){
                    return Boolean.TRUE;
                }
            }
        }
        return Boolean.FALSE;

    }

    public boolean isExpired(){
        Date currentDate = new Date();
        int currentMinute = currentDate.getMinutes();
        int currentHour = currentDate.getHours();
        int currentDay = currentDate.getDay();
        int currentMonth = currentDate.getMonth();
        int currentYear = currentDate.getYear();

        if(currentYear == this.getmYear()){
            if(currentMonth == this.getmMonth()){
                if(currentDay == this.getmDay()){
                    if(getmEndTimeDetail() == "pm"){
                       currentHour -= 12;
                        if(currentHour < getmEndTimeHour()){
                            return Boolean.FALSE;
                        }
                        if(currentHour == getmEndTimeHour()){
                            if(currentMinute < getmEndTimeMinute()) {
                                return Boolean.FALSE;
                            }
                        }
                        return Boolean.TRUE;
                    }
                    else{
                        if(currentHour < getmEndTimeHour()){
                            return Boolean.FALSE;
                        }
                        if(currentHour == getmEndTimeHour()){
                            if(currentMinute < getmEndTimeMinute()) {
                                return Boolean.FALSE;
                            }
                        }
                        return Boolean.TRUE;

                    }
                }
            }
        }
        return Boolean.TRUE;

    }

    public boolean hasStarted(){
        Date currentDate = new Date();
        int currentMinute = currentDate.getMinutes();
        int currentHour = currentDate.getHours();
        int currentDay = currentDate.getDay();
        int currentMonth = currentDate.getMonth();
        int currentYear = currentDate.getYear();


        if(currentYear == this.getmYear()){
            if(currentMonth == this.getmMonth()){
                if(currentDay == this.getmDay()){
                    if(getmEndTimeDetail() == "pm"){
                        currentHour -= 12;
                        if(currentHour > getmStartTimeHour()){
                            return Boolean.TRUE;
                        }
                        if(currentHour == getmStartTimeHour()){
                            if(currentMinute > getmStartTimeMinute()) {
                                return Boolean.TRUE;
                            }
                        }
                        return Boolean.FALSE;
                    }
                    else{
                        if(currentHour > getmStartTimeHour()){
                            return Boolean.TRUE;
                        }
                        if(currentHour == getmStartTimeHour()){
                            if(currentMinute > getmStartTimeMinute()) {
                                return Boolean.TRUE;
                            }
                        }
                        return Boolean.FALSE;

                    }
                }
            }
        }
        return Boolean.FALSE;

    }
}