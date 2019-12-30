package com.developer.ivan.easymadbus.framework

import android.app.Application
import android.content.pm.PackageManager.PERMISSION_GRANTED
import androidx.core.content.ContextCompat

class PermissionChecker(private val application: Application) {

    fun check(permission: String): Boolean =
        ContextCompat.checkSelfPermission(application, permission) == PERMISSION_GRANTED
}