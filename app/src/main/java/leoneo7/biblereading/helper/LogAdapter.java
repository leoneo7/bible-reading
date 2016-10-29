package leoneo7.biblereading.helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import leoneo7.biblereading.R;
import leoneo7.biblereading.ReadLog;

/**
 * Created by ryouken on 2016/09/24.
 */
public class LogAdapter extends BaseAdapter {

    Context context;
    LayoutInflater layoutInflater = null;
    ArrayList<ReadLog> mReadLogList;

    public LogAdapter(Context context) {
        this.context = context;
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setLogList(ArrayList<ReadLog> readLog) {
        this.mReadLogList = readLog;
    }

    @Override
    public int getCount() {
        return mReadLogList.size();
    }

    @Override
    public Object getItem(int position) {
        return mReadLogList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.log_list, parent, false);

        int month = mReadLogList.get(position).getDate().get(Calendar.MONTH);
        int day = mReadLogList.get(position).getDate().get(Calendar.DATE);
        int week_day = mReadLogList.get(position).getDate().DAY_OF_WEEK;
        String[] week_day_string = {"(月)", "(火)", "(水)", "(木)", "(金)", "(土)", "(日)"};
        String date = String.format("%d月%d日%s", month+1, day, week_day_string[week_day-1]);
        String chapters = String.valueOf(mReadLogList.get(position).getChapters()) + "章";

        ((TextView)convertView.findViewById(R.id.logDate)).setText(date);
        ((TextView)convertView.findViewById(R.id.chapters)).setText(chapters);

        return convertView;
    }
}