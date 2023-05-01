package com.example.emafoods.core.presentation.models.helper

import com.example.emafoods.core.data.models.helper.Model


interface DataModelMapper<M : Model, VD : ViewData> {
    fun mapToModel(viewData: VD): M

    fun mapToViewData(model: M): VD
}