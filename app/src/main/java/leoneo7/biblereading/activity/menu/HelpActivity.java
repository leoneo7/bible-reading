package leoneo7.biblereading.activity.menu;

import android.app.Activity;
import android.os.Bundle;

import butterknife.ButterKnife;
import leoneo7.biblereading.R;

/**
 * Created by ryouken on 2016/09/25.
 */
public class HelpActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_help);
        ButterKnife.bind(this);
    }
}
