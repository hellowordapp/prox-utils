package com.proxglobal.survey.dialog

import android.content.Context
import android.os.Bundle
import android.widget.RadioButton
import com.google.firebase.analytics.FirebaseAnalytics
import com.proxglobal.proxads.databinding.DialogSurvey4Binding
import com.proxglobal.survey.ConfigSurvey

class Survey4Dialog(
    context: Context,
    private val binding: DialogSurvey4Binding,
    private val config: ConfigSurvey
) : SurveyDialog(context, binding.root) {

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseAnalytics = FirebaseAnalytics.getInstance(context)

        binding.txtQuestion1.text = config.question_1
        binding.radOption11.text = config.option_1_1
        binding.radOption12.text = config.option_1_2
        binding.radOption13.text = config.option_1_3

        binding.txtQuestion2.text = config.question_2
        binding.radOption21.text = config.option_2_1
        binding.radOption22.text = config.option_2_2
        binding.radOption23.text = config.option_2_3
    }

    override fun onSubmit(): Boolean {
        return if (binding.grpQuestion1.checkedRadioButtonId == -1
            || binding.grpQuestion2.checkedRadioButtonId == -1){
            false
        }else{
            val bundle = Bundle()
            bundle.putString("event_type", "click_submit")
            bundle.putString("survey_name", config.survey_name)
            bundle.putString("question_1", binding.txtQuestion1.text.toString())
            bundle.putString("answer_1", findViewById<RadioButton>(binding.grpQuestion1.checkedRadioButtonId).text.toString())
            bundle.putString("question_2", binding.txtQuestion2.text.toString())
            bundle.putString("answer_2", findViewById<RadioButton>(binding.grpQuestion2.checkedRadioButtonId).text.toString())
            firebaseAnalytics.logEvent("prox_survey", bundle)
            true
        }
    }

    override fun onNotNow() {
        val bundle = Bundle()
        bundle.putString("event_type", "click_cancel")
        bundle.putString("survey_name", config.survey_name)
        firebaseAnalytics.logEvent("prox_survey", bundle)
    }
}