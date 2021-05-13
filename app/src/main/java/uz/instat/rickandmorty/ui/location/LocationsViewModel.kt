package uz.instat.rickandmorty.ui.location

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
    private val repo:LocationsRepository = LocationsRepository.getInstance()
) : ViewModel() {

    fun fetchLocations(): Flow<PagingData<LocationModel>> {
        return try {
            repo.getLocationsFlowDb().cachedIn(viewModelScope)
        } catch (e: Exception) {
            Log.d("TAG", "fetchLocations: ${e.message}")
            flowOf(PagingData.empty())
        }
    }
}