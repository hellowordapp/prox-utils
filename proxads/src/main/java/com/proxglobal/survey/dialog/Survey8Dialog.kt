package com.proxglobal.survey.dialog

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import com.google.firebase.analytics.FirebaseAnalytics
import com.proxglobal.proxads.databinding.DialogSurvey8Binding
import com.proxglobal.survey.ConfigSurvey

class Survey8Dialog(context: Context, private val binding: DialogSurvey8Binding, private val config: ConfigSurvey) :
    SurveyDialog(context, binding.root) {

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.grpQuestion.setOnCheckedChangeListener(grpQuestionListener)

        firebaseAnalytics = FirebaseAnalytics.getInstance(context)

        binding.txtQuestion.text = config.question_1
        binding.radOption1.text = config.option_1_1
        binding.radOption2.text = config.option_1_2
        binding.radOption3.text = config.option_1_3
        binding.radOptionOther.text = config.option_other_1

        binding.txtQuestionOther.text = config.question_2
        binding.txtAnswerOther.hint = config.hint_answer_2
    }

    private val grpQuestionListener =
        RadioGroup.OnCheckedChangeListener { _: RadioGroup?, _: Int ->
            if (binding.radOptionOther.isChecked){
                binding.layoutQuestionOther.visibility = View.VISIBLE
            }else{
                binding.layoutQuestionOther.visibility = View.GONE
            }
        }

    override fun onSubmit(): Boolean {
        return if (binding.grpQuestion.checkedRadioButtonId == -1
            || (binding.radOptionOther.isChecked && binding.txtAnswerOther.text.trim().isEmpty())) false
        else{
            val bundle = Bundle()
            bundle.putString("event_type", "click_submit")
            bundle.putString("question_1", binding.txtQuestion.text.toString())
            bundle.putString("answer_1", findViewById<RadioButton>(binding.grpQuestion.checkedRadioButtonId).text.toString())
            bundle.putString("question_2", binding.txtQuestionOther.text.toString())
            bundle.putString("answer_2", if (binding.radOptionOther.isChecked) binding.txtAnswerOther.text.trim().toString() else "")
            firebaseAnalytics.logEvent("prox_survey", bundle)
            true
        }
    }
}