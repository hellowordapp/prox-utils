package com.proxglobal.survey.dialog

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.proxglobal.proxads.databinding.DialogSurvey7Binding
import com.proxglobal.survey.ConfigSurvey

class Survey7Dialog(
    context: Context,
    private val binding: DialogSurvey7Binding,
    private val config: ConfigSurvey
) : SurveyDialog(context, binding.root) {

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseAnalytics = FirebaseAnalytics.getInstance(context)

        binding.txtQuestion.text = config.question_1
        binding.txtAnswer.hint = config.hint_answer_1
    }

    override fun onSubmit(): Boolean {
        return if (binding.txtAnswer.text.trim().isEmpty()) false
        else {
            val bundle = Bundle()
            bundle.putString("event_type", "click_submit")
            bundle.putString("question_1", binding.txtQuestion.text.toString())
            bundle.putString("answer_1", binding.txtAnswer.text.trim().toString())
            firebaseAnalytics.logEvent("prox_survey", bundle)
            true
        }
    }
}