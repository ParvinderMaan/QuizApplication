package com.app.quiz.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.app.quiz.R

class SignOutDialogFragment : DialogFragment() {
    private var TAG: String? = null
    private var alertDialogListener: AlertDialogListener? = null


    companion object {
        fun newInstance(): SignOutDialogFragment {
            val fragment = SignOutDialogFragment()
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        isCancelable = false
        val builder = AlertDialog.Builder(requireActivity(), R.style.AppAlertDialogStyle)
        builder.setTitle(resources.getString(R.string.app_name))
        builder.setMessage(getString(R.string.title_log_out))
        builder.setPositiveButton(
            getString(R.string.action_yes)
        ) { dialog: DialogInterface?, which: Int ->
            TAG = "YES"
            dismiss()
        }
        builder.setNegativeButton(
            getString(R.string.action_no)
        ) { dialog: DialogInterface?, which: Int ->
            TAG = "NO"
            dismiss()
        }
        return builder.create()
    }

    override fun onDetach() {
        super.onDetach()
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