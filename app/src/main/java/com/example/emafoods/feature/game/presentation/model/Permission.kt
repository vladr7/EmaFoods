package com.example.emafoods.feature.game.presentation.model

enum class Permission(val value: String) {
    GENERATE("Generează rețete"),
    ADD_TO_PENDING("Adaugă o rețetă în lista de așteptare"),
    ACCEPT_DENY_FROM_PENDING("Acceptă/Respinge rețete din lista de așteptare"),
    MAIN_LIST_VISIBLE("Vizualizează întreaga listă de rețete"),
    EDIT_PENDING("Editează reteta din lista de așteptare"),
    SURPRISE("Surpriză!")
}