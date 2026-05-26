package com.example.obdash.shared

import androidx.car.app.CarAppService
import androidx.car.app.Session
import androidx.car.app.validation.HostValidator
import android.content.Context
import android.content.pm.ApplicationInfo

fun checkDebugMode(context: Context): Boolean {
    return (context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
}

class MyCarAppService : CarAppService() {
    override fun createHostValidator(): HostValidator {
        return if (checkDebugMode(this)) {
            HostValidator.ALLOW_ALL_HOSTS_VALIDATOR
        } else {
            HostValidator.Builder(this)
                .addAllowedHosts(R.array.hosts_allowlist)
                .build()
        }
    }

    override fun onCreateSession(): Session {
        return MyCarAppSession()
    }
}