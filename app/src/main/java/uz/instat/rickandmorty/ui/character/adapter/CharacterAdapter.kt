package uz.instat.rickandmorty.ui.character.adapter

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import uz.instat.rickandmorty.R
import uz.instat.rickandmorty.data.model.character.CharacterModel
import uz.instat.rickandmorty.databinding.ItemCharacterBinding


class CharacterAdapter(private val characterClickListener: CharacterClickListener) :
    PagingDataAdapter<CharacterModel, CharacterAdapter.CharacterVH>(CharacterDiffer) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterVH {
        return CharacterVH(
            ItemCharacterBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ), characterClickListener
        )
    }

    override fun onBindViewHolder(holder: CharacterVH, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    class CharacterVH(
        private val binding: ItemCharacterBinding,
        private val characterClickListener: CharacterClickListener
    ) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        private lateinit var glide: RequestManager
        private lateinit var model: CharacterModel

        init {
            glide = Glide.with(binding.root)
            itemView.setOnClickListener(this)
        }

        fun bind(item: CharacterModel) = with(binding) {
            model = item
            glide
                .load(item.image)
                .circleCrop()
                .placeholder(R.drawable.ic_morty)
                .into(binding.ivCharacterImage)

            binding.tvCharacterName.text = item.name
            binding.tvCharacterSpecies.text = item.species
            binding.tvCharacterGender.text = item.gender
            binding.tvCharacterStatus.text = item.status

            @ColorRes val color: Int =
                when (item.status) {
                    "Alive" -> R.color.green_a700
                    "Dead" -> R.color.red_a700
                    "unknown" -> R.color.gray_700
                    else -> R.color.gray_300
                }
            binding.root.setCardBackgroundColor(
                ContextCompat.getColor(binding.root.context, color)
            )
            binding.cvCharacterImage.setCardBackgroundColor(
                ContextCompat.getColor(binding.root.context, color)
            )
            setTextViewDrawableColor(binding.tvCharacterStatus, color)

        }

        private fun setTextViewDrawableColor(textView: TextView, @ColorRes color: Int) {
            for (drawable in textView.compoundDrawables) {
                if (drawable != null) {
                    drawable.colorFilter =
                        PorterDuffColorFilter(
                            ContextCompat.getColor(textView.context, color),
                            PorterDuff.Mode.SRC_IN
                        )
                }
            }
        }

        override fun onClick(v: View?) {
            characterClickListener.onCharacterClicked(
                binding,
                model
            )
        }
    }

    object CharacterDiffer : DiffUtil.ItemCallback<CharacterModel>() {
        override fun areItemsTheSame(oldItem: CharacterModel, newItem: CharacterModel) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: CharacterModel, newItem: CharacterModel) =
            oldItem == newItem
    }

    interface CharacterClickListener {
        fun onCharacterClicked(binding: ItemCharacterBinding, model: CharacterModel)
    }

}