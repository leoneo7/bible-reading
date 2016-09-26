package leoneo7.biblereading;

import java.util.Calendar;

/**
 * Created by ryouken on 2016/09/24.
 */
public class ReadLog {

    public ReadLog(long date, int chapters) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        this.date = calendar;
        this.chapters = chapters;
    }

    Calendar date;
    int chapters;

    public void setDate(Calendar date) { this.date = date; }

    public void setChapters(int chapters) { this.chapters = chapters; }

    public Calendar getDate() { return date; }

    public int getChapters() { return chapters; }

}
