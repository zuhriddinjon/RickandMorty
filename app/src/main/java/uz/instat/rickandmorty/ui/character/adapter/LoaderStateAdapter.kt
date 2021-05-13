package uz.instat.rickandmorty.ui.character.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import uz.instat.rickandmorty.databinding.ItemLoaderBinding

class LoaderStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<LoaderStateAdapter.LoaderVH>() {

    override fun onBindViewHolder(holder: LoaderVH, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoaderVH {
        return LoaderVH(
            ItemLoaderBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ), retry
        )
    }

    class LoaderVH(private val binding: ItemLoaderBinding, retry: () -> Unit) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.btnRetry.setOnClickListener {
                retry()
            }
        }

        fun bind(loadState: LoadState) {
            if (loadState is LoadState.Loading) {
                binding.mlLoader.transitionToEnd()
            } else {
                binding.mlLoader.transitionToStart()
            }
        }
    }
}