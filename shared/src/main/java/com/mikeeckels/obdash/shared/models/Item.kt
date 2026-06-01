package com.mikeeckels.obdash.shared.models

import android.content.res.Resources.ID_NULL
import androidx.car.app.model.Template
import androidx.car.app.CarContext
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class Item (
    val name: TextSource,
    val value: String = "",
    val iconId: Int = ID_NULL,
    val unitType: String = "",
    val isChecked: Boolean? = null,
    val onCheckedChange: ((Boolean) -> Unit)? = null
)

data class TabItem(
    val item: Item,
    val contentId: String,
    val contentProvider: (CarContext) -> Template
)

@OptIn(ExperimentalUuidApi::class)
fun Item.toTab(contentProvider: (CarContext) -> Template): TabItem {
    val longNameHash = this.name.hashCode().toLong()
    val uniqueId = Uuid.fromLongs(longNameHash, longNameHash).toString()

    return TabItem(this, uniqueId, contentProvider)
}