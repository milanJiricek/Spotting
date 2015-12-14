package app.majlant.spotting.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import app.majlant.spotting.R;
import app.majlant.spotting.fragments.ProgressDialogFragment;

/**
 * Created by majlant on 23.11.15.
 *
 * @author Milan Jiricek <milan.jiricekpv@gmail.com></milan.jiricekpv@gmail.com>
 */
public class Utils {
    private static final String TAG = "Utils";

    /**
     * Vypne sensor zmeny orientace obrazovky.
     *
     * @param activity
     * @param preferenceOrientation
     */
    public static void setActivityOrientation(Activity activity, int preferenceOrientation) {
        if (preferenceOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else if (preferenceOrientation == Configuration.ORIENTATION_PORTRAIT) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    /* ####### progress dialog ####### */
    public static void showProgressDialog(Activity activity, FragmentManager fragmentManager, String info) {
        //check info
        if (info == null) {
            Log.v(TAG, "info is null.");
            return;
        }
        //stop sensor
        Utils.setActivityOrientation(activity, activity.getResources().getConfiguration().orientation);

        FragmentTransaction ft = fragmentManager.beginTransaction();
        Fragment prev = fragmentManager.findFragmentByTag(ProgressDialogFragment.TAG);

        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        DialogFragment frag = ProgressDialogFragment.newInstance(info);
        frag.setCancelable(false);
        frag.show(ft, ProgressDialogFragment.TAG);
    }

    public static void dismissProgressDialog(Activity activity, FragmentManager fragmentManager) {
        ProgressDialogFragment progressDialogFragment = (ProgressDialogFragment) fragmentManager.findFragmentByTag(ProgressDialogFragment.TAG);

        if (progressDialogFragment != null) {
            progressDialogFragment.dismiss();
        }
        //start sensor
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
    }

    /* ######### snackbar ######### */
    public static void showErrorSnackbar(View view, String text, Activity activity) {
        hideKeyboard(view, activity);

        Snackbar snackbar = Snackbar.make(view,
                text,
                Snackbar.LENGTH_LONG);
        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.RED);
        sbView.setBackgroundColor(activity.getResources().getColor(R.color.spotting_base_100_percent_gray));
        snackbar.show();
    }

    public static void showInfoSnackbar(View view, String text, Activity activity) {
        hideKeyboard(view, activity);

        Snackbar snackbar = Snackbar.make(view,
                text,
                Snackbar.LENGTH_LONG);
        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.BLACK);
        sbView.setBackgroundColor(activity.getResources().getColor(R.color.spotting_base_100_percent_gray));
        snackbar.show();
    }

    private static void hideKeyboard(View view, Activity activity) {
        // Check if no view has focus:
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /* ############# connection ########### */

    /**
     * Checks if the device has Internet connection.
     *
     * @param ctx
     * @return <code>true</code> if the phone is connected to the Internet.
     */
    public static boolean haveInternet(Context ctx) {
        NetworkInfo info = (NetworkInfo) ((ConnectivityManager) ctx.
                getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return !(info == null || !info.isConnected());
    }

    /* ########## other ############ */

    /**
     * for API > 8 aka Android 2.2
     * isValidEmail method is used for checking valid email format.
     *
     * @param email
     * @return boolean true for valid false for invalid
     */
    public static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
