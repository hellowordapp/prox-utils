package com.proxglobal.rate;

public interface RatingDialogListener {
    void onSubmitButtonClicked(int rate, String comment);
    void onLaterButtonClicked();
    void onChangeStar(int rate);
}
