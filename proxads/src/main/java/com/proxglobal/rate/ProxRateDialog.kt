package com.proxglobal.rate

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.*
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.proxglobal.proxads.R

class ProxRateDialog : DialogFragment {
    private lateinit var mView: View
    private var mConfig: Config? = null
    private var layoutId = 0
    private var starRate = 0

    private lateinit var starDes: List<String>

    private lateinit var btnStar1: ImageView
    private lateinit var btnStar2: ImageView
    private lateinit var btnStar3: ImageView
    private lateinit var btnStar4: ImageView
    private lateinit var btnStar5: ImageView
    private lateinit var txtStarDes: TextView
    private lateinit var edtComment: EditText
    private lateinit var layoutLater: View
    private lateinit var btnSubmit: Button

    constructor()
    private constructor(config: Config) {
        mConfig = config
        layoutId = R.layout.dialog_rating
    }

    private constructor(layoutId: Int, listener: RatingDialogListener) {
        this.layoutId = layoutId
        mConfig = Config()
        mConfig!!.setListener(listener)
    }

    @SuppressLint("CutPasteId")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        mConfig = mDialog!!.mConfig
        layoutId = mDialog!!.layoutId
        val builder = AlertDialog.Builder(requireActivity())
        val inflater = requireActivity().layoutInflater
        mView = inflater.inflate(layoutId, null)
        loadConfig()

        starDes = listOf(
            getString(R.string._very_bad),
            getString(R.string._not_good),
            getString(R.string._quite_ok),
            getString(R.string._very_good),
            getString(R.string._excellent)
        )

        btnStar1 = mView.findViewById(R.id.btn_rate1)
        btnStar2 = mView.findViewById(R.id.btn_rate2)
        btnStar3 = mView.findViewById(R.id.btn_rate3)
        btnStar4 = mView.findViewById(R.id.btn_rate4)
        btnStar5 = mView.findViewById(R.id.btn_rate5)
        txtStarDes = mView.findViewById(R.id.star_des)
        edtComment = mView.findViewById(R.id.comment)
        layoutLater = mView.findViewById(R.id.layout_later)
        btnSubmit = mView.findViewById(R.id.submit)

        btnStar1.setOnClickListener {
            btnStar1.setImageResource(R.drawable.ic_star_enable)
            btnStar2.setImageResource(R.drawable.ic_star_disable)
            btnStar3.setImageResource(R.drawable.ic_star_disable)
            btnStar4.setImageResource(R.drawable.ic_star_disable)
            btnStar5.setImageResource(R.drawable.ic_star_disable)
            starRate = 1
            txtStarDes.text = starDes[starRate - 1]
            edtComment.visibility = View.VISIBLE
            layoutLater.visibility = View.GONE
            btnSubmit.visibility = View.VISIBLE
        }

        btnStar2.setOnClickListener {
            btnStar1.setImageResource(R.drawable.ic_star_enable)
            btnStar2.setImageResource(R.drawable.ic_star_enable)
            btnStar3.setImageResource(R.drawable.ic_star_disable)
            btnStar4.setImageResource(R.drawable.ic_star_disable)
            btnStar5.setImageResource(R.drawable.ic_star_disable)
            starRate = 2
            txtStarDes.text = starDes[starRate - 1]
            edtComment.visibility = View.VISIBLE
            layoutLater.visibility = View.GONE
            btnSubmit.visibility = View.VISIBLE
        }

        btnStar3.setOnClickListener {
            btnStar1.setImageResource(R.drawable.ic_star_enable)
            btnStar2.setImageResource(R.drawable.ic_star_enable)
            btnStar3.setImageResource(R.drawable.ic_star_enable)
            btnStar4.setImageResource(R.drawable.ic_star_disable)
            btnStar5.setImageResource(R.drawable.ic_star_disable)
            starRate = 3
            txtStarDes.text = starDes[starRate - 1]
            edtComment.visibility = View.VISIBLE
            layoutLater.visibility = View.GONE
            btnSubmit.visibility = View.VISIBLE
        }

        btnStar4.setOnClickListener {
            btnStar1.setImageResource(R.drawable.ic_star_enable)
            btnStar2.setImageResource(R.drawable.ic_star_enable)
            btnStar3.setImageResource(R.drawable.ic_star_enable)
            btnStar4.setImageResource(R.drawable.ic_star_enable)
            btnStar5.setImageResource(R.drawable.ic_star_disable)
            starRate = 4
            txtStarDes.text = starDes[starRate - 1]
            edtComment.visibility = View.GONE
            layoutLater.visibility = View.GONE
            btnSubmit.visibility = View.GONE

            sp!!.edit().putBoolean("isRated", true).apply()
            linkToStore()
            Handler(Looper.getMainLooper()).postDelayed({ dismiss() }, 1000)
        }

        btnStar5.setOnClickListener {
            btnStar1.setImageResource(R.drawable.ic_star_enable)
            btnStar2.setImageResource(R.drawable.ic_star_enable)
            btnStar3.setImageResource(R.drawable.ic_star_enable)
            btnStar4.setImageResource(R.drawable.ic_star_enable)
            btnStar5.setImageResource(R.drawable.ic_star_enable)
            starRate = 5
            txtStarDes.text = starDes[starRate - 1]
            edtComment.visibility = View.GONE
            layoutLater.visibility = View.GONE
            btnSubmit.visibility = View.GONE

            sp!!.edit().putBoolean("isRated", true).apply()
            linkToStore()
            Handler(Looper.getMainLooper()).postDelayed({ dismiss() }, 1000)
        }

        btnSubmit.setOnClickListener {
            if (starRate < 1) {
                val alertDialog = AlertDialog.Builder(requireActivity()).create()
                alertDialog.setTitle(getString(R.string._notify))
                alertDialog.setMessage(getString(R.string._please_select_star))
                alertDialog.setButton(
                    AlertDialog.BUTTON_NEUTRAL,
                    getString(R.string._ok)
                ) { dialog, _ -> dialog.dismiss() }
                alertDialog.show()
            } else {
                sp!!.edit().putBoolean("isRated", true).apply()
                val alertDialog = AlertDialog.Builder(requireActivity()).create()
                alertDialog.setTitle(getString(R.string._thanks))
                alertDialog.setMessage(getString(R.string._thank_for_rating))
                alertDialog.setButton(
                    AlertDialog.BUTTON_NEUTRAL,
                    getString(R.string._ok)
                ) { dialog, _ ->
                    dialog.dismiss()
                    mConfig!!.mListener!!.onDone()
                }
                dismiss()
                alertDialog.show()
                mConfig!!.mListener!!.onSubmitButtonClicked(
                    starRate,
                    mView.findViewById<EditText>(R.id.comment).text.toString()
                )
            }
        }
        layoutLater.setOnClickListener {
            mConfig!!.mListener!!.onLaterButtonClicked()
            dismiss()
        }

        builder.setView(mView)
        val d: Dialog = builder.create()
        d.setCanceledOnTouchOutside(false)
        if (d.window != null) {
            d.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            d.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        }
        return d
    }

    private fun linkToStore() {
        val appPackageName = requireActivity().packageName
        try {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=$appPackageName")
                )
            )
        } catch (e: ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                )
            )
        }
    }

    private fun loadConfig() {
        if (mConfig!!.rateTitle != null)
            (mView.findViewById<View>(R.id.tv_rating_title) as TextView).text = mConfig!!.rateTitle
        if (mConfig!!.mDescription != null)
            (mView.findViewById<View>(R.id.tv_rating_description) as TextView).text =
                mConfig!!.mDescription
        if (mConfig!!.icon != null)
            (mView.findViewById<View>(R.id.icon) as ImageView).setImageDrawable(mConfig!!.icon)
        if (mConfig!!.mBackgroundIcon != null)
            (mView.findViewById<View>(R.id.icon) as ImageView).background =
                mConfig!!.mBackgroundIcon
        if (mConfig!!.mCommentHint != null)
            (mView.findViewById<View>(R.id.comment) as EditText).hint = mConfig!!.mCommentHint
    }

    override fun show(manager: FragmentManager, tag: String?) {
        if (isAdded) {
            return
        }
        try {
            super.show(manager, tag)
        } catch (e: Exception) {
            Log.d("show_rate", "error: " + e.message)
        }
    }

    override fun dismiss() {
        if (isAdded) {
            super.dismiss()
        }
    }

    class Config {
        var mListener: RatingDialogListener? = null
        var icon: Drawable? = null
        var mBackgroundIcon: Drawable? = null
        var rateTitle: String? = null
        var mDescription: String? = null
        var mCommentHint: String? = null

        /**
         * set comment hint when edit text is empty<br></br>
         * **don't call to use default**
         *
         * @param commentHint
         */
        fun setCommentHint(commentHint: String?) {
            this.mCommentHint = commentHint
        }

        /**
         * set description for dialog <br></br>
         * **don't call to use default**
         *
         * @param description
         */
        fun setDescription(description: String?) {
            this.mDescription = description
        }

        /**
         * set title for dialog<br></br>
         * **don't call to use default**
         *
         * @param title
         */
        fun setTitle(title: String?) {
            rateTitle = title
        }

        /**
         * set icon for dialog<br></br>
         * **don't call to use default**
         *
         * @param icon
         */
        fun setForegroundIcon(icon: Drawable?) {
            this.icon = icon
        }

        /**
         * set background icon for dialog<br></br>
         * **don't call to use default**
         *
         * @param backgroundIcon
         */
        fun setBackgroundIcon(backgroundIcon: Drawable?) {
            this.mBackgroundIcon = backgroundIcon
        }

        /**
         * set listener for rating dialog<br></br>
         * **important**
         *
         * @param listener
         */
        fun setListener(listener: RatingDialogListener?) {
            this.mListener = listener
        }
    }

    companion object {
        private var sp: SharedPreferences? = null
        private var mDialog: ProxRateDialog? = null

        /**
         * init dialog view with layout id as param with listener
         *
         * @param layoutId
         */
        fun init(layoutId: Int, listener: RatingDialogListener) {
            mDialog = ProxRateDialog(layoutId, listener)
        }

        /**
         * init dialog view with default view and config
         *
         * @param config
         */
        fun init(config: Config) {
            mDialog = ProxRateDialog(config)
        }

        fun isRated(context: Context): Boolean {
            if (sp == null) sp = context.getSharedPreferences("prox", Context.MODE_PRIVATE)
            return sp!!.getBoolean("isRated", false)
        }

        /**
         * show by anyway (ignore rated)
         *
         * @param fm
         */
        fun showAlways(context: Context, fm: FragmentManager) {
            if (sp == null) sp = context.getSharedPreferences("prox", Context.MODE_PRIVATE)
            mDialog!!.show(fm, "prox")
        }

        /**
         * show if you haven't rate this app yet
         *
         * @param fm
         */
        fun showIfNeed(context: Context, fm: FragmentManager) {
            if (sp == null) sp = context.getSharedPreferences("prox", Context.MODE_PRIVATE)
            if (!isRated(context)) {
                mDialog!!.show(fm, "prox")
            } else {
                if (mDialog!!.mConfig != null) {
                    mDialog!!.mConfig!!.mListener!!.onRated()
                }
            }
        }
    }
}