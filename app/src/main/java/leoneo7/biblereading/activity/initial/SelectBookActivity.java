package leoneo7.biblereading.activity.initial;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import leoneo7.biblereading.R;
import leoneo7.biblereading.UserPref;

/**
 * Created by ryouken on 2016/09/13.
 */
public class SelectBookActivity extends Activity {
    @BindView(R.id.book_message)
    TextView mMessage;
    @BindView(R.id.new_button)
    BootstrapButton mNewButton;
    @BindView(R.id.both_button)
    BootstrapButton mBothButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.initial_select_book);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.new_button)
    public void newButtonClicked() {
        Intent intent = new Intent(this, SelectTargetActivity.class);
        UserPref.getInstance().setBookChapter(this, 260);
        startActivity(intent);
    }

    @OnClick(R.id.both_button)
    public void bothButtonClicked() {
        Intent intent = new Intent(this, SelectTargetActivity.class);
        UserPref.getInstance().setBookChapter(this, 1189);
        startActivity(intent);
    }

}
