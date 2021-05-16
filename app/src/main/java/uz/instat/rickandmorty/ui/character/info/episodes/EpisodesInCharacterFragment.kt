package uz.instat.rickandmorty.ui.character.info.episodes

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import uz.instat.rickandmorty.common.lazyFast
import uz.instat.rickandmorty.databinding.FragmentEpisodesBinding
import uz.instat.rickandmorty.databinding.ItemEpisodeBinding
import uz.instat.rickandmorty.ui.character.adapter.LoaderStateAdapter
import uz.instat.rickandmorty.ui.episode.adapter.EpisodeAdapter

const val KEY_CHARACTER_INFO_EPISODES = "get_episode_in_character"

class EpisodesInCharacterFragment : Fragment(), EpisodeAdapter.EpisodeClickListener {

    private val mViewModel: EpisodesInCharacterViewModel by viewModels()
    private var _binding: FragmentEpisodesBinding? = null
    private val binding get() = _binding!!

    private val adapter: EpisodeAdapter by lazyFast { EpisodeAdapter(this) }
    private val loaderStateAdapter: LoaderStateAdapter by lazyFast { LoaderStateAdapter { adapter.retry() } }

    private var episodesIds: List<Long>? = null

    companion object {
        fun newInstance(ids: LongArray) = EpisodesInCharacterFragment().apply {
            arguments = bundleOf(
                Pair(KEY_CHARACTER_INFO_EPISODES, ids)
            )
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true

        episodesIds = requireArguments().getLongArray(KEY_CHARACTER_INFO_EPISODES)?.asList()
        mViewModel.fetchEpisodesInCharacter(episodesIds!!)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEpisodesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        fetchAbout()
    }

    private fun fetchAbout() {
        lifecycleScope.launch {
            try {
                mViewModel.episodesFlow.distinctUntilChanged().collectLatest { items ->
                    adapter.submitData(PagingData.from(items))
                }
            } catch (e: Exception) {
                Log.d("TAG_Character_Info", "fetchCharacters: ${e.message}")
            }
        }


    }

    private fun setupViews() {
        binding.episodesRv.layoutManager = LinearLayoutManager(requireContext())
        binding.episodesRv.adapter = adapter
        adapter.withLoadStateHeaderAndFooter(
            loaderStateAdapter,
            loaderStateAdapter)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onEpisodeClicked(binding: ItemEpisodeBinding, id: Long) {
//        TODO("Not yet implemented")
    }
}