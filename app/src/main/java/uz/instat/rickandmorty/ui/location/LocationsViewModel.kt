package uz.instat.rickandmorty.ui.location

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import uz.instat.rickandmorty.data.model.location.LocationModel
import uz.instat.rickandmorty.repo.location.LocationsRepository

@ExperimentalPagingApi
class LocationsViewModel(
    private val repo: LocationsRepository = LocationsRepository.getInstance()
) : ViewModel() {


    private lateinit var _locationsFlow: Flow<PagingData<LocationModel>>
    val locationsFlow: Flow<PagingData<LocationModel>>
        get() = _locationsFlow

    init {
        fetchLocations()
    }

    private fun fetchLocations() {
        _locationsFlow = try {
            repo.getLocationsFlowDb().cachedIn(viewModelScope)
        } catch (e: Exception) {
            flowOf(PagingData.empty())
        }
    }
}