package com.mutualmobile.harvestKmp.domain.usecases.praxisSpringBootAuth

import com.mutualmobile.harvestKmp.data.network.PraxisSpringBootAPI
import com.mutualmobile.harvestKmp.domain.model.request.HarvestOrganization
import com.mutualmobile.harvestKmp.domain.model.response.ApiResponse
import com.mutualmobile.harvestKmp.features.NetworkResponse

class ExistingOrgSignUpUseCase(private val praxisSpringBootAPI: PraxisSpringBootAPI) {
    suspend fun perform(
        firstName: String,
        lastName: String,
        company: String,
        email: String,
        password: String
    ): NetworkResponse<ApiResponse<HarvestOrganization>> {
        return praxisSpringBootAPI.existingOrgSignUp(firstName, lastName, company, email, password)
    }
}