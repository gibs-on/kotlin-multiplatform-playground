package com.mutualmobile.harvestKmp.domain.usecases.praxisSpringBoot

import com.mutualmobile.harvestKmp.data.network.PraxisSpringBootAPI
import com.mutualmobile.harvestKmp.domain.model.request.HarvestOrganization
import com.mutualmobile.harvestKmp.domain.model.response.ApiResponse
import com.mutualmobile.harvestKmp.features.NetworkResponse

class ForgotPasswordUseCase(private val praxisSpringBootAPI: PraxisSpringBootAPI) {
    suspend operator fun invoke(
        email: String
    ): NetworkResponse<ApiResponse<HarvestOrganization>> {
        return praxisSpringBootAPI.forgotPassword(email)
    }
}