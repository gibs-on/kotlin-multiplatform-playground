package harvest

import com.mutualmobile.harvestKmp.datamodel.*
import com.mutualmobile.harvestKmp.datamodel.Routes.Screen.withOrgId
import com.mutualmobile.harvestKmp.domain.model.response.LoginResponse
import com.mutualmobile.harvestKmp.features.harvest.LoginDataModel
import common.Themes
import components.AppThemeContext
import csstype.Margin
import csstype.px
import harvest.material.TopAppBar
import kotlinx.browser.window
import kotlinx.js.jso
import mui.material.*
import mui.system.sx
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.MediaQueryList
import org.w3c.dom.events.Event
import org.w3c.dom.events.EventListener
import react.*
import react.dom.*
import react.dom.aria.ariaLabel
import react.router.NavigateFunction
import react.router.NavigateOptions
import react.router.dom.useSearchParams
import react.router.useLocation
import react.router.useNavigate
import setupFcmPush
import kotlin.js.Json


val JSLoginScreen = VFC {
    var message by useState("")
    var email by useState("")
    val searchParams = useSearchParams();

    val organizationId: String? = searchParams.component1().get(Routes.Keys.orgIdentifier)
    val orgId: String? = searchParams.component1().get(Routes.Keys.orgId)

    var password by useState("")
    val navigator = useNavigate()

    val dataModel = LoginDataModel(onDataState = { stateNew ->
        when (stateNew) {
            is LoadingState -> {
                message = "Loading..."
            }
            is SuccessState<*> -> {
                message = (stateNew.data as LoginResponse).message ?: "Some message"
                setupFcmPush()
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

    dataModel.praxisCommand = { newCommand ->
        when (newCommand) {
            is NavigationPraxisCommand -> {
                navigator(BROWSER_SCREEN_ROUTE_SEPARATOR + newCommand.screen)
            }
            is ModalPraxisCommand -> {
                window.alert(newCommand.title + "\n" + newCommand.message)
            }
        }
    }


    useEffectOnce {
        dataModel.activate()
    }

    TopAppBar {
        title = "${organizationId ?: ""} Login Form"
        subtitle = message
    }
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
                this.placeholder = "Work Email"
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
                onClick = {
                    navigator("/forgotPassword")
                }
                +"Forgot Password"
            }

            Button {
                this.onClick = {
                    dataModel.login(email, password)
                }
                +"Login"
            }

            organizationId?.takeIf { it.isNotEmpty() }?.let {
                Button {
                    this.variant = ButtonVariant.contained
                    sx {
                        this.margin = Margin(24.px, 4.px)
                    }
                    +"Signup with $organizationId"
                    onClick = {
                        navigator(
                            BROWSER_SCREEN_ROUTE_SEPARATOR +
                                    Routes.Screen.SIGNUP.withOrgId(organizationId, orgId)
                        )
                    }
                }
            }
        }
    }
}

fun navigateRoot(navigator: NavigateFunction) {
    navigator.invoke(to = BROWSER_SCREEN_ROUTE_SEPARATOR, options = jso {
        replace = true
    })
}