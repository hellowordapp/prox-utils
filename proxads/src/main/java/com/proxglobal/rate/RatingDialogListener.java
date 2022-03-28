package com.proxglobal.rate;

public abstract class RatingDialogListener {
    public void onSubmitButtonClicked(int rate, String comment) {};
    public void onLaterButtonClicked() {};
    public void onChangeStar(int rate) {};
    public void onRated() {};
    public void onDone() {};
}
