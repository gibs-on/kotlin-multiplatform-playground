package com.mutualmobile.harvestKmp.domain.usecases.praxisSpringBoot

import com.mutualmobile.harvestKmp.data.network.PraxisSpringBootAPI
import com.mutualmobile.harvestKmp.domain.model.response.LoginResponse
import com.mutualmobile.harvestKmp.features.NetworkResponse
import com.mutualmobile.harvestKmp.validators.LoginFormValidator

class LoginUseCase(private val praxisSpringBootAPI: PraxisSpringBootAPI) {
    suspend operator fun invoke(email: String, password: String): NetworkResponse<LoginResponse> {
        LoginFormValidator().invoke(email, password)
        return praxisSpringBootAPI.login(email, password)
    }
}