package jamilaappinc.grubmate;

/**
 * Created by Jamila on 10/7/2017.
 */

public class Date {
    Integer mStartTimeHour;
    Integer mStartTimeMinute;
    String mStartTimeDetail;
    Integer mEndTimeHour;
    Integer mEndTimeMinute;
    String mEndTimeDetail;
    String mMonth;
    Integer mDay;
    Integer mYear;

    Date(Integer startTimeHour, Integer startTimeMinute, String startTimeDetail, Integer endTimeHour,
         Integer endTimeMinute, String endTimeDetail, String month, Integer day, Integer year){
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

    public String getmMonth() { return mMonth; }

    public void setmDay(Integer day) { mDay = day;}

    public void setmMonth (String month) { mMonth = month;}

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

    public Boolean isInTime(Date otherDate){

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

    public Boolean isSameDate(Date otherDate){
        if(otherDate.getmMonth().equals(this.getmMonth())){
            if(otherDate.getmDay() == this.getmDay()){
                if(otherDate.getmYear() == this.getmYear()){
                    return Boolean.TRUE;
                }
            }
        }
        return Boolean.FALSE;

    }
}
