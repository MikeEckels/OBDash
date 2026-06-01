package com.mikeeckels.obdash.shared.builders

import android.content.res.Resources
import androidx.car.app.CarContext
import androidx.car.app.model.ListTemplate
import androidx.car.app.model.ItemList
import androidx.car.app.model.Row
import androidx.car.app.model.Template
import androidx.car.app.model.Toggle
import com.mikeeckels.obdash.shared.models.Item
import com.mikeeckels.obdash.shared.models.ItemBuilder
import kotlin.collections.forEach

object ListBuilder : ItemBuilder {
    override fun build(carContext: CarContext, items: List<Item>): Template {
        val listBuilder = ItemList.Builder()

        items.forEach { item ->
            val rowBuilder = Row.Builder()
                .setTitle(item.name.resolve(carContext))
                .addText(item.value)

            if (item.iconId != Resources.ID_NULL) {
                rowBuilder.setImage(icon(carContext, item.iconId), Row.IMAGE_TYPE_ICON)
            }

            item.isChecked?.let { state ->
                rowBuilder.setToggle(Toggle.Builder { checked -> item.onCheckedChange?.invoke(checked) }
                    .setChecked(state)
                    .build()
                )
            }

            listBuilder.addItem(rowBuilder.build())
        }

        return ListTemplate.Builder().setSingleList(listBuilder.build()).build()
    }
}