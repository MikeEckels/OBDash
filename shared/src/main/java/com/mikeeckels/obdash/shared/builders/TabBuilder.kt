package com.mikeeckels.obdash.shared.builders

import androidx.car.app.CarContext
import androidx.car.app.model.Action
import androidx.car.app.model.Tab
import androidx.car.app.model.TabContents
import androidx.car.app.model.TabTemplate
import com.mikeeckels.obdash.shared.models.TabItem
import com.mikeeckels.obdash.shared.models.Builder

class TabBuilder(private val activeTabId: String, private val callback: TabTemplate.TabCallback) : Builder<TabItem> {
    override fun build(carContext: CarContext, items: List<TabItem>) : TabTemplate {
        val tabTemplateBuilder = TabTemplate.Builder(callback).setActiveTabContentId(activeTabId).setHeaderAction(Action.APP_ICON)

        items.forEach { tabItem ->
            val tabBuilder = Tab.Builder()
                .setTitle(tabItem.item.name.resolve(carContext))
                .setContentId(tabItem.contentId)
                .setIcon(icon(carContext, tabItem.item.iconId))

            tabTemplateBuilder.addTab(tabBuilder.build())
        }

        val activeTabItem = items.firstOrNull { it.contentId == activeTabId } ?: items.first()
        val innerTemplate = activeTabItem.contentProvider(carContext)

        tabTemplateBuilder.setTabContents(TabContents.Builder(innerTemplate).build())
        return tabTemplateBuilder.build()
    }
}