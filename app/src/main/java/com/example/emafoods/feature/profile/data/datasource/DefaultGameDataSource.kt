package com.example.emafoods.feature.profile.data.datasource

import com.example.emafoods.feature.profile.domain.model.UserLevel
import javax.inject.Inject

class DefaultGameDataSource @Inject constructor() : GameDataSource {
    override fun listOfXpActions() =
        listOf<String>(
            "Generare reteta",
            "Reteta adaugata",
            "Reteta acceptata de admin",
            "Review aplicatie",
            "Deschizi aplicatia pentru prima data pe ziua respectiva",
            "Accepti/respingi o reteta (admin)",
        )

    override fun userLevel(): UserLevel =
        UserLevel.ADD_DECLINE

}