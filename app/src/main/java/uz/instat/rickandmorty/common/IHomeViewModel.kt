package uz.instat.rickandmorty.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

interface IHomeViewModel {
    val liveData: LiveData<String>
    fun getAll()
}

class HomeViewModel : ViewModel(), IHomeViewModel {

    override val liveData: LiveData<String> = MutableLiveData()

    override fun getAll() {

    }
}