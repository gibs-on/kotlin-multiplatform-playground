package com.baseio.kmm.domain.usecases.praxisSpringBootAuth

import com.baseio.kmm.data.network.PraxisSpringBootAPI
import com.baseio.kmm.domain.model.response.SuccessResponse
import com.baseio.kmm.features.NetworkResponse

class SignUpUseCase(private val praxisSpringBootAPI: PraxisSpringBootAPI) {
    suspend fun perform(email: String, password: String): NetworkResponse<SuccessResponse> {
        return praxisSpringBootAPI.signup(email, password)
    }
}