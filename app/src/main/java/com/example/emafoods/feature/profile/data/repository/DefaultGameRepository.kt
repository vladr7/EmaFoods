package com.example.emafoods.feature.profile.data.repository

import com.example.emafoods.feature.profile.data.datasource.GameDataSource
import com.example.emafoods.feature.profile.domain.usecase.repository.GameRepository
import javax.inject.Inject

class DefaultGameRepository @Inject constructor(
    private val gameDataSource: GameDataSource
): GameRepository {

    override fun listOfXpActions(): List<String> =
        gameDataSource.listOfXpActions()
}
