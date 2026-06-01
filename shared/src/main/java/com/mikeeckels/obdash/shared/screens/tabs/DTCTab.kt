package com.mikeeckels.obdash.shared.screens.tabs

import androidx.car.app.CarContext
import androidx.car.app.model.Template
import com.mikeeckels.obdash.shared.R
import com.mikeeckels.obdash.shared.models.Item
import com.mikeeckels.obdash.shared.models.asSource
import com.mikeeckels.obdash.shared.builders.ListBuilder

object DTCTab {
    fun buildTemplate(carContext: CarContext): Template {
        val dtcs = listOf(
            Item("P0301".asSource, "Cylinder 1 Misfire", R.drawable.ic_dtc),
            Item("P0420".asSource, "Catalyst System Efficiency", R.drawable.ic_dtc),
            Item("P0401".asSource, "EGR A Flow Insufficient Detected"),
            Item("P0453".asSource, "EVAP System Pressure Sensor/Switch A Circuit High", R.drawable.ic_dtc)
        )

        return ListBuilder.build(carContext, dtcs)
    }
}