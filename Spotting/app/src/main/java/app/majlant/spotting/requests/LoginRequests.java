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
 * Created by majlant on 19.11.15.
 *
 * @author Milan Jiricek <milan.jiricekpv@gmail.com></milan.jiricekpv@gmail.com>
 */
public class LoginRequests {
    private static final String TAG = "LoginRequests";

    public static void doLogin(final Activity activity, final FragmentManager fragmentManager, final String nick, final String password, final VolleyResponse delegate) {
        Utils.showProgressDialog(activity, fragmentManager, activity.getResources().getString(R.string.spotting_logging));
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
                Utils.showErrorSnackbar(activity.findViewById(R.id.loginLayout),
                        activity.getResources().getString(R.string.spotting_log_error),
                        activity);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return setParameters(nick, password);
            }
        };

        // Adding request to request gueue
        AppController.getInstance().addToRequestQueue(req);
    }

    private static Map<String, String> setParameters(String nick, String password) {
        // Posting parameters to login url
        Map<String, String> params = new HashMap<>();
        params.put("tag", "login");
        params.put("nick", nick);
        params.put("password", password);

        return params;
    }
}
