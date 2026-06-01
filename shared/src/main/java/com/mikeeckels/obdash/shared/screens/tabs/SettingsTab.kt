package com.mikeeckels.obdash.shared.screens.tabs

import androidx.car.app.CarContext
import androidx.car.app.model.Template
import com.mikeeckels.obdash.shared.models.Item
import com.mikeeckels.obdash.shared.models.asSource
import com.mikeeckels.obdash.shared.builders.ListBuilder

object SettingsTab {
    private var setting1 = false
    private var setting2 = false
    private var setting3 = false
    private var setting4 = false

    fun buildTemplate(carContext: CarContext, onStateChanged: () -> Unit): Template {
        val settings = listOf(
            Item("Setting 1".asSource, if (setting1) "ON" else "OFF", isChecked = setting1) { setting1 = it; onStateChanged() },
            Item("Setting 2".asSource, if (setting2) "ON" else "OFF", isChecked = setting2) { setting2 = it; onStateChanged() },
            Item("Setting 3".asSource, if (setting3) "ON" else "OFF", isChecked = setting3) { setting3 = it; onStateChanged() },
            Item("Setting 4".asSource, if (setting4) "ON" else "OFF", isChecked = setting4) { setting4 = it; onStateChanged() }
        )
        return ListBuilder.build(carContext, settings)
    }
}
