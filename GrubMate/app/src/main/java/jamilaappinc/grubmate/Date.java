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

    Date(Integer startTimeHour, Integer startTimeMinute, String startTimeDetail, Integer endTimeHour,
         Integer endTimeMinute, String endTimeDetail){
        mStartTimeHour = startTimeHour;
        mStartTimeMinute = startTimeMinute;
        mStartTimeDetail = startTimeDetail;
        mEndTimeHour = endTimeHour;
        mEndTimeMinute = endTimeMinute;
        mEndTimeDetail = endTimeDetail;
    }

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
        if(otherDate.getmStartTimeDetail().equals(this.getmStartTimeDetail())){
            if(otherDate.getmStartTimeHour() < this.getmEndTimeHour()){
                if(otherDate.getmEndTimeHour() > this.getmStartTimeHour()){
                    return TRUE;
                }

            }
            else if(otherDate.getmStartTimeHour() == this.getmEndTimeHour()){
                
            }
        }
        if(otherDate.getmEndTimeDetail().equals(this.getmEndTimeDetail())){

        }
        return FALSE;
    }
}
