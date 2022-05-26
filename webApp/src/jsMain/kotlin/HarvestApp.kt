import com.mutualmobile.harvestKmp.datamodel.BROWSER_QUERY
import com.mutualmobile.harvestKmp.datamodel.BROWSER_SCREEN_ROUTE_SEPARATOR
import components.ThemeModule
import harvest.*
import react.router.Route
import react.router.Routes
import react.router.dom.HashRouter
import react.FC
import react.Props
import react.create
import react.router.dom.BrowserRouter
import workspace.JsWorkspaceFindScreen
import com.mutualmobile.harvestKmp.datamodel.Routes.Screen
import orguser.UserDashboardUI

external interface AppProps : Props

val HarvestApp = FC<AppProps> {
    BrowserRouter {
        ThemeModule {
            Routes {
                Route {
                    path = "*"
                    this.element = JSNotFound.create()
                }
                Route {
                    this.index = true
                    this.element = JsWorkspaceFindScreen.create()
                    this.path = BROWSER_SCREEN_ROUTE_SEPARATOR
                }
                Route {
                    this.path = BROWSER_SCREEN_ROUTE_SEPARATOR + Screen.LOGIN
                    this.element = JSLoginScreen.create()
                }
                Route {
                    this.path = BROWSER_SCREEN_ROUTE_SEPARATOR + Screen.FORGOT_PASSWORD
                    this.element = ForgotPasswordUI.create()
                }
                Route {
                    this.path = BROWSER_SCREEN_ROUTE_SEPARATOR + Screen.CHANGE_PASSWORD
                    this.element = ChangePasswordUI.create()
                }
                Route {
                    this.path = BROWSER_SCREEN_ROUTE_SEPARATOR + Screen.RESET_PASSWORD
                    this.element = ResetPasswordScreen.create()
                }
                Route {
                    this.path = BROWSER_SCREEN_ROUTE_SEPARATOR + Screen.TRENDING_UI
                    this.element = TrendingUI.create()
                }
                Route {
                    this.path = BROWSER_SCREEN_ROUTE_SEPARATOR + Screen.SIGNUP
                    this.element = JSSignupScreen.create()
                }
                Route {
                    this.path = BROWSER_SCREEN_ROUTE_SEPARATOR + Screen.ORG_USER_DASHBOARD
                    this.element = UserDashboardUI.create()
                }
            }
        }
    }
}