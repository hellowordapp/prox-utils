package com.proxglobal.rate;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.proxglobal.proxads.R;

import java.util.Arrays;
import java.util.List;

public class ProxRateDialog extends DialogFragment {
    private View view;
    private Config mConfig;
    private Context context;
    private static SharedPreferences sp;
    private static ProxRateDialog dialog;
    private int layoutId;

    private ProxRateDialog(Context context, Config config){
        mConfig = config;
        this.context = context;
        sp = context.getSharedPreferences("prox", Context.MODE_PRIVATE);
        layoutId = R.layout.dialog_rating;
    }

    private ProxRateDialog(Context context, int layoutId) {
        this.layoutId = layoutId;
        this.context = context;
        sp = context.getSharedPreferences("prox", Context.MODE_PRIVATE);
        mConfig = null;
    }

    /**
     * init dialog view with layout id as param
     * @param context
     * @param layoutId
     */
    private static void init(Context context, int layoutId) {
        dialog = new ProxRateDialog(context, layoutId);
    }

    /**
     * init dialog view with default view and config
     * @param context
     * @param config
     */
    public static void init(Context context, @NonNull Config config){
        dialog = new ProxRateDialog(context, config);
    }

    /**
     * show by anyway (ignore rated)
     * @param fm
     */
    public static void showAlways(FragmentManager fm){
        dialog.show(fm,"prox");
    }

    /**
     * show if you haven't rate this app yet
     * @param fm
     */
    public static void showIfNeed(FragmentManager fm){
        if (!sp.getBoolean("isRated", false)){
            dialog.show(fm, "prox");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(layoutId, null);

        loadConfig();

        RatingBar ratingBar = view.findViewById(R.id.rating_bar);
        TextView tvStar = view.findViewById(R.id.star_des);
        EditText edComment = view.findViewById(R.id.comment);

        List<String> starDes = Arrays.asList("Very Bad", "Not good", "Quite ok", "Very Good", "Excellent !!!");
        view.findViewById(R.id.submit).setOnClickListener(v -> {
            if ((int) ratingBar.getRating() < 1) {
                androidx.appcompat.app.AlertDialog alertDialog = new androidx.appcompat.app.AlertDialog.Builder(context).create();
                alertDialog.setTitle("Notify");
                alertDialog.setMessage("Please select star !");
                alertDialog.setButton(androidx.appcompat.app.AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            } else {
                sp.edit().putBoolean("isRated", true).apply();
                androidx.appcompat.app.AlertDialog alertDialog = new androidx.appcompat.app.AlertDialog.Builder(context).create();
                alertDialog.setTitle("Thanks!");
                alertDialog.setMessage("Thank for rating");
                alertDialog.setButton(androidx.appcompat.app.AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                this.dismiss();
                alertDialog.show();
                mConfig.listener.onSubmitButtonClicked((int) ratingBar.getRating(), edComment.getText().toString());
            }

        });

        view.findViewById(R.id.layout_later).setOnClickListener(v -> {
            mConfig.listener.onLaterButtonClicked();
            this.dismiss();
        });

        ratingBar.setOnRatingBarChangeListener((r, v, b) -> {
            view.findViewById(R.id.layout_later).setVisibility(View.GONE);
            view.findViewById(R.id.submit).setVisibility(View.VISIBLE);
            if (v>0) tvStar.setText(starDes.get((int) v - 1));
            edComment.setVisibility(v < 4 ? View.VISIBLE : View.GONE);

            mConfig.listener.onChangeStar((int) v);
            if (v>=4){
                sp.edit().putBoolean("isRated", true).apply();
                String appPackageName = getActivity().getPackageName();
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
                dismiss();
            }
        });

        builder.setView(view);
        Dialog d = builder.create();
        d.setCanceledOnTouchOutside(false);
        if (d.getWindow() != null) {
            d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            d.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        return d;
    }

    private void loadConfig() {
        if(mConfig.rateTitle != null)
            ((TextView) view.findViewById(R.id.tv_rating_title)).setText(mConfig.rateTitle);

        if(mConfig.description != null)
            ((TextView) view.findViewById(R.id.tv_rating_description)).setText(mConfig.description);

        if(mConfig.icon != null)
            ((ImageView) view.findViewById(R.id.icon)).setImageDrawable(mConfig.icon);

        if(mConfig.backgroundIcon != null) {
            ((ImageView) view.findViewById(R.id.icon)).setBackground(mConfig.backgroundIcon);
        }

        if(mConfig.commentHint != null) {
            ((EditText) view.findViewById(R.id.comment)).setHint(mConfig.commentHint);
        }
    }

    @Override
    public void show(@NonNull FragmentManager manager, @Nullable String tag) {
        if (isAdded()) {
            return;
        }
        super.show(manager, tag);
    }

    @Override
    public void dismiss() {
        if (isAdded()) {
            super.dismiss();;
        }
    }

    public static class Config {
        private RatingDialogListener listener;
        private Drawable icon;
        private Drawable backgroundIcon;
        private String rateTitle, description, commentHint;

        /**
         * set comment hint when edit text is empty<br>
         * <b>don't call to use default</b>
         * @param commentHint
         */
        public void setCommentHint(String commentHint) {
            this.commentHint = commentHint;
        }

        /**
         * set description for dialog <br>
         * <b>don't call to use default</b>
         * @param description
         */
        public void setDescription(String description) {
            this.description = description;
        }

        /**
         * set title for dialog<br>
         * <b>don't call to use default</b>
         * @param title
         */
        public void setTitle(String title) {
            this.rateTitle = title;
        }

        /**
         * set icon for dialog<br>
         * <b>don't call to use default</b>
         * @param icon
         */
        public void setForegroundIcon(Drawable icon) {
            this.icon = icon;
        }

        /**
         * set background icon for dialog<br>
         * <b>don't call to use default</b>
         * @param backgroundIcon
         */
        public void setBackgroundIcon(Drawable backgroundIcon) {
            this.backgroundIcon = backgroundIcon;
        }

        /**
         * set listener for rating dialog<br>
         * <strong>important</strong>
         * @param listener
         */
        public void setListener(RatingDialogListener listener) {
            this.listener = listener;
        }
    }
}
