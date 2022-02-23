package com.proxglobal.proxads.remote_config;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
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
    private int iconAppId = 0;
    private String appTitle;

    private TextView versionName;
    private TextView title;
    private TextView message;

    private long exitPressTime= 0L;
    private int layoutId;

    public UpdateDialog() {

    }

    public UpdateDialog(int iconAppId, String appTitle) {
        this.iconAppId = iconAppId;
        this.appTitle = appTitle;
        this.layoutId = R.layout.dialog_update;
    }

    public UpdateDialog(int layoutId) {
        this.layoutId = layoutId;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null) {
            this.appTitle = (String) savedInstanceState.getString("app_title");
            this.iconAppId = (int) savedInstanceState.getInt("icon_app_id");
            this.layoutId = (int) savedInstanceState.getInt("layout_id");
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("icon_app_id", iconAppId);
        outState.putString("app_title", appTitle);
        outState.putInt("layout_id", layoutId);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(layoutId, null);

        if(iconAppId != 0) view.findViewById(R.id.ud_icon).setBackgroundResource(iconAppId);
        if(appTitle != null) ((TextView) view.findViewById(R.id.ud_app_title)).setText(appTitle);
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

        parent.findViewById(R.id.ud_update).setOnClickListener((v)->{
            if(configUpdateVersion != null) {
                linkToStore(configUpdateVersion.newPackage);
            }
        });
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

    private void linkToStore(String newPackage) {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(
                ProxRemoteConfig.PREF_RATE, Context.MODE_PRIVATE
        );

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(ProxRemoteConfig.PREF_RATE, true);
        editor.apply();

        String appPackageName = "";
        if (newPackage.equals("")) {
            appPackageName = requireActivity().getPackageName();
        } else {
            appPackageName = newPackage;
        }

        try {
            requireActivity().startActivity(
                    new Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=" + appPackageName)
                    )
            );
        } catch (ActivityNotFoundException e) {
            requireActivity().startActivity(
                    new Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)
                    )
            );
        }
    }
}
