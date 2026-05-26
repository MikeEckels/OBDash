package com.example.obdash.shared

import androidx.car.app.CarContext
import androidx.car.app.Screen
import androidx.car.app.model.Action
import androidx.car.app.model.MessageTemplate
import androidx.car.app.model.Template

class MyCarAppScreen(carContext: CarContext) : Screen(carContext) {
    val centerText = carContext.getString(R.string.CenterText)
    override fun onGetTemplate(): Template {
        return MessageTemplate.Builder(centerText)
            .setHeaderAction(Action.APP_ICON)
            .setTitle("Test Screen")
            .build()
    }
}