package leoneo7.biblereading;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Calendar;
import java.util.Random;

/**
 * Created by ryouken on 2016/09/14.
 */
public class UserPref {
    private static UserPref ourInstance = new UserPref();

    public static UserPref getInstance() {
        return ourInstance;
    }

    private UserPref() {
    }

    private static final String USER_NAME = "user_name";
    private static final String BOOK_CHAPTER = "book_chapter";
    private static final String TARGET_CHAPTER = "target_chapter";
    private static final String TAKE_DAYS = "take_days";
    private static final String START_DATE = "start_date";
    private static final String CURRENT_SPRINT_ID = "current_sprint_id";
    private static final String LEVEL = "level";
    private static final String EXP = "exp";
    private static final String DONE_CHAPTERS = "done_chapters";
    private static final String RUNNING_DAYS = "running_days";
    private static final String IS_STARTED = "is_started";

    public void setUserName(Context context, String userName) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(USER_NAME, userName);
        editor.commit();
    }

    public void setBookChapter(Context context, int bookChapter) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(BOOK_CHAPTER, bookChapter);
        editor.commit();
    }

    public void setTargetChapter(Context context, int targetChapter) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(TARGET_CHAPTER, targetChapter);
        editor.commit();
    }

    public void setTakeDays(Context context, int takeDays) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(TAKE_DAYS, takeDays);
        editor.commit();
    }

    public void setStartDate(Context context, Calendar calendar) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putLong(START_DATE, calendar.getTimeInMillis());
        editor.commit();
    }

    public void setLevel(Context context, int level) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(LEVEL, level);
        editor.commit();
    }

    public void setEXP(Context context, int exp) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(EXP, exp);
        editor.commit();
    }

    public void setDoneChapters(Context context, int doneChapters) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(DONE_CHAPTERS, doneChapters);
        editor.commit();
    }

    public void setIsStarted(Context context, boolean isStarted) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(IS_STARTED, isStarted);
        editor.commit();
    }

    public void setCurrentSprintId(Context context, int sprintId) {
        Log.d("setCurrentSprintId", String.valueOf(sprintId));
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(CURRENT_SPRINT_ID, sprintId);
        editor.commit();
    }

    public void addEXP(Context context, int point) {
        int exp = getEXP(context);
        int level = getLevel(context);
        exp += point;
        while (exp >= level * 500) {
            exp -= level * 500;
            levelUp(context, level);
        }
        setEXP(context, exp);
    }

    private void levelUp(Context context, int level) {
        level ++;
        setLevel(context, level);
    }

    public int setRandomEXP(int readNumber) {
        int sum = 0;
        for (int i = 0; i < readNumber; i++) {
            sum += new Random().nextInt(50) +51;
        }
        return sum;
    }

//    public void manageRunningDays(boolean isSuccess) {
//        if(isSuccess) {
//            runningDays ++;
//        } else {
//            runningDays = 0;
//        }
//    }

//    public void bonusEXP(int runningDays) {
//        switch (runningDays) {
//            case 3:   addEXP(500);   break;
//            case 7:   addEXP(1000);  break;
//            case 21:  addEXP(3000);  break;
//            case 40:  addEXP(5000);  break;
//            case 70:  addEXP(10000); break;
//            case 100: addEXP(20000); break;
//            case 210: addEXP(30000); break;
//        }
//    }

    public String getUserName(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getString(USER_NAME, "");
    }

    public int getBookChapter(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getInt(BOOK_CHAPTER, 0);
    }

    public int getTargetChapter(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getInt(TARGET_CHAPTER, 0);
    }

    public int getTakeDays(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getInt(TAKE_DAYS, 0);
    }

    public long getStartDate(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getLong(START_DATE, 0);
    }

    public int getLevel(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getInt(LEVEL, 1);
    }

    public int getEXP(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getInt(EXP, 0);
    }

    public int getDoneChapters(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getInt(DONE_CHAPTERS, 0);
    }

    public boolean getIsStarted(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getBoolean(IS_STARTED, false);
    }

    public int getCurrentSprintId(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getInt(CURRENT_SPRINT_ID, 1000);
    }
}

