package com.app.quiz.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.app.quiz.R

class AlterLanguageDialogFragment : DialogFragment() {
    private var TAG: String? = null
    private var alertDialogListener: AlertDialogListener? = null


    companion object {
        fun newInstance(): AlterLanguageDialogFragment {
            val fragment = AlterLanguageDialogFragment()
            return fragment
        }
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        isCancelable = false
        val builder = AlertDialog.Builder(requireActivity(), R.style.AppAlertDialogStyle)
        builder.setTitle(resources.getString(R.string.app_name))
        builder.setMessage(getString(R.string.title_change_language))
        builder.setPositiveButton(
            getString(R.string.action_ok)
        ) { dialog: DialogInterface?, which: Int ->
            TAG = "YES"
            dismiss()
        }

        return builder.create()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        when (TAG) {
            "YES" ->  //
                alertDialogListener!!.onClickYes()
            "NO" ->  // nothing
                alertDialogListener!!.onClickNo()
        }
    }

    fun setOnAlertDialogListener(alertDialogListener: AlertDialogListener?) {
        this.alertDialogListener = alertDialogListener
    }

    public interface AlertDialogListener {
        fun onClickYes()
        fun onClickNo()
    }

}