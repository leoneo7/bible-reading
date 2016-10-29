package leoneo7.biblereading;

import java.util.Calendar;

/**
 * Created by ryouken on 2016/09/24.
 */
public class ReadLog {

    int id;
    Calendar date;
    int chapters;
    int sprintId;

    public ReadLog(int id, long date, int chapters, int sprintId) {
        this.id = id;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        this.date = calendar;
        this.chapters = chapters;
        this.sprintId = sprintId;
    }

    public void setDate(Calendar date) { this.date = date; }

    public void setChapters(int chapters) { this.chapters = chapters; }

    public void setSprintId(int sprintId) { this.sprintId = sprintId; }

    public int getId() { return id; }

    public Calendar getDate() { return date; }

    public int getChapters() { return chapters; }

    public int getSprintId() { return sprintId; }

}
