package jamilaappinc.grubmate;

/**
 * Created by Jamila on 10/7/2017.
 */

public class Date {
    String mStartTime;
    String mStartTimeDetail;
    String mEndTime;
    String mEndTimeDetail;

    Date(String startTime, String startTimeDetail, String endTime, String endTimeDetail){
        mStartTime = startTime;
        mStartTimeDetail = startTimeDetail;
        mEndTime = endTime;
        mEndTimeDetail = endTimeDetail;
    }

    public String getmStartTime() { return mStartTime; }

    public void setmStartTime(String startTime) { mStartTime = startTime; }

    public String getmEndTime() { return mEndTime; }

    public void setmEndTime(String endTime) { mEndTime = endTime; }

    public String getmEndTimeDetail() {return mEndTimeDetail; }

    public void setmEndTimeDetail(String endTimeDetail){ mEndTimeDetail = endTimeDetail; }
    
    public String getmStartTimeDetail() { return mStartTimeDetail; }

    public void setmStartTimeDetail(String startTimeDetail) { mStartTimeDetail = startTimeDetail; }

    public Boolean isInTime(Date otherDate){
        if(otherDate.)
    }
}
