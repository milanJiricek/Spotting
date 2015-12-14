package app.majlant.spotting.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import app.majlant.spotting.R;
import app.majlant.spotting.app.Const;
import app.majlant.spotting.interfaces.VolleyResponse;
import app.majlant.spotting.requests.ForgotPassRequests;
import app.majlant.spotting.utils.Utils;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by majlant on 17.11.15.
 *
 * @author Milan Jiricek <milan.jiricekpv@gmail.com></milan.jiricekpv@gmail.com>
 */
public class ForgotPassDialogFragment extends DialogFragment {
    public static final String TAG = "ForgotPassDialogFragment";
    private static final String PUT_VISIBLE_KEY = "putIsVisibleKey";
    //init view
    @Bind(R.id.imgProgress)
    ImageView imgProgress;

    @Bind(R.id.btnSendMail)
    Button btnSendMail;

    @Bind(R.id.btnClose)
    Button btnClose;

    @Bind(R.id.editNick)
    EditText editNick;

    @Bind(R.id.editMail)
    EditText editMail;

    private View view;
    private boolean isVisibleProgress = false;

    public static ForgotPassDialogFragment newInstance() {
        return new ForgotPassDialogFragment();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();

        view = inflater.inflate(R.layout.dialog_fragment_forgot_pass, null);
        ButterKnife.bind(this, view);

        if (savedInstanceState != null) {
            isVisibleProgress = savedInstanceState.getBoolean(PUT_VISIBLE_KEY);
            if (isVisibleProgress) {
                Animation fadeIn = AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_in);
                imgProgress.startAnimation(fadeIn);
                imgProgress.setVisibility(View.VISIBLE);

                imgProgress.setBackgroundResource(R.drawable.horizontal_progress_bar);
                AnimationDrawable frameAnimation = (AnimationDrawable) imgProgress.getBackground();
                frameAnimation.start();
            }
        }

        initBtnCloseEvent();
        initBtnSendMailEvent();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        return builder.create();
    }

    private void initBtnSendMailEvent() {
        btnSendMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                String nick = editNick.getText().toString();
                String mail = editMail.getText().toString();

                // Check for empty data in the form editNick
                if (nick.isEmpty()) {
                    editNick.setError(getResources().getString(R.string.spotting_log_edittext_error_field));
                    return;
                } else {
                    editNick.setError(null);
                }

                // Check for empty data in the form editMail
                if (mail.isEmpty()) {
                    editMail.setError(getResources().getString(R.string.spotting_log_edittext_error_field));
                    return;
                } else {
                    editMail.setError(null);
                }

                //Check connection
                if (!Utils.haveInternet(getActivity())) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.spotting_check_connection), Toast.LENGTH_LONG).show();
                    return;
                }

                Animation fadeIn = AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_in);
                imgProgress.startAnimation(fadeIn);
                imgProgress.setVisibility(View.VISIBLE);

                imgProgress.setBackgroundResource(R.drawable.horizontal_progress_bar);
                AnimationDrawable frameAnimation = (AnimationDrawable) imgProgress.getBackground();
                //frameAnimation.start();
                //isVisibleProgress = true;

                ForgotPassRequests.sendMail(getActivity(), getFragmentManager(), nick, mail, new VolleyResponse() {
                    @Override
                    public void processFinish(String response) {
                        //todo handle response
                        try {
                            JSONObject jObj = new JSONObject(response);
                            int errorState = jObj.getInt("error");
                            switch (errorState) {
                                case Const.OK:
                                    Utils.showInfoSnackbar(getActivity().findViewById(R.id.loginLayout),
                                            getResources().getString(R.string.spotting_forgot_password_new_pass_send),
                                            getActivity());
                                    dismiss();
                                    break;
                                case Const.NICK_OR_MAIL_DOES_NOT_EXIST:
                                    Toast.makeText(getActivity(), getResources().getString(R.string.spotting_forgot_password_name_or_pass_not_exist), Toast.LENGTH_LONG).show();
                                    break;
                                case Const.MAIL_NEW_PASS_NOT_SENT:
                                    Toast.makeText(getActivity(), getResources().getString(R.string.spotting_forgot_password_not_new_pass_send), Toast.LENGTH_LONG).show();
                                    break;
                                default:
                                    Toast.makeText(getActivity(), getResources().getString(R.string.spotting_volley_error), Toast.LENGTH_LONG).show();
                                    break;
                            }
                        } catch (JSONException e) {
                            Log.v("ForgotPass", "JSONException" + e);
                            Toast.makeText(getActivity(), getResources().getString(R.string.spotting_volley_error), Toast.LENGTH_LONG).show();
                        }
                    }
                }, frameAnimation);
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(PUT_VISIBLE_KEY, isVisibleProgress);
    }

    private void initBtnCloseEvent() {
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void setIsVisibleProgress(boolean isVisibleProgress) {
        this.isVisibleProgress = isVisibleProgress;
    }

    public void setVisibilityProgress(boolean bool) {
        if (bool) {
            imgProgress.setVisibility(View.VISIBLE);
        } else {
            imgProgress.setVisibility(View.INVISIBLE);
        }
    }
}
