package com.mikeeckels.obdash.shared.screens.tabs

import androidx.car.app.CarContext
import androidx.car.app.model.Template
import com.mikeeckels.obdash.shared.R
import com.mikeeckels.obdash.shared.models.Item
import com.mikeeckels.obdash.shared.models.asSource
import com.mikeeckels.obdash.shared.builders.GridBuilder

object GraphsTab {
    fun buildTemplate(carContext: CarContext): Template {

        val graphs = listOf(
            Item("RPM".asSource, "1200", R.drawable.ic_graph, "RPM"),
            Item("Throttle".asSource, "56", R.drawable.ic_graph, "%"),
            Item("DTC's".asSource, "8", R.drawable.ic_graph),
            Item("Steering Angle".asSource, "30",R.drawable.ic_graph, "°")
        )

        return GridBuilder.build(carContext, graphs)
    }
}