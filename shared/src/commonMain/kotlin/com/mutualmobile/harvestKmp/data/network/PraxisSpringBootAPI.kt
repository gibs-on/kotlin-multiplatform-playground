package com.mutualmobile.harvestKmp.data.network

import com.mutualmobile.harvestKmp.domain.model.request.ChangePassword
import com.mutualmobile.harvestKmp.domain.model.request.LogoutData
import com.mutualmobile.harvestKmp.domain.model.request.RefreshToken
import com.mutualmobile.harvestKmp.domain.model.request.User
import com.mutualmobile.harvestKmp.domain.model.response.FindOrgResponse
import com.mutualmobile.harvestKmp.domain.model.response.LoginResponse
import com.mutualmobile.harvestKmp.domain.model.response.SignUpResponse
import com.mutualmobile.harvestKmp.features.NetworkResponse

interface PraxisSpringBootAPI {

    suspend fun getUser(id: String): User

    suspend fun putUser(id: String): User

    suspend fun refreshToken(refreshToken: String): RefreshToken

    suspend fun existingOrgSignUp(
        firstName: String,
        lastName: String,
        company: String,
        email: String,
        password: String
    ): NetworkResponse<SignUpResponse>

    suspend fun newOrgSignUp(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        orgName: String,
        orgWebsite: String,
        orgIdentifier: String
    ): NetworkResponse<SignUpResponse>

    suspend fun login(email: String, password: String): NetworkResponse<LoginResponse>

    suspend fun logout(userId: String): LogoutData

    suspend fun fcmToken(): NetworkResponse<LoginResponse>

    suspend fun changePassword(password: String, oldPassword: String): ChangePassword

    suspend fun findOrgByIdentifier(identifier: String): NetworkResponse<FindOrgResponse>

}
