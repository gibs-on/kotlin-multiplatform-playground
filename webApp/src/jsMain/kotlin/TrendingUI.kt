import com.baseio.kmm.domain.model.GithubReposItem
import com.baseio.kmm.features.trending.TrendingDataModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import react.*
import react.dom.*

external interface TrendingProps : Props

val TrendingUI = fc<TrendingProps> {
  var trendingRepos: List<GithubReposItem> by useState(emptyList())
  var message: String by useState("")
  var state by useState<TrendingDataModel.UiState>()

  val dataModel = TrendingDataModel(onDataState = { stateNew ->
    state = stateNew
    when (stateNew) {
      is TrendingDataModel.LoadingState -> {
        message = "Loading..."
      }
      is TrendingDataModel.SuccessState -> {
        trendingRepos = stateNew.trendingList
        message = "Found repos..."
      }
      TrendingDataModel.Complete -> {
        message = "Completed loading!"
      }
      TrendingDataModel.EmptyState -> {
        message = "Emty state"
      }
      is TrendingDataModel.ErrorState -> {
        message = stateNew.throwable.message ?: "Error"
      }
    }
  })

  useEffectOnce {
    MainScope().launch {
      setupDriver()
      message = "Driver created";
      dataModel.activate()
      message = "DataModel activated";
    }
  }

  h1 {
    +"Trending Kotlin Repositories"
  }
  h1 {
    +"Status :"
    +message
  }

  div {
    for (repo in trendingRepos) {
      img(src = repo.avatar, classes = "img") {
        this.attrs.height = "100"
        this.attrs.width = "100"
      }
      div {
        p {
          key = repo.url
          +"${repo.name}: ${repo.author}"
        }
        a {
          attrs {
            href = repo.url.toString()
            target = repo.url.toString()
          }
          p {
            +repo.url.toString()
          }
        }
      }

    }
  }

}

