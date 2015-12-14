package app.majlant.spotting.requests;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.AnimationDrawable;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

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
import app.majlant.spotting.fragments.ForgotPassDialogFragment;
import app.majlant.spotting.interfaces.VolleyResponse;
import app.majlant.spotting.utils.Utils;

/**
 * Created by majlant on 5.12.15.
 *
 * @author Milan Jiricek <milan.jiricekpv@gmail.com></milan.jiricekpv@gmail.com>
 */
public class ForgotPassRequests {
    private static final String TAG = "ForgotPassRequests";

    public static void sendMail(final Activity activity, final FragmentManager fragmentManager, final String nick, final String mail, final VolleyResponse delegate, final AnimationDrawable frameAnimation) {
        //stop sensor
        Utils.setActivityOrientation(activity, activity.getResources().getConfiguration().orientation);

        frameAnimation.start();
        setVisibleProgress(fragmentManager, true);

        StringRequest req = new StringRequest(Request.Method.POST,
                Config.NEW_PASS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                VolleyLog.v("Response:%n %s", response);
                //start sensor
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);

                frameAnimation.stop();
                setVisibleProgress(fragmentManager, false);
                delegate.processFinish(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                //start sensor
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);

                frameAnimation.stop();
                setVisibleProgress(fragmentManager, false);
                Toast.makeText(activity, activity.getResources().getString(R.string.spotting_volley_error), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return setParameters(nick, mail);
            }
        };

        // Adding request to request gueue
        AppController.getInstance().addToRequestQueue(req);
    }

    private static Map<String, String> setParameters(String nick, String mail) {
        // Posting parameters to login url
        Map<String, String> params = new HashMap<>();
        params.put("tag", "new_pass");
        params.put("nick", nick);
        params.put("email", mail);

        return params;
    }

    private static void setVisibleProgress(FragmentManager fragmentManager, boolean visible) {
        ForgotPassDialogFragment forgotPassDialogFragment = (ForgotPassDialogFragment) fragmentManager.findFragmentByTag(ForgotPassDialogFragment.TAG);

        if (forgotPassDialogFragment != null) {
            forgotPassDialogFragment.setIsVisibleProgress(visible);
            forgotPassDialogFragment.setVisibilityProgress(visible);
        }
    }
}
