package app.majlant.spotting;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import app.majlant.spotting.app.Const;
import app.majlant.spotting.fragments.ForgotPassDialogFragment;
import app.majlant.spotting.interfaces.VolleyResponse;
import app.majlant.spotting.requests.LoginRequests;
import app.majlant.spotting.utils.Utils;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by majlant on 9.11.15.
 *
 * @author Milan Jiricek <milan.jiricekpv@gmail.com></milan.jiricekpv@gmail.com>
 */
public class LoginActivity extends FragmentActivity {
    private static final String TAG = "LoginActivity";
    //init view
    @Bind(R.id.btnForgotPassword)
    Button btnForgotPassword;

    @Bind(R.id.txtVersion)
    TextView txtVersion;

    @Bind(R.id.btnLinkToRegistration)
    Button btnLinkToRegistration;

    @Bind(R.id.btnLogin)
    Button btnLogin;

    @Bind(R.id.editNick)
    EditText editNick;

    @Bind(R.id.editPassword)
    EditText editPassword;

    @Bind(R.id.loginLayout)
    ScrollView loginLayout;

    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        ButterKnife.bind(this);
        activity = this;

        TransitionDrawable transition = (TransitionDrawable) loginLayout.getBackground();
        transition.startTransition(Const.TRANSACTION_TIME);

        initBtnLoginEvent();
        initBtnLinkToRegistrationEvent();
        initBtnForgotPasswordEvent();
        setTxtVersion();

        checkExtras();
    }

    private void checkExtras() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String nick = extras.getString(RegistrationActivity.EXTRAS_NICK);
            if (nick != null) {
                editNick.setText(nick);
                Utils.showErrorSnackbar(loginLayout,
                        activity.getResources().getString(R.string.spotting_registration_successful),
                        activity);
            }
        }
    }

    private void initBtnForgotPasswordEvent() {
        btnForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showForgotPassDialog();
            }
        });
    }

    private void showForgotPassDialog() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag(ForgotPassDialogFragment.TAG);

        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        DialogFragment frag = ForgotPassDialogFragment.newInstance();
        frag.setCancelable(false);
        frag.show(ft, ForgotPassDialogFragment.TAG);
    }

    private void setTxtVersion() {
        String versionName = BuildConfig.VERSION_NAME;

        String text = String.format(getResources().getString(R.string.spotting_version), versionName);

        txtVersion.setText(text);
    }

    /**
     * Link to Registration activity
     */
    private void initBtnLinkToRegistrationEvent() {
        btnLinkToRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, RegistrationActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    private void initBtnLoginEvent() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo handle login
                String nick = editNick.getText().toString();
                String password = editPassword.getText().toString();

                // Check for empty data in the form editNick
                if (nick.isEmpty()) {
                    editNick.setError(getResources().getString(R.string.spotting_log_edittext_error_field));
                    return;
                } else {
                    editNick.setError(null);
                }

                // Check for empty data in the form editPassword
                if (password.isEmpty()) {
                    editPassword.setError(getResources().getString(R.string.spotting_log_edittext_error_field));
                    return;
                } else {
                    editPassword.setError(null);
                }

                //Check connection
                if (!Utils.haveInternet(activity)) {
                    Utils.showErrorSnackbar(loginLayout,
                            activity.getResources().getString(R.string.spotting_check_connection),
                            activity);
                    return;
                }

                LoginRequests.doLogin(activity, getSupportFragmentManager(), nick, password, new VolleyResponse() {
                    @Override
                    public void processFinish(String response) {
                        //todo handle response
                        try {
                            JSONObject jObj = new JSONObject(response);
                            int errorState = jObj.getInt("error");
                            switch (errorState) {
                                case Const.OK:
                                    Intent intent = new Intent(activity, SpotsActivity.class);
                                    startActivity(intent);
                                    break;
                                case Const.INCORRECT_NICK_OR_PASS:
                                    Utils.showErrorSnackbar(loginLayout,
                                            activity.getResources().getString(R.string.spotting_incorrect_nick_or_password),
                                            activity);
                                    break;
                                default:
                                    Utils.showErrorSnackbar(loginLayout,
                                            activity.getResources().getString(R.string.spotting_log_error),
                                            activity);
                                    break;
                            }
                        } catch (JSONException e) {
                            Log.v(TAG, "JSONException" + e);
                            Utils.showErrorSnackbar(loginLayout,
                                    activity.getResources().getString(R.string.spotting_log_error),
                                    activity);
                        }
                    }
                });

            }
        });
    }
}
