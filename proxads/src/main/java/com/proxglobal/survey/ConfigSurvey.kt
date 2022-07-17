package com.proxglobal.survey

import com.google.gson.annotations.SerializedName

class ConfigSurvey {
    @SerializedName("style")
    var style = 0

    @JvmField
    @SerializedName("status")
    var status = false

    @SerializedName("question_1")
    var question_1: String? = null

    @SerializedName("hint_answer_1")
    var hint_answer_1: String? = null

    @SerializedName("option_1_1")
    var option_1_1: String? = null

    @SerializedName("option_1_2")
    var option_1_2: String? = null

    @SerializedName("option_1_3")
    var option_1_3: String? = null

    @SerializedName("option_1_4")
    var option_1_4: String? = null

    @SerializedName("question_2")
    var question_2: String? = null

    @SerializedName("hint_answer_2")
    var hint_answer_2: String? = null

    @SerializedName("option_2_1")
    var option_2_1: String? = null

    @SerializedName("option_2_2")
    var option_2_2: String? = null

    @SerializedName("option_2_3")
    var option_2_3: String? = null

    @SerializedName("option_2_4")
    var option_2_4: String? = null
}