package uz.instat.rickandmorty.ui.character.info

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import uz.instat.rickandmorty.R
import uz.instat.rickandmorty.common.id
import uz.instat.rickandmorty.data.model.character.CharacterModel
import uz.instat.rickandmorty.databinding.FragmentCharacterInfoBinding
import uz.instat.rickandmorty.ui.character.info.about.AboutFragment
import uz.instat.rickandmorty.ui.character.info.episodes.EpisodesInCharacterFragment

const val KEY_CHARACTER_INFO_MODEL = "key_character_info_id"

class CharacterInfoFragment : Fragment() {

    private var _binding: FragmentCharacterInfoBinding? = null
    private val binding get() = _binding!!

    private var model: CharacterModel? = null
    private val idList: ArrayList<Long> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true

        model = requireArguments().getParcelable(KEY_CHARACTER_INFO_MODEL)
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
        setupViews()
    }

    private fun setupViews() {
        if (model != null) {
            model!!.episode.forEach { idList.add(it.id) }

            binding.tvName.text = model!!.name
            binding.tvSpecies.text = model!!.species

            Glide.with(requireContext())
                .load(model!!.image)
                .circleCrop()
                .placeholder(R.drawable.ic_morty)
                .into(binding.ivCharacterInfo)

            setupViewPager()
        }

    }

    private fun setupViewPager() {
        val ids = idList.toLongArray()

        val adapter = ViewPagerAdapter(childFragmentManager)
        adapter.addFragment(
            AboutFragment.newInstance(
                model!!.id,
                model!!.created,
                model!!.gender,
                model!!.status
            ), "About"
        )
        adapter.addFragment(EpisodesInCharacterFragment.newInstance(ids), "Episodes")
        binding.viewPager.adapter = adapter
        binding.tabs.setupWithViewPager(binding.viewPager)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}