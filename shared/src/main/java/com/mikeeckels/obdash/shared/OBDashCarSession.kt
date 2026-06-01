package com.mikeeckels.obdash.shared

import android.content.Intent
import androidx.car.app.Screen
import androidx.car.app.Session
import com.mikeeckels.obdash.shared.screens.HomeScreen

class OBDashCarSession : Session() {
    override fun onCreateScreen(intent: Intent): Screen {
        return HomeScreen(carContext)
    }
}