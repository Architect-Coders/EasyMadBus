package com.developer.ivan.easymadbus.framework

import android.app.Application
import android.content.pm.PackageManager.PERMISSION_GRANTED
import androidx.core.content.ContextCompat

interface PermissionChecker
{
    fun check(permission: String): Boolean
}
class AndroidPermissionChecker(private val application: Application): PermissionChecker {

    override fun check(permission: String): Boolean =
        ContextCompat.checkSelfPermission(application, permission) == PERMISSION_GRANTED
}