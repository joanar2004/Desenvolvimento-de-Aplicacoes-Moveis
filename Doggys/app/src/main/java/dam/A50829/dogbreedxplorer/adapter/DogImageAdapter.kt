package dam.A50829.dogbreedxplorer.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dam.A50829.dogbreedxplorer.R
import dam.A50829.dogbreedxplorer.model.DogImage

class DogImageAdapter(
    private val onImageClick: (DogImage) -> Unit,
    private val onFavouriteClick: (DogImage) -> Unit
) : RecyclerView.Adapter<DogImageAdapter.DogViewHolder>() {

    private var images: List<DogImage> = emptyList()

    fun submitList(newImages: List<DogImage>) {
        images = newImages
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_dog_image, parent, false)
        return DogViewHolder(view)
    }

    override fun onBindViewHolder(holder: DogViewHolder, position: Int) {
        holder.bind(images[position])
    }

    override fun getItemCount(): Int = images.size

    inner class DogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.dogImageView)
        private val favouriteButton: ImageButton = itemView.findViewById(R.id.favouriteButton)
        private val breedText: TextView = itemView.findViewById(R.id.breedOverlayText)

        fun bind(dogImage: DogImage) {
            breedText.text = dogImage.breed.replaceFirstChar { it.uppercase() }
            
            Glide.with(itemView.context)
                .load(dogImage.url)
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_error)
                .centerCrop()
                .into(imageView)

            if (dogImage.isFavourite) {
                favouriteButton.setImageResource(R.drawable.ic_heart_filled)
            } else {
                favouriteButton.setImageResource(R.drawable.ic_heart_outline)
            }

            imageView.setOnClickListener { onImageClick(dogImage) }
            favouriteButton.setOnClickListener { onFavouriteClick(dogImage) }
        }
    }
}
