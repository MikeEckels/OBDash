package com.mikeeckels.obdash.shared.screens

import androidx.car.app.CarContext
import androidx.car.app.Screen
import androidx.car.app.model.Template
import androidx.car.app.model.TabTemplate
import com.mikeeckels.obdash.shared.R
import com.mikeeckels.obdash.shared.models.Item
import com.mikeeckels.obdash.shared.models.asSource
import com.mikeeckels.obdash.shared.models.toTab
import com.mikeeckels.obdash.shared.screens.tabs.DTCTab
import com.mikeeckels.obdash.shared.screens.tabs.GaugesTab
import com.mikeeckels.obdash.shared.screens.tabs.GraphsTab
import com.mikeeckels.obdash.shared.screens.tabs.SettingsTab
import com.mikeeckels.obdash.shared.builders.TabBuilder

class HomeScreen(carContext: CarContext) : Screen(carContext) {
    private var activeTabId = ""
    private val tabs = listOf(
        Item(R.string.HomeTab1.asSource, iconId = R.drawable.ic_gauge).toTab {GaugesTab.buildTemplate(it)},
        Item(R.string.HomeTab2.asSource, iconId = R.drawable.ic_graph).toTab {GraphsTab.buildTemplate(it)},
        Item(R.string.HomeTab3.asSource, iconId = R.drawable.ic_dtc).toTab {DTCTab.buildTemplate(it)},
        Item(R.string.HomeTab4.asSource, iconId = R.drawable.ic_settings).toTab {SettingsTab.buildTemplate(it) {invalidate()}}
    )

    init { activeTabId = tabs.first().contentId }

    private val tabCallback = object : TabTemplate.TabCallback {
        override fun onTabSelected(tabContentId: String) {
            activeTabId = tabContentId
            invalidate()
        }
    }

    override fun onGetTemplate(): Template {
        val tabBuilder = TabBuilder(activeTabId, tabCallback)
        return tabBuilder.build(carContext, tabs)
    }
}