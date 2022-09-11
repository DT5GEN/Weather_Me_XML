package com.deeppowercrew.weathermexml

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment


fun Fragment.isPermissionsGranted(permissions: String): Boolean {
    return ContextCompat.checkSelfPermission(
        activity as AppCompatActivity, permissions
    ) == PackageManager.PERMISSION_GRANTED

}