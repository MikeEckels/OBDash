package com.mikeeckels.obdash.shared.builders

import androidx.car.app.CarContext
import androidx.car.app.model.GridItem
import androidx.car.app.model.GridTemplate
import androidx.car.app.model.ItemList
import com.mikeeckels.obdash.shared.models.Item
import com.mikeeckels.obdash.shared.models.ItemBuilder

object GridBuilder : ItemBuilder{
    override fun build(carContext: CarContext, items: List<Item>): GridTemplate {
        val listBuilder = ItemList.Builder()

        items.forEach { item ->
            val text = if (item.unitType.isNotEmpty()) {
                "${item.value}${item.unitType}"
            } else {
                item.value
            }

            listBuilder.addItem(GridItem.Builder()
                .setTitle(item.name.resolve(carContext))
                .setText(text)
                .setImage(icon(carContext, item.iconId), GridItem.IMAGE_TYPE_ICON)
                .build())
        }

        return GridTemplate.Builder().setSingleList(listBuilder.build()).build()
    }
}