package com.mikeeckels.obdash.shared.models

import androidx.car.app.CarContext
import androidx.car.app.model.CarIcon
import androidx.car.app.model.Template
import androidx.core.graphics.drawable.IconCompat

interface Builder<T> {
    fun icon(carContext: CarContext, iconId: Int): CarIcon{
        return CarIcon.Builder(IconCompat.createWithResource(carContext, iconId)).build()
    }

    fun build(carContext: CarContext, items: List<T>): Template
}

interface ItemBuilder : Builder<Item>