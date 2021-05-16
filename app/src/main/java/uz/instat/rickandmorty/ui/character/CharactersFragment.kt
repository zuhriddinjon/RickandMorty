package uz.instat.rickandmorty.ui.character

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.instat.rickandmorty.R
import uz.instat.rickandmorty.common.lazyFast
import uz.instat.rickandmorty.data.model.character.CharacterModel
import uz.instat.rickandmorty.databinding.FragmentCharactersBinding
import uz.instat.rickandmorty.databinding.ItemCharacterBinding
import uz.instat.rickandmorty.ui.character.adapter.CharacterAdapter
import uz.instat.rickandmorty.ui.character.adapter.LoaderStateAdapter
import uz.instat.rickandmorty.ui.character.info.KEY_CHARACTER_INFO_MODEL

@ExperimentalPagingApi
class CharactersFragment : Fragment(), CharacterAdapter.CharacterClickListener {

    private val mViewModel: CharactersViewModel /*by viewModels()*/
            by lazyFast { defaultViewModelProviderFactory.create(CharactersViewModel::class.java) }
    private var _binding: FragmentCharactersBinding? = null
    private val adapter: CharacterAdapter by lazyFast { CharacterAdapter(this) }
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
        _binding = FragmentCharactersBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
        fetchCharacters()
    }

    private fun fetchCharacters() {
        lifecycleScope.launch {
            try {
                mViewModel.charactersFlow.collectLatest {
                    adapter.submitData(it)
                }
            } catch (e: Exception) {
                Log.d("TAG_Character", "fetchCharacters: ${e.message}")
            }
        }
    }


    private fun setUpViews() {
        binding.charactersRv.layoutManager = //GridLayoutManager(requireContext(), 2)
            LinearLayoutManager(requireContext())
        binding.charactersRv.adapter = adapter
        adapter.withLoadStateHeaderAndFooter(
            loaderStateAdapter,
            loaderStateAdapter)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCharacterClicked(binding: ItemCharacterBinding, model: CharacterModel) {
        val args = bundleOf(
            Pair(KEY_CHARACTER_INFO_MODEL, model)
        )
        findNavController().navigate(R.id.nav_character_info, args)
    }
}