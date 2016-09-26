package leoneo7.biblereading.activity.main;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.MenuItem;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import leoneo7.biblereading.R;
import leoneo7.biblereading.UserPref;
import leoneo7.biblereading.activity.menu.AboutAppActivity;
import leoneo7.biblereading.activity.menu.HelpActivity;
import leoneo7.biblereading.activity.menu.NotificationActivity;
import leoneo7.biblereading.activity.menu.SettingActivity;
import leoneo7.biblereading.helper.DBAdapter;

/**
 * Created by ryouken on 2016/09/13.
 */
public class ProgressActivity extends AppCompatActivity {

    @BindView(R.id.navigation_view)
    NavigationView mNavigationView;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.tool_bar)
    Toolbar mToolBar;

    @BindView(R.id.pie_chart)
    PieChart mPieChart;

    @BindView(R.id.minus_button)
    ImageButton mMinusButton;

    @BindView(R.id.plus_button)
    ImageButton mPlusButton;

    @BindView(R.id.read_chapter)
    TextView mReadText;

    @BindView(R.id.achieve_button)
    BootstrapButton mAchieveButton;

    @BindView(R.id.daily_read)
    TextView mDailyRead;

    @BindView(R.id.exp_progress)
    ProgressBar mProgressBar;

    @BindView(R.id.level_text)
    TextView mLevelText;

    private DBAdapter dbAdapter = new DBAdapter(this);
    private UserPref mUser = UserPref.getInstance();
    private int mDoneChapters = 0;
    private int mWeekTargetChapters = mUser.getTargetChapter(this) * 7;
    private int mReadNumber = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_progress);

        ButterKnife.bind(this);

        onClickMenu();
        setupToolBar();
        setupPieChartView();
        setupText();
        setupProgressBar();
        setupLevel();
    }

    private void setupToolBar() {
        setSupportActionBar(mToolBar);

        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeAsUpIndicator(R.drawable.xbox_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);

        mToolBar.setBackgroundColor(Color.rgb(70, 70, 70));
        mToolBar.setTitle("MENU");
        mToolBar.setTitleTextColor(Color.WHITE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupPieChartView() {
        int leftChapters = mWeekTargetChapters - mDoneChapters;

        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(mDoneChapters, 0));
        entries.add(new PieEntry(leftChapters, 1));

        PieDataSet dataSet = new PieDataSet(entries, "");

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(ContextCompat.getColor(this, R.color.twitter));
        colors.add(Color.argb(100, 180, 180, 180));
        dataSet.setColors(colors);

        PieData pieData = new PieData(dataSet);
        pieData.setDrawValues(false);

        mPieChart.setDescription("");
        mPieChart.setRotationEnabled(false);
        mPieChart.getLegend().setEnabled(false);
        mPieChart.setHoleRadius(60f);
        mPieChart.setCenterText(generateCenterSpannableText(leftChapters));
        mPieChart.animateY(2000, Easing.EasingOption.EaseInQuart);
        mPieChart.setData(pieData);
    }

    private SpannableString generateCenterSpannableText(int leftChapters) {
        SpannableString s = new SpannableString("残り\n" + ((Integer) leftChapters).toString() + "章");
        s.setSpan(new RelativeSizeSpan(4.5f), 3, 5, 0);
        s.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), 3, 5, 0);
        s.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.twitter)), 3, 5, 0);
        return s;
    }

    private void setupText() {
        mReadText.setText(String.valueOf(mReadNumber));
        if (mReadNumber >= mUser.getTargetChapter(this)) {
            mReadText.setTextColor(Color.rgb(0, 150, 0));
            mAchieveButton.setText(R.string.achieve_button);
        } else {
            mReadText.setTextColor(Color.RED);
            mAchieveButton.setText(R.string.save_button);
        }
    }

    private void setupLevel() {
        mLevelText.setText(String.valueOf(mUser.getLevel(this)));
    }

    private void setupProgressBar() {
        mProgressBar.setMax(mUser.getLevel(this) * 500);
        ObjectAnimator animation = ObjectAnimator.ofInt(mProgressBar, "progress", mUser.getEXP(this));
        animation.setDuration(500);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();
    }

    @OnClick(R.id.minus_button)
    public void onMinusButton() {
        if (mReadNumber == 0) return;
        mReadNumber --;
        setupText();
    }

    @OnClick(R.id.plus_button)
    public void onPlusButton() {
        mReadNumber ++;
        setupText();
    }

    @OnClick(R.id.achieve_button)
    public void onAchieveButton() {
        if (mReadNumber == 0) return;
        int point = mUser.setRandomEXP(mReadNumber);
        mUser.addEXP(this, point);
        mDoneChapters += mReadNumber;
        saveLog();
        setupPieChartView();
        setupProgressBar();
        setupLevel();
        String message = "+" + ((Integer) point).toString() + "EXP";
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    protected void saveLog(){
        dbAdapter.open();
        dbAdapter.saveLog(Calendar.getInstance().getTimeInMillis(), mReadNumber);
        dbAdapter.close();
    }

    private void onClickMenu() {
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menu_home:
                        Intent intent_progress = new Intent(ProgressActivity.this, ProgressActivity.class);
                        startActivity(intent_progress);
                        break;
                    case R.id.menu_profile:
                        break;
                    case R.id.menu_history:
                        Intent intent_history = new Intent(ProgressActivity.this, HistoryActivity.class);
                        startActivity(intent_history);
                        break;
                    case R.id.menu_ranking:
                        break;
                    case R.id.menu_notification:
                        Intent intent_notification = new Intent(ProgressActivity.this, NotificationActivity.class);
                        startActivity(intent_notification);
                        break;
                    case R.id.menu_about:
                        Intent intent_about = new Intent(ProgressActivity.this, AboutAppActivity.class);
                        startActivity(intent_about);
                        break;
                    case R.id.menu_help:
                        Intent intent_help = new Intent(ProgressActivity.this, HelpActivity.class);
                        startActivity(intent_help);
                        break;
                    case R.id.menu_setting:
                        Intent intent_setting = new Intent(ProgressActivity.this, SettingActivity.class);
                        startActivity(intent_setting);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

}
