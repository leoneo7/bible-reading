package leoneo7.biblereading.activity.menu;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import leoneo7.biblereading.R;
import leoneo7.biblereading.activity.main.HistoryActivity;
import leoneo7.biblereading.activity.main.ProgressActivity;

/**
 * Created by ryouken on 2016/09/25.
 */
public class AboutAppActivity extends AppCompatActivity {

    @BindView(R.id.navigation_view)
    NavigationView mNavigationView;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.tool_bar)
    Toolbar mToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_about_app);
        ButterKnife.bind(this);

        onClickMenu();
        setupToolBar();
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

    private void onClickMenu() {
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menu_home:
                        Intent intent_progress = new Intent(AboutAppActivity.this, ProgressActivity.class);
                        startActivity(intent_progress);
                        break;
//                    case R.id.menu_profile:
//                        Intent intent_profile = new Intent(AboutAppActivity.this, ProfileActivity.class);
//                        startActivity(intent_profile);
//                        break;
                    case R.id.menu_history:
                        Intent intent_history = new Intent(AboutAppActivity.this, HistoryActivity.class);
                        startActivity(intent_history);
                        break;
//                    case R.id.menu_ranking:
//                        break;
//                    case R.id.menu_notification:
//                        Intent intent_notification = new Intent(HistoryActivity.this, NotificationActivity.class);
//                        startActivity(intent_notification);
//                        break;
                    case R.id.menu_about:
                        Intent intent_about = new Intent(AboutAppActivity.this, AboutAppActivity.class);
                        startActivity(intent_about);
                        break;
//                    case R.id.menu_help:
//                        Intent intent_help = new Intent(HistoryActivity.this, HelpActivity.class);
//                        startActivity(intent_help);
//                        break;
                    case R.id.menu_setting:
                        Intent intent_setting = new Intent(AboutAppActivity.this, SettingActivity.class);
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
