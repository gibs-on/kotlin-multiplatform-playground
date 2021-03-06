package com.mutualmobile.harvestKmp.features.harvest

import com.mutualmobile.harvestKmp.datamodel.*
import com.mutualmobile.harvestKmp.di.SharedComponent
import com.mutualmobile.harvestKmp.di.SpringBootAuthUseCasesComponent
import com.mutualmobile.harvestKmp.features.NetworkResponse
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class ChangePasswordDataModel(private val onDataState: (DataState) -> Unit) :
    PraxisDataModel(onDataState), KoinComponent {

    private var currentLoadingJob: Job? = null
    private val useCasesComponent = SpringBootAuthUseCasesComponent()
    private val changePasswordUseCase = useCasesComponent.provideChangePasswordUseCase()

    override fun activate() {
    }

    override fun destroy() {
        dataModelScope.cancel()
    }

    override fun refresh() {

    }

    fun changePassWord(password: String, oldPassword: String) {
        currentLoadingJob?.cancel()
        currentLoadingJob = dataModelScope.launch {
            onDataState(LoadingState)
            when (val changePasswordResponse = changePasswordUseCase(password, oldPassword)) {
                is NetworkResponse.Success -> {
                    print("ChangePassword Successful, ${changePasswordResponse.data.message}")
                    onDataState(SuccessState(changePasswordResponse.data))
                }
                is NetworkResponse.Failure -> {
                    print("ChangePassword Failed, ${changePasswordResponse.throwable.message}")
                    onDataState(ErrorState(changePasswordResponse.throwable))
                }
                is NetworkResponse.Unauthorized -> {
                    settings.clear()
                    praxisCommand(ModalPraxisCommand("Unauthorized","Please login again!"))
                    praxisCommand(NavigationPraxisCommand(""))
                }
            }
        }
    }
}