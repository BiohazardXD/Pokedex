package com.code.pokedex.presentation.viewmodel

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import androidx.paging.map
import com.code.pokedex.domain.usescase.GetAllPokemons
import com.code.pokedex.domain.usescase.SearchPokemon
import com.code.pokedex.domain.usescase.SearchPokemonById
import com.code.pokedex.framework.utils.Utils
import com.code.pokedex.presentation.model.UiAction
import com.code.pokedex.presentation.model.UiModel
import com.code.pokedex.presentation.model.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val LAST_QUERY_SCROLLED: String = "last_query_scrolled"
private const val LAST_SEARCH_QUERY: String = "last_search_query"
private const val DEFAULT_QUERY = ""
private val generations = listOf(
    "Primera generación",
    "Segunda generación",
    "Tercera generación",
    "Cuarta generación",
    "Quinta generación",
    "Sexta generación",
    "Séptima generación",
    "Octava generación"
)

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllPokemons: GetAllPokemons,
    private val searchPokemon: SearchPokemon,
    private val searchPokemonById: SearchPokemonById,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    /**
     * Stream of immutable states representative of the UI.
     */
    val state: StateFlow<UiState>

    val pagingDataFlow: Flow<PagingData<UiModel>>

    /**
     * Processor of side effects from the UI which in turn feedback into [state]
     */
    val accept: (UiAction) -> Unit

    init {
        val initialQuery: String = savedStateHandle.get(LAST_SEARCH_QUERY) ?: DEFAULT_QUERY
        val lastQueryScrolled: String = savedStateHandle.get(LAST_QUERY_SCROLLED) ?: DEFAULT_QUERY
        val actionStateFlow = MutableSharedFlow<UiAction>()
        val searches = actionStateFlow
            .filterIsInstance<UiAction.Search>()
            .distinctUntilChanged()
            .onStart { emit(UiAction.Search(query = initialQuery)) }
        val queriesScrolled = actionStateFlow
            .filterIsInstance<UiAction.Scroll>()
            .distinctUntilChanged()
            // This is shared to keep the flow "hot" while caching the last query scrolled,
            // otherwise each flatMapLatest invocation would lose the last query scrolled,
            .shareIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
                replay = 1
            )
            .onStart { emit(UiAction.Scroll(currentQuery = lastQueryScrolled)) }
        pagingDataFlow = searches
            .flatMapLatest {
                if (it.query.isEmpty()) {
                    getPokemons()
                } else {
                    if (Utils.isNumber(it.query)) {
                        searchPokemonById(it.query.toInt())
                    } else {
                        searchPokemon(it.query)
                    }
                }
            }
            .cachedIn(viewModelScope)

        state = combine(
            searches,
            queriesScrolled,
            ::Pair
        ).map { (search, scroll) ->
            UiState(
                query = search.query,
                lastQueryScrolled = scroll.currentQuery,
                // If the search query matches the scroll query, the user has scrolled
                hasNotScrolledForCurrentSearch = search.query != scroll.currentQuery
            )
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
                initialValue = UiState()
            )

        accept = { action ->
            viewModelScope.launch { actionStateFlow.emit(action) }
        }
    }

    override fun onCleared() {
        savedStateHandle[LAST_SEARCH_QUERY] = state.value.query
        savedStateHandle[LAST_QUERY_SCROLLED] = state.value.lastQueryScrolled
        super.onCleared()
    }

    private suspend fun getPokemons(): Flow<PagingData<UiModel>> =
        getAllPokemons.execute().map { pagingData ->
            pagingData.map {
                UiModel.PokemonItem(it)
            }
        }.map {
            it.insertSeparators { before, after ->
                if (after == null) return@insertSeparators null

                if (before == null) return@insertSeparators UiModel.SeparatorItem(generations[0].uppercase())

                when (after.pokemon.id) {
                    //1 -> UiModel.SeparatorItem(generations[0].uppercase())
                    152 -> UiModel.SeparatorItem(generations[1].uppercase())
                    252 -> UiModel.SeparatorItem(generations[2].uppercase())
                    387 -> UiModel.SeparatorItem(generations[3].uppercase())
                    494 -> UiModel.SeparatorItem(generations[4].uppercase())
                    650 -> UiModel.SeparatorItem(generations[5].uppercase())
                    722 -> UiModel.SeparatorItem(generations[6].uppercase())
                    810 -> UiModel.SeparatorItem(generations[7].uppercase())
                    else -> null
                }
            }
        }

    private suspend fun searchPokemon(queryString: String): Flow<PagingData<UiModel>> =
        searchPokemon.execute(queryString)
            .map { pagingData -> pagingData.map { UiModel.PokemonItem(it) } }
            .map {
                it.insertSeparators { before, after ->
                    if (after == null) {
                        // we're at the end of the list
                        return@insertSeparators null
                    }

                    if (before == null) {
                        // we're at the beginning of the list
                        return@insertSeparators UiModel.SeparatorItem(generations[0].uppercase())
                    }
                    // check between 2 items
                    when (after.pokemon.id) {
                        in 1..151 -> UiModel.SeparatorItem(generations[0].uppercase())
                        in 152..251 -> UiModel.SeparatorItem(generations[1].uppercase())
                        in 252..386 -> UiModel.SeparatorItem(generations[2].uppercase())
                        in 387..493 -> UiModel.SeparatorItem(generations[3].uppercase())
                        in 494..649 -> UiModel.SeparatorItem(generations[4].uppercase())
                        in 650..721 -> UiModel.SeparatorItem(generations[5].uppercase())
                        in 722..809 -> UiModel.SeparatorItem(generations[6].uppercase())
                        in 810..2000 -> UiModel.SeparatorItem(generations[7].uppercase())
                        else -> null
                    }
                }
            }

    private suspend fun searchPokemonById(id: Int): Flow<PagingData<UiModel>> =
        searchPokemonById.execute(id)
            .map { pagingData -> pagingData.map { UiModel.PokemonItem(it) } }
            .map {
                it.insertSeparators { before, after ->
                    if (after == null) return@insertSeparators null

                    if (before == null) return@insertSeparators UiModel.SeparatorItem(generations[0].uppercase())

                    when (after.pokemon.id) {
                        in 1..151 -> UiModel.SeparatorItem(generations[0].uppercase())
                        in 152..251 -> UiModel.SeparatorItem(generations[1].uppercase())
                        in 252..386 -> UiModel.SeparatorItem(generations[2].uppercase())
                        in 387..493 -> UiModel.SeparatorItem(generations[3].uppercase())
                        in 494..649 -> UiModel.SeparatorItem(generations[4].uppercase())
                        in 650..721 -> UiModel.SeparatorItem(generations[5].uppercase())
                        in 722..809 -> UiModel.SeparatorItem(generations[6].uppercase())
                        in 810..2000 -> UiModel.SeparatorItem(generations[7].uppercase())
                        else -> null
                    }
                }
            }
}