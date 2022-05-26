package com.mutualmobile.harvestKmp.features.harvest

import com.mutualmobile.harvestKmp.datamodel.*
import com.mutualmobile.harvestKmp.di.SpringBootAuthUseCasesComponent
import com.mutualmobile.harvestKmp.features.NetworkResponse
import com.mutualmobile.harvestKmp.features.NetworkResponse.Failure
import com.mutualmobile.harvestKmp.features.NetworkResponse.Success
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class OrgUserDashboardDataModel(private val onDataState: (DataState) -> Unit) :
    PraxisDataModel(onDataState), KoinComponent {

    private var currentLoadingJob: Job? = null
    private val useCasesComponent = SpringBootAuthUseCasesComponent()

    override fun activate() {
        if (!useCasesComponent.providerUserLoggedInUseCase().invoke()) {
            praxisCommand(NavigationPraxisCommand(""))//take to root
        }
    }

    override fun destroy() {

    }

    override fun refresh() {
    }

    fun logout() {
        dataModelScope.launch(exceptionHandler) {
            when (val result = useCasesComponent.provideLogoutUseCase().invoke()) {
                is Success<*> -> {
                    onDataState(SuccessState(result.data))
                    praxisCommand(NavigationPraxisCommand(screen = ""))
                }
                is Failure -> {
                    onDataState(ErrorState(result.throwable))
                    praxisCommand(
                        ModalPraxisCommand(
                            title = "Error",
                            result.throwable.message ?: "An Unknown error has happened"
                        )
                    )
                }
            }
        }
    }
}