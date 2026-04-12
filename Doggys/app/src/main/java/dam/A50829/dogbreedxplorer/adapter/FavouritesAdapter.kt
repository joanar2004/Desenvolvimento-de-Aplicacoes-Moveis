package dam.A50829.dogbreedxplorer.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dam.A50829.dogbreedxplorer.R
import dam.A50829.dogbreedxplorer.model.DogImage

class FavouritesAdapter(
    private val onFavouriteClick: (DogImage) -> Unit
) : RecyclerView.Adapter<FavouritesAdapter.FavouriteViewHolder>() {

    private var favourites: List<DogImage> = emptyList()

    fun submitList(newFavourites: List<DogImage>) {
        favourites = newFavourites
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_favourite, parent, false)
        return FavouriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        holder.bind(favourites[position])
    }

    override fun getItemCount(): Int = favourites.size

    inner class FavouriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val thumbnailView: ImageView = itemView.findViewById(R.id.thumbnailImageView)

        fun bind(dogImage: DogImage) {
            Glide.with(itemView.context)
                .load(dogImage.url)
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_error)
                .centerCrop()
                .into(thumbnailView)

            itemView.setOnClickListener { onFavouriteClick(dogImage) }
        }
    }
}
