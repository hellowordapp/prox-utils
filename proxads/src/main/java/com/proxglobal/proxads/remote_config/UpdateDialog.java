package com.proxglobal.proxads.remote_config;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.proxglobal.proxads.R;

public class UpdateDialog extends DialogFragment {
    private int iconAppId;
    private String appTitle;

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

        builder.setView(view);
        Dialog d = builder.create();
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

    public void showDialog(FragmentManager manager) {
        if (isAdded()) {
            return;
        }
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
