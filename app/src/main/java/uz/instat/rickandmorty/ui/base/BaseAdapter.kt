package uz.instat.rickandmorty.ui.base

import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<T : Any, VH : RecyclerView.ViewHolder>(BaseDiffer: DiffUtil.ItemCallback<T>) :
    PagingDataAdapter<T, VH>(BaseDiffer){

    }