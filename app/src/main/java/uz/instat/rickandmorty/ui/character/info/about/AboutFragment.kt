package uz.instat.rickandmorty.ui.character.info.about

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import uz.instat.rickandmorty.databinding.FragmentCharacterAboutBinding

const val KEY_CHARACTER_INFO_ABOUT_ID = "key_character_info_about_id"
const val KEY_CHARACTER_INFO_ABOUT_CREATED = "key_character_info_about_created"
const val KEY_CHARACTER_INFO_ABOUT_GENDER = "key_character_info_about_gender"
const val KEY_CHARACTER_INFO_ABOUT_STATUS = "key_character_info_about_status"

class AboutFragment : Fragment() {

    private var _binding: FragmentCharacterAboutBinding? = null
    private val binding get() = _binding!!

    private var characterId: Long? = null
    private var characterCreated: String? = null
    private var characterGender: String? = null
    private var characterStatus: String? = null

    companion object {
        fun newInstance(id: Long, created: String, gender: String, status: String) =
            AboutFragment().apply {
                arguments = bundleOf(
                    Pair(KEY_CHARACTER_INFO_ABOUT_ID, id),
                    Pair(KEY_CHARACTER_INFO_ABOUT_CREATED, created),
                    Pair(KEY_CHARACTER_INFO_ABOUT_GENDER, gender),
                    Pair(KEY_CHARACTER_INFO_ABOUT_STATUS, status)
                )
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true

        characterId = requireArguments().getLong(KEY_CHARACTER_INFO_ABOUT_ID)
        characterCreated = requireArguments().getString(KEY_CHARACTER_INFO_ABOUT_CREATED)
        characterGender = requireArguments().getString(KEY_CHARACTER_INFO_ABOUT_GENDER)
        characterStatus = requireArguments().getString(KEY_CHARACTER_INFO_ABOUT_STATUS)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCharacterAboutBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        binding.tvCreatedDate.text = characterCreated
        binding.tvGender.text = characterGender
        binding.tvStatus.text = characterStatus
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}