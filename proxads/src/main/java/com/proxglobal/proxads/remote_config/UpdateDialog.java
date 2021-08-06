package com.proxglobal.proxads.remote_config;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.proxglobal.proxads.R;

public class UpdateDialog extends DialogFragment {
    private ConfigUpdateVersion configUpdateVersion;
    private int iconAppId;
    private String appTitle;

    private TextView versionName;
    private TextView title;
    private TextView message;

    private long exitPressTime= 0L;

    public UpdateDialog(int iconAppId, String appTitle) {
        this.iconAppId = iconAppId;
        this.appTitle = appTitle;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_update, null);

        view.findViewById(R.id.ud_icon).setBackgroundResource(iconAppId);
        ((TextView) view.findViewById(R.id.ud_app_title)).setText(appTitle);
        getView(view);

        builder.setView(view);
        Dialog d = builder.create();

        d.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (event.getAction() != KeyEvent.ACTION_DOWN) {
                    return true;
                }
                if (System.currentTimeMillis() - exitPressTime < 2000) {
                    requireActivity().finishAffinity();
                    System.exit(0);
                }
                else {
                    exitPressTime = System.currentTimeMillis();
                    Toast.makeText(requireActivity(), "Press again to exit", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });

        d.setCanceledOnTouchOutside(false);
        if (d.getWindow() != null) {
            d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            try {
                d.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return d;
    }

    private void getView(View parent) {
        versionName = parent.findViewById(R.id.ud_version_name);
        title = parent.findViewById(R.id.ud_title);
        message = parent.findViewById(R.id.ud_message);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (configUpdateVersion != null) {
            versionName.setText(configUpdateVersion.versionName);
            title.setText(configUpdateVersion.title);
            message.setText(configUpdateVersion.message);
        }
    }

    public void showDialog(FragmentManager manager, ConfigUpdateVersion config) {
        if (isAdded()) {
            return;
        }

        configUpdateVersion = config;

        try {
            show(manager, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dismiss() {
        if (isAdded()) {
            super.dismiss();
        }

    }
}
