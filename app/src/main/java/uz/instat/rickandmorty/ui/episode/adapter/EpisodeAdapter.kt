package uz.instat.rickandmorty.ui.episode.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import uz.instat.rickandmorty.data.model.episode.EpisodeModel
import uz.instat.rickandmorty.databinding.ItemEpisodeBinding

class EpisodeAdapter(private val episodeClickListener: EpisodeClickListener) :
    PagingDataAdapter<EpisodeModel, EpisodeAdapter.EpisodeVH>(EpisodeDiffer) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeVH {
        return EpisodeVH(
            ItemEpisodeBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ), episodeClickListener
        )
    }

    override fun onBindViewHolder(holder: EpisodeVH, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    class EpisodeVH(
        private val binding: ItemEpisodeBinding,
        private val episodeClickListener: EpisodeClickListener
    ) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        private lateinit var model: EpisodeModel

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(item: EpisodeModel) = with(binding) {
            model = item
            binding.tvEpisodeName.text = item.name
            binding.tvEpisode.text = item.episode
            binding.tvEpisodeAirDate.text = item.airDate
        }

        override fun onClick(v: View?) {
            episodeClickListener.onEpisodeClicked(
                binding,
                model.id
            )
        }
    }

    object EpisodeDiffer : DiffUtil.ItemCallback<EpisodeModel>() {
        override fun areItemsTheSame(oldItem: EpisodeModel, newItem: EpisodeModel) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: EpisodeModel, newItem: EpisodeModel) =
            oldItem == newItem
    }

    interface EpisodeClickListener {
        fun onEpisodeClicked(binding: ItemEpisodeBinding, id: Long)
    }

}