package com.proxglobal.survey.dialog

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.proxglobal.proxads.databinding.DialogSurvey6Binding
import com.proxglobal.survey.ConfigSurvey

class Survey6Dialog(
    context: Context,
    private val binding: DialogSurvey6Binding,
    private val config: ConfigSurvey
) : SurveyDialog(context, binding.root) {

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseAnalytics = FirebaseAnalytics.getInstance(context)

        binding.txtQuestion1.text = config.question_1
        binding.txtAnswer1.hint = config.hint_answer_1

        binding.txtQuestion2.text = config.question_2
        binding.txtAnswer2.hint = config.hint_answer_2
    }

    override fun onSubmit(): Boolean {
        return if (binding.txtAnswer1.text.trim().isEmpty()
            || binding.txtAnswer2.text.trim().isEmpty()
        ) {
            false
        } else {
            val bundle = Bundle()
            bundle.putString("event_type", "click_submit")
            bundle.putString("question_1", binding.txtQuestion1.text.toString())
            bundle.putString("answer_1", binding.txtAnswer1.text.trim().toString())
            bundle.putString("question_2", binding.txtQuestion2.text.toString())
            bundle.putString("answer_2", binding.txtAnswer2.text.trim().toString())
            firebaseAnalytics.logEvent("prox_survey", bundle)
            true
        }
    }
}