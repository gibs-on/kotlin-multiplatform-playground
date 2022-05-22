package harvest

import com.mutualmobile.harvestKmp.datamodel.*
import com.mutualmobile.harvestKmp.features.harvest.LoginDataModel
import csstype.Margin
import csstype.px
import harvest.material.TopAppBar
import kotlinx.coroutines.*
import kotlinx.css.sub
import mui.material.*
import mui.system.responsive
import mui.system.sx
import org.w3c.dom.HTMLInputElement
import react.*
import react.dom.*
import react.router.useNavigate
import setupDriver

val JSLoginScreen = VFC {
    var message by useState("")
    var email by useState("")
    var state by useState<DataState>()
    var password by useState("")

    val dataModel = LoginDataModel(onDataState = { stateNew ->
        state = stateNew
        when (stateNew) {
            is LoadingState -> {
                message = "Loading..."
            }
            is SuccessState<*> -> {
                message = "Logged In!"
                val navigator = useNavigate()
                navigator.invoke(to = "/trendingui")
            }
            Complete -> {
                message = "Completed loading!"
            }
            EmptyState -> {
                message = "Empty state"
            }
            is ErrorState -> {
                message = stateNew.throwable.message ?: "Error"
            }
        }
    })


    useEffectOnce {
        dataModel.activate()
    }

    TopAppBar {
        title = "Login Form"
        subtitle = message
    }

    Box {
        Card {
            sx {
                margin = Margin(24.px, 24.px)
            }
            Stack {
                sx {
                    margin = Margin(24.px, 24.px)
                }
                TextField {
                    this.variant = FormControlVariant.outlined
                    this.value = email
                    this.onChange = {
                        val target = it.target as HTMLInputElement
                        email = target.value
                    }
                    this.placeholder = "Enter email address"
                    sx {
                        margin = Margin(12.px, 2.px)
                    }
                }


                TextField {
                    this.variant = FormControlVariant.outlined
                    this.value = password
                    this.onChange = {
                        val target = it.target as HTMLInputElement
                        password = target.value
                    }
                    this.placeholder = "Enter Password"
                    sx {
                        margin = Margin(12.px, 2.px)
                    }
                }



                Button {
                    this.onClick = {
                        dataModel.login(email, password)
                    }
                    +"Login"
                }
            }


        }
    }

}

