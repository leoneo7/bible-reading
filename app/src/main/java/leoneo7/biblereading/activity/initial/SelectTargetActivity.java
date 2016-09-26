package leoneo7.biblereading.activity.initial;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.SeekBar;
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
public class SelectTargetActivity extends Activity {

    @BindView(R.id.target_message)
    TextView mMessage;

    @BindView(R.id.target_progress)
    SeekBar mSeekBar;

    @BindView(R.id.seekbar_number)
    TextView mSeekBarNumberText;

    @BindView(R.id.take_days_text)
    TextView mTakeDaysText;

    @BindView(R.id.next_button)
    BootstrapButton mNextButton;

    private double mTakeDays = 0;
    private UserPref mUser = UserPref.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.initial_select_target);
        ButterKnife.bind(this);

        seekBarEvent();
    }

    @OnClick(R.id.next_button)
    public void startButtonClicked() {
        if (mSeekBar.getProgress() == 0) {
            return;
        } else {
            mUser.setTargetChapter(this, mSeekBar.getProgress());
        }
        Intent intent = new Intent(this, SelectWhenActivity.class);
        startActivity(intent);
    }

    private void seekBarEvent() {
        setSeekBarText();
        setTakeDaysText();

        mSeekBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        setSeekBarText();
                        setTakeDaysText();
                    }
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        setSeekBarText();
                        setTakeDaysText();
                    }
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        setSeekBarText();
                        setTakeDaysText();
                    }
                }
        );
    }

    private void setSeekBarText() {
        mSeekBarNumberText.setText(String.valueOf(mSeekBar.getProgress()) + "章");
    }

    private void setTakeDaysText() {
        if (mSeekBar.getProgress() == 0) {
            mTakeDaysText.setText(R.string.please_set_target);
        } else {
            mTakeDays = Math.ceil((double) mUser.getBookChapter(this) / (double)mSeekBar.getProgress());
            mTakeDaysText.setText("そのペースだと\n" + String.valueOf((int) mTakeDays) + "日かかります");
            mUser.setTakeDays(this, (int) mTakeDays);
        }
    }
}
