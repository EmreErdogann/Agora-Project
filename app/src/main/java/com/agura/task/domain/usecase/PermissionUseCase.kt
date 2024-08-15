package com.agura.task.domain.usecase

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import javax.inject.Inject

class PermissionUseCase @Inject constructor(
    private val context: Context
) {

    fun checkSelfPermission(): Boolean {
        return REQUESTED_PERMISSIONS.all { permission ->
            ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
        }
    }

    fun requestPermissions(activity: Activity) {
        ActivityCompat.requestPermissions(activity, REQUESTED_PERMISSIONS, PERMISSION_REQ_ID)
    }



    companion object {
        private const val PERMISSION_REQ_ID = 22
        private val REQUESTED_PERMISSIONS = arrayOf(
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA
        )
    }
}