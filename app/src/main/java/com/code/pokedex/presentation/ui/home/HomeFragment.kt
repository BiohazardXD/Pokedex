package com.code.pokedex.presentation.ui.home

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.code.pokedex.databinding.FragmentHomeBinding
import com.code.pokedex.presentation.model.UiAction
import com.code.pokedex.presentation.model.UiModel
import com.code.pokedex.presentation.model.UiState
import com.code.pokedex.presentation.ui.home.adapter.PokemonAdapter
import com.code.pokedex.presentation.ui.home.adapter.PokemonLoadStateAdapter
import com.code.pokedex.presentation.ui.home.adapter.RemotePresentationState
import com.code.pokedex.presentation.ui.home.adapter.asRemotePresentationState
import com.code.pokedex.presentation.viewmodel.HomeViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val viewModel by viewModels<HomeViewModel>()

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setUpUI()

        return root
    }

    fun setUpUI() {
        val decoration = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        binding.recycler.addItemDecoration(decoration)

        binding.bindState(
            uiState = viewModel.state,
            pagingData = viewModel.pagingDataFlow,
            uiActions = viewModel.accept
        )
    }

    private fun FragmentHomeBinding.bindState(
        uiState: StateFlow<UiState>,
        pagingData: Flow<PagingData<UiModel>>,
        uiActions: (UiAction) -> Unit
    ) {
        val pokemonAdapter = PokemonAdapter()
        val header = PokemonLoadStateAdapter { pokemonAdapter.retry() }
        recycler.adapter = pokemonAdapter.withLoadStateHeaderAndFooter(
            header = header,
            footer = PokemonLoadStateAdapter { pokemonAdapter.retry() }
        )
        bindSearch(
            uiState = uiState,
            onQueryChanged = uiActions
        )
        bindList(
            header = header,
            pokemonAdapter = pokemonAdapter,
            uiState = uiState,
            pagingData = pagingData,
            onScrollChanged = uiActions
        )
    }

    private fun FragmentHomeBinding.bindSearch(
        uiState: StateFlow<UiState>,
        onQueryChanged: (UiAction.Search) -> Unit
    ) {
        search.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                updateRepoListFromInput(onQueryChanged)
                true
            } else {
                false
            }
        }
        search.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                updateRepoListFromInput(onQueryChanged)
                true
            } else {
                false
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            uiState
                .map { it.query }
                .distinctUntilChanged()
                .collect(search::setText)
        }
    }

    private fun FragmentHomeBinding.updateRepoListFromInput(onQueryChanged: (UiAction.Search) -> Unit) {
        search.text.trim().let {
            if (it.isNotEmpty()) {
                recycler.scrollToPosition(0)
                onQueryChanged(UiAction.Search(query = it.toString()))
            }
        }
    }

    private fun FragmentHomeBinding.bindList(
        header: PokemonLoadStateAdapter,
        pokemonAdapter: PokemonAdapter,
        uiState: StateFlow<UiState>,
        pagingData: Flow<PagingData<UiModel>>,
        onScrollChanged: (UiAction.Scroll) -> Unit
    ) {
        retryButton.setOnClickListener { pokemonAdapter.retry() }
        recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy != 0) onScrollChanged(UiAction.Scroll(currentQuery = uiState.value.query))
            }
        })
        val notLoading = pokemonAdapter.loadStateFlow
            .asRemotePresentationState()
            .map { it == RemotePresentationState.PRESENTED }

        val hasNotScrolledForCurrentSearch = uiState
            .map { it.hasNotScrolledForCurrentSearch }
            .distinctUntilChanged()

        val shouldScrollToTop = combine(
            notLoading,
            hasNotScrolledForCurrentSearch,
            Boolean::and
        )
            .distinctUntilChanged()

        lifecycleScope.launch {
            pagingData.collectLatest(pokemonAdapter::submitData)
        }

        lifecycleScope.launch {
            shouldScrollToTop.collect { shouldScroll ->
                if (shouldScroll) recycler.scrollToPosition(0)
            }
        }

        lifecycleScope.launch {
            pokemonAdapter.loadStateFlow.collect { loadState ->
                // Show a retry header if there was an error refreshing, and items were previously
                // cached OR default to the default prepend state
                header.loadState = loadState.mediator
                    ?.refresh
                    ?.takeIf { it is LoadState.Error && pokemonAdapter.itemCount > 0 }
                    ?: loadState.prepend

                val isListEmpty = loadState.refresh is LoadState.NotLoading && pokemonAdapter.itemCount == 0
                // show empty list
                emptyList.isVisible = isListEmpty
                // Only show the list if refresh succeeds, either from the the local db or the remote.
                recycler.isVisible =  loadState.source.refresh is LoadState.NotLoading || loadState.mediator?.refresh is LoadState.NotLoading
                // Show loading spinner during initial load or refresh.
                progressBar.isVisible = loadState.mediator?.refresh is LoadState.Loading
                // Show the retry state if initial load or refresh fails.
                retryButton.isVisible = loadState.mediator?.refresh is LoadState.Error && pokemonAdapter.itemCount == 0
                // Toast on any error, regardless of whether it came from RemoteMediator or PagingSource
                val errorState = loadState.source.append as? LoadState.Error
                    ?: loadState.source.prepend as? LoadState.Error
                    ?: loadState.append as? LoadState.Error
                    ?: loadState.prepend as? LoadState.Error
                errorState?.let {
                    /*Toast.makeText(
                        requireContext(),
                        "\uD83D\uDE28 Wooops ${it.error}",
                        Toast.LENGTH_LONG
                    ).show()*/
                    Snackbar.make(binding.recycler, "\uD83D\uDE28 Wooops ${it.error}", Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}