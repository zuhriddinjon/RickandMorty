package uz.instat.rickandmorty.ui.episode

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import uz.instat.rickandmorty.common.lazyFast
import uz.instat.rickandmorty.databinding.FragmentEpisodesBinding
import uz.instat.rickandmorty.databinding.ItemEpisodeBinding
import uz.instat.rickandmorty.ui.character.adapter.LoaderStateAdapter
import uz.instat.rickandmorty.ui.episode.adapter.EpisodeAdapter

@ExperimentalPagingApi
class EpisodesFragment : Fragment(), EpisodeAdapter.EpisodeClickListener {

    private val mViewModel: EpisodesViewModel by lazyFast {
        defaultViewModelProviderFactory.create(
            EpisodesViewModel::class.java
        )
    }
    private var _binding: FragmentEpisodesBinding? = null
    private val adapter: EpisodeAdapter by lazyFast { EpisodeAdapter(this) }
    private val loaderStateAdapter: LoaderStateAdapter by lazyFast { LoaderStateAdapter { adapter.retry() } }

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEpisodesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
        fetchEpisodes()
    }

    private fun fetchEpisodes() {
        lifecycleScope.launch {
            try {
                mViewModel.fetchEpisodes().distinctUntilChanged().collectLatest {
                    adapter.submitData(it)
                }
            } catch (e: Exception) {
                Log.d("TAG_Episode", "fetchCharacters: ${e.message}")
            }
        }
    }

    private fun setUpViews() {
        binding.episodesRv.layoutManager =
            LinearLayoutManager(requireContext())
        binding.episodesRv.adapter =
            adapter.withLoadStateFooter(loaderStateAdapter)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onEpisodeClicked(binding: ItemEpisodeBinding, id: Long) {
//        TODO("Not yet implemented")
    }
}