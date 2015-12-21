package app.majlant.spotting;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import app.majlant.spotting.app.Const;
import app.majlant.spotting.interfaces.VolleyResponse;
import app.majlant.spotting.requests.RegistrationRequests;
import app.majlant.spotting.utils.Utils;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by majlant on 14.11.15.
 *
 * @author Milan Jiricek <milan.jiricekpv@gmail.com></milan.jiricekpv@gmail.com>
 */
public class RegistrationActivity extends FragmentActivity {
    public static final String EXTRAS_NICK = "extrasNick";
    private static final String TAG = "RegistrationActivity";
    //init view
    @Bind(R.id.txtVersion)
    TextView txtVersion;

    @Bind(R.id.txtLinkToLogin)
    TextView txtLinkToLogin;

    @Bind(R.id.btnRegistration)
    Button btnRegistration;

    @Bind(R.id.editNick)
    EditText editNick;

    @Bind(R.id.editEmail)
    EditText editEmail;

    @Bind(R.id.editTextPassword)
    EditText editTextPassword;

    @Bind(R.id.editPasswordRepeat)
    EditText editPasswordRepeat;

    @Bind(R.id.registerLayout)
    ScrollView registerLayout;

    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_activity);
        ButterKnife.bind(this);
        activity = this;

        TransitionDrawable transition = (TransitionDrawable) registerLayout.getBackground();
        transition.startTransition(Const.TRANSACTION_TIME);

        initLinkToLoginEvent();
        initBtnRegistrationEvent();
        setTxtVersion();
    }

    private void setTxtVersion() {
        String versionName = BuildConfig.VERSION_NAME;

        String text = String.format(getResources().getString(R.string.spotting_version), versionName);

        txtVersion.setText(text);
    }

    /**
     * Link to LoginActivity Screen
     */
    private void initLinkToLoginEvent() {
        txtLinkToLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent intent = new Intent(activity, LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });
    }

    private void initBtnRegistrationEvent() {
        btnRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo handle registration
                final String nick = editNick.getText().toString();
                String mail = editEmail.getText().toString();
                String password = editTextPassword.getText().toString();
                String passwordConfirm = editPasswordRepeat.getText().toString();

                // Check for empty data in the form editNick
                if (nick.isEmpty()) {
                    editNick.setError(getResources().getString(R.string.spotting_log_edittext_error_field));
                    return;
                } else {
                    editNick.setError(null);
                }

                // Check for empty data in the form editEmail
                if (mail.isEmpty()) {
                    editEmail.setError(getResources().getString(R.string.spotting_log_edittext_error_field));
                    return;
                } else {
                    editEmail.setError(null);
                }

                // Check for empty data in the form editTextPassword
                if (password.isEmpty()) {
                    editTextPassword.setError(getResources().getString(R.string.spotting_log_edittext_error_field));
                    return;
                } else {
                    editTextPassword.setError(null);
                }

                // Check for empty data in the form editPasswordRepeat
                if (passwordConfirm.isEmpty()) {
                    editPasswordRepeat.setError(getResources().getString(R.string.spotting_log_edittext_error_field));
                    return;
                } else {
                    editPasswordRepeat.setError(null);
                }

                if (!Utils.isValidEmail(mail)) {
                    editEmail.setError(getResources().getString(R.string.spotting_mail_not_valid));
                    return;
                } else {
                    editEmail.setError(null);
                }

                if (!password.equals(passwordConfirm)) {
                    editTextPassword.setError(getResources().getString(R.string.spotting_password_not_match));
                    return;
                } else {
                    editTextPassword.setError(null);
                }

                //check min. length
                if (password.length() < 6) {
                    editTextPassword.setError(getResources().getString(R.string.spotting_password_length));
                    return;
                } else {
                    editTextPassword.setError(null);
                }

                //Check connection
                if (!Utils.haveInternet(activity)) {
                    Utils.showErrorSnackbar(registerLayout,
                            activity.getResources().getString(R.string.spotting_check_connection),
                            activity);
                    return;
                }


                RegistrationRequests.doRegistration(activity, getSupportFragmentManager(), nick, password, mail, new VolleyResponse() {
                    @Override
                    public void processFinish(String response) {
                        //todo handle response
                        try {
                            JSONObject jObj = new JSONObject(response);
                            int errorState = jObj.getInt("error");
                            switch (errorState) {
                                case Const.OK:
                                    Bundle extras = new Bundle();
                                    extras.putString(EXTRAS_NICK, nick);//Send nick to Login activity.
                                    Intent i = new Intent(getApplicationContext(),
                                            LoginActivity.class);
                                    i.putExtras(extras);
                                    startActivity(i);
                                    finish();
                                    overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                                    break;
                                case Const.USER_ALREADY_EXISTED:
                                    Utils.showErrorSnackbar(registerLayout,
                                            activity.getResources().getString(R.string.spotting_user_already_existed),
                                            activity);
                                    break;
                                default:
                                    Utils.showErrorSnackbar(registerLayout,
                                            activity.getResources().getString(R.string.spotting_volley_error),
                                            activity);
                                    break;
                            }
                        } catch (JSONException e) {
                            Log.v(TAG, "JSONException" + e);
                            Utils.showErrorSnackbar(registerLayout,
                                    activity.getResources().getString(R.string.spotting_volley_error),
                                    activity);
                        }
                    }
                });

            }
        });
    }
}
