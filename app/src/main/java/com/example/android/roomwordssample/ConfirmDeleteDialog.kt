package com.example.android.roomwordssample

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class ConfirmDeleteDialog : DialogFragment() {
    // Use this instance of the interface to deliver action events.
    private lateinit var listener: NoticeDialogListener

    // The activity that creates an instance of this dialog fragment must
    // implement this interface to receive event callbacks. Each method passes
    // the DialogFragment in case the host needs to query it.
    interface NoticeDialogListener {
        fun onDialogPositiveClick(dialog: DialogFragment)
        fun onDialogNegativeClick(dialog: DialogFragment)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Build the dialog and set up the button click handlers.
            val builder = AlertDialog.Builder(it)
            builder.setMessage("Do you really want to delete this task?")
                .setPositiveButton("Yes",
                    DialogInterface.OnClickListener { dialog, id ->
                        // Send the positive button event back to the
                        // host activity.
                        listener.onDialogPositiveClick(this)
                    })
                .setNegativeButton("I'll think again",
                    DialogInterface.OnClickListener { dialog, id ->
                        // Send the negative button event back to the
                        // host activity.
                        listener.onDialogNegativeClick(this)
                    })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    // Override the Fragment.onAttach() method to instantiate the
    // NoticeDialogListener.
    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Verify that the host activity implements the callback interface.
        try {
            // Instantiate the NoticeDialogListener so you can send events to
            // the host.
            listener = context as NoticeDialogListener
        } catch (e: ClassCastException) {
            // The activity doesn't implement the interface. Throw exception.
            throw ClassCastException((context.toString() +
                    " must implement NoticeDialogListener"))
        }
    }
}