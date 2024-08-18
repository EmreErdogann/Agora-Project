package com.agura.task.presentation.util

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.view.View
import com.agura.task.databinding.LbCustomOneOptionDialogBinding

fun Activity.showCustomOneOptionDialog(
    title: String,
    message: String,
    buttonText: String,
    cancelable: Boolean,
    clickCallback: (dialog: Dialog, view: View) -> Unit,
) {
    val builder = AlertDialog.Builder(this)
    val binding = LbCustomOneOptionDialogBinding.inflate(layoutInflater)
    val alertDialog = builder.setView(binding.root).create()
    alertDialog.setCancelable(cancelable)
    binding.apply {
        txtTitle.text = title
        txtDescription.text = message
        btnOk.text = buttonText
        btnOk.setOnClickListener {
            alertDialog.dismiss()
            clickCallback(alertDialog, it)
        }
    }
    alertDialog.show()
}