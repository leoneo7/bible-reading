package leoneo7.biblereading.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

/**
 * Created by ryouken on 2016/09/17.
 */
public class AlertDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle("タイトル")
                .setMessage("メッセージ")
                .create();
    }

    @Override
    public void onPause() {
        super.onPause();

        dismiss();
    }
}
