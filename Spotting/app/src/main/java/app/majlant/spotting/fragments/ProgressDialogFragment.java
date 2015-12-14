package app.majlant.spotting.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import app.majlant.spotting.R;

/**
 * Created by majlant on 22.11.15.
 *
 * @author Milan Jiricek <milan.jiricekpv@gmail.com></milan.jiricekpv@gmail.com>
 */
public class ProgressDialogFragment extends DialogFragment {
    public static final String TAG = "ProgressDialogFragment";
    private static final String PUT_INFO_KEY = "infoKey";

    public static ProgressDialogFragment newInstance(String info) {
        ProgressDialogFragment fragment = new ProgressDialogFragment();

        Bundle args = new Bundle();
        args.putString(PUT_INFO_KEY, info);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String info = getArguments().getString(PUT_INFO_KEY);
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_fragment_progress, null);

        TextView txtInfo = (TextView) view.findViewById(R.id.txtInfo);
        txtInfo.setText(info);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        return builder.create();
    }
}
