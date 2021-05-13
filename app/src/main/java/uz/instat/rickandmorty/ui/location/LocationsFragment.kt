package uz.instat.rickandmorty.ui.location

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import uz.instat.rickandmorty.R
import uz.instat.rickandmorty.common.lazyFast
import uz.instat.rickandmorty.databinding.FragmentLocationsBinding
import uz.instat.rickandmorty.databinding.ItemLocationBinding
import uz.instat.rickandmorty.ui.character.adapter.LoaderStateAdapter
import uz.instat.rickandmorty.ui.location.adapter.LocationAdapter

@ExperimentalPagingApi
class LocationsFragment : Fragment(), LocationAdapter.LocationClickListener,
    SwipeRefreshLayout.OnRefreshListener {

    private val mViewModel: LocationsViewModel by lazyFast {
        defaultViewModelProviderFactory.create(
            LocationsViewModel::class.java
        )
    }
    private var _binding: FragmentLocationsBinding? = null
    private val adapter: LocationAdapter by lazyFast { LocationAdapter(this) }
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
        _binding = FragmentLocationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
        fetchLocations()
    }

    private fun fetchLocations() {
        lifecycleScope.launch {
            try {
                mViewModel.fetchLocations().distinctUntilChanged().collectLatest {
                    adapter.submitData(it)
                }
            } catch (e: Exception) {
                Log.d("TAG_Location", "fetchCharacters: ${e.message}")
            }
        }
    }

    private fun setUpViews() {
        binding.locationsRv.layoutManager =
            LinearLayoutManager(requireContext())
        binding.locationsRv.adapter =
            adapter.withLoadStateFooter(loaderStateAdapter)
        binding.locationsSrl.setColorSchemeResources(R.color.pink, R.color.indigo, R.color.lime)
        binding.locationsSrl.setOnRefreshListener(this)

        with(adapter) {
            lifecycleScope.launch {
                loadStateFlow.collectLatest {
                    binding.locationsSrl.isRefreshing = it.refresh is LoadState.Loading
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onLocationClicked(binding: ItemLocationBinding, id: Long) {
//        TODO("Not yet implemented")
    }

    override fun onRefresh() {
        adapter.refresh()
    }
}