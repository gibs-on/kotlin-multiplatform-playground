package orguser

import com.mutualmobile.harvestKmp.datamodel.*
import com.mutualmobile.harvestKmp.domain.model.response.ApiResponse
import com.mutualmobile.harvestKmp.domain.model.response.OrgProjectResponse
import com.mutualmobile.harvestKmp.features.harvest.FindProjectsInOrgDataModel
import com.mutualmobile.harvestKmp.features.harvest.OrgProjectDataModel
import csstype.*
import emotion.react.css
import kotlinx.browser.window
import kotlinx.js.jso
import mui.material.*
import mui.icons.material.Add
import mui.system.sx
import react.*
import react.router.useNavigate
import kotlin.js.Date

val JsOrgProjectsScreen = VFC {
    var message by useState("")
    var createRequested by useState(false)
    var projectKey by useState("")
    var selectedProject by useState<OrgProjectResponse?>(null)
    val navigator = useNavigate()
    var projects by useState<List<OrgProjectResponse>>()
    val limit = 20
    var currentPage by useState(0)
    var totalPages by useState(0)
    var isLoading by useState(false)

    val dataModel = FindProjectsInOrgDataModel(onDataState = { stateNew ->
        isLoading = stateNew is LoadingState
        when (stateNew) {
            is LoadingState -> {
                message = "Loading..."
            }
            is SuccessState<*> -> {
                message = try {
                    val response =
                        (stateNew.data as ApiResponse<Pair<Int, List<OrgProjectResponse>>>)
                    projects = response.data?.second
                    totalPages = response.data?.first ?: 0
                    response.message ?: "Some message"
                } catch (ex: Exception) {
                    ex.message ?: ""
                }
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
        dataModel.findProjectInOrg(
            offset = currentPage, limit = limit, orgId = null
        )
    }

    Box {
        if (isLoading) {
            CircularProgress()
        } else {
            Box {
                sx {
                    position = Position.relative
                    transform = translatez(0.px)
                    flexGrow = number(1.0)
                    alignSelf = AlignSelf.flexEnd
                    alignItems = AlignItems.baseline
                }
                Fab {
                    variant = FabVariant.extended
                    sx {
                        transform = translatez(0.px)
                        bottom = 16.px
                        right = 16.px
                    }
                    color = FabColor.primary
                    Add()
                    onClick = {
                        createRequested = true
                    }
                    +"Create Project"
                }
                Pagination {
                    count = totalPages
                    page = currentPage
                    onChange = { event, value ->
                        currentPage = value.toInt()
                        dataModel.findProjectInOrg(
                            offset = value.toInt().minus(1), limit = limit, orgId = null
                        )
                    }

                }
                List {
                    projects?.map { project ->
                        val format: dynamic = kotlinext.js.require("date-fns").format
                        val start =
                            format(Date(project.startDate.toString()), "yyyy-MM-dd") as String
                        val end = format(Date(project.endDate.toString()), "yyyy-MM-dd") as? String
                        ListItem {
                            ListItemText {
                                primary =
                                    ReactNode("Name: ${project.name ?: ""}\nClient: ${project.client ?: ""}")
                                secondary =
                                    ReactNode("Start Date: $start EndDate: $end")
                            }
                            onClick = {
                                selectedProject = project
                                createRequested = true
                            }
                        }
                    }
                }
            }
        }




        JsCreateProject {
            key = selectedProject?.id ?: "1"
            drawerOpen = createRequested
            projectClicked = selectedProject
            onOpen = {
                createRequested = true
            }
            onClose = {
                createRequested = false
                selectedProject = null
                currentPage = 0
                dataModel.findProjectInOrg(
                    offset = currentPage, limit = limit, orgId = null
                )
            }
        }
    }
}