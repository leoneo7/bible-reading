package leoneo7.biblereading;

import java.util.Calendar;

/**
 * Created by ryouken on 2016/10/02.
 */
public class SprintLog {

    int sprintId;
    Calendar startDate;
    Calendar endDate;

    public SprintLog(int sprintId, long startDate, long endDate) {
        this.sprintId = sprintId;

        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(startDate);
        this.startDate = calendar1;

        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(endDate);
        this.endDate = calendar2;
    }

    public Calendar getStartDate() {
        return startDate;
    }

    public void setStartDate(Calendar startDate) {
        this.startDate = startDate;
    }

    public Calendar getEndDate() {
        return endDate;
    }

    public void setEndDate(Calendar endDate) {
        this.endDate = endDate;
    }

    public int getSprintId() {return sprintId;}
}
