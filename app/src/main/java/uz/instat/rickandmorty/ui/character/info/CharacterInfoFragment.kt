package uz.instat.rickandmorty.ui.character.info

import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import uz.instat.rickandmorty.R
import uz.instat.rickandmorty.databinding.FragmentCharacterInfoBinding

const val KEY_CHARACTER_INFO_ID = "key_character_info_id"

class CharacterInfoFragment : Fragment() {

    private val mViewModel: CharacterInfoViewModel by viewModels()
    private var _binding: FragmentCharacterInfoBinding? = null
    private val binding get() = _binding!!

    private var characterId: Long? = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true

        characterId = requireArguments().getLong(KEY_CHARACTER_INFO_ID)
        sharedElementEnterTransition =
            TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.fade)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCharacterInfoBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchCharacter()
    }

    private fun fetchCharacter() {
        lifecycleScope.launch {
            try {
                mViewModel.fetchCharacter(characterId!!).distinctUntilChanged()
                    .collectLatest { item ->
                        binding.tvName.text = item.name
                        binding.tvSpecies.text = item.species

                        Glide.with(requireContext())
                            .load(item.image)
                            .circleCrop()
                            .placeholder(R.drawable.ic_morty)
                            .into(binding.ivCharacterInfo)
                    }
            } catch (e: Exception) {
                Log.d("TAG_Character_Info", "fetchCharacters: ${e.message}")
            }
        }


    }


}