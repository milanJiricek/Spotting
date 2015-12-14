package app.majlant.spotting.requests;

import android.app.Activity;
import android.support.v4.app.FragmentManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import app.majlant.spotting.R;
import app.majlant.spotting.app.AppController;
import app.majlant.spotting.app.Config;
import app.majlant.spotting.interfaces.VolleyResponse;
import app.majlant.spotting.utils.Utils;

/**
 * Created by majlant on 4.12.15.
 *
 * @author Milan Jiricek <milan.jiricekpv@gmail.com></milan.jiricekpv@gmail.com>
 */
public class RegistrationRequests {
    private static final String TAG = "RegistrationRequests";

    public static void doRegistration(final Activity activity, final FragmentManager fragmentManager, final String nick, final String password, final String mail, final VolleyResponse delegate) {
        Utils.showProgressDialog(activity, fragmentManager, activity.getResources().getString(R.string.spotting_registering));
        StringRequest req = new StringRequest(Request.Method.POST,
                Config.LOGIN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                VolleyLog.v("Response:%n %s", response);
                Utils.dismissProgressDialog(activity, fragmentManager);
                delegate.processFinish(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                Utils.dismissProgressDialog(activity, fragmentManager);
                Utils.showErrorSnackbar(activity.findViewById(R.id.registerLayout),
                        activity.getResources().getString(R.string.spotting_volley_error),
                        activity);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return setParameters(nick, password, mail);
            }
        };

        // Adding request to request gueue
        AppController.getInstance().addToRequestQueue(req);
    }

    private static Map<String, String> setParameters(String nick, String password, String mail) {
        // Posting parameters to login url
        Map<String, String> params = new HashMap<>();
        params.put("tag", "register");
        params.put("nick", nick);
        params.put("password", password);
        params.put("email", mail);

        return params;
    }
}
