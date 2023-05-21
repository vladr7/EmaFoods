package com.example.emafoods.feature.game.presentation.model

enum class Permission(val value: String) {
    GENERATE("Genereaza reteta"),
    ADD_TO_PENDING("Adauga la retetele in asteptare"),
    ACCEPT_DENY_FROM_PENDING("Accepta/Respinge reteta"),
    MAIN_LIST_VISIBLE("Vizualizeaza lista principala"),
    EDIT_MAIN("Editeaza lista principala"),
    SURPRISE("Surpriza!")
}