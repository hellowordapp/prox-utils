package com.proxglobal.rate

abstract class RatingDialogListener {
    open fun onSubmitButtonClicked(rate: Int, comment: String) {}
    open fun onLaterButtonClicked() {}
    open fun onChangeStar(rate: Int) {}
    open fun onRated() {}
    open fun onDone() {}
}