package com.mikeeckels.obdash.shared.screens.tabs

import androidx.car.app.CarContext
import androidx.car.app.model.Template
import com.mikeeckels.obdash.shared.R
import com.mikeeckels.obdash.shared.models.Item
import com.mikeeckels.obdash.shared.models.asSource
import com.mikeeckels.obdash.shared.builders.GridBuilder

object GaugesTab {
    fun buildTemplate(carContext: CarContext): Template {

        val gauges = listOf(
            Item("RPM".asSource, "750", R.drawable.ic_gauge, "RPM"),
            Item("PSI".asSource, "2000", R.drawable.ic_gauge, "PSI"),
            Item("DTC's".asSource, "6", R.drawable.ic_gauge),
            Item("EGR".asSource, "300",R.drawable.ic_gauge, "%")
        )

        return GridBuilder.build(carContext, gauges)
    }
}