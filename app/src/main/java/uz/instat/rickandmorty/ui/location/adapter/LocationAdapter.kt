package uz.instat.rickandmorty.ui.location.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import uz.instat.rickandmorty.data.model.location.LocationModel
import uz.instat.rickandmorty.databinding.ItemLocationBinding

class LocationAdapter(private val locationClickListener: LocationClickListener) :
    PagingDataAdapter<LocationModel, LocationAdapter.LocationVH>(LocationDiffer) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationVH {
        return LocationVH(
            ItemLocationBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ), locationClickListener
        )
    }

    override fun onBindViewHolder(holder: LocationVH, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    class LocationVH(
        private val binding: ItemLocationBinding,
        private val locationClickListener: LocationClickListener
    ) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        private lateinit var model: LocationModel

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(item: LocationModel) = with(binding) {
            model = item
            binding.tvLocationName.text = item.name
            binding.tvLocationDeminsion.text = item.dimension
            binding.tvLocationType.text = item.type
        }

        override fun onClick(v: View?) {
            locationClickListener.onLocationClicked(
                binding,
                model.id
            )
        }
    }

    object LocationDiffer : DiffUtil.ItemCallback<LocationModel>() {
        override fun areItemsTheSame(oldItem: LocationModel, newItem: LocationModel) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: LocationModel, newItem: LocationModel) =
            oldItem == newItem
    }

    interface LocationClickListener {
        fun onLocationClicked(binding: ItemLocationBinding, id: Long)
    }

}