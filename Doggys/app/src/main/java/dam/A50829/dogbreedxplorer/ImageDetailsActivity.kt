package dam.A50829.dogbreedxplorer

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dam.A50829.dogbreedxplorer.model.DogImage
import dam.A50829.dogbreedxplorer.viewmodel.DogViewModel

class ImageDetailsActivity : AppCompatActivity() {

    private lateinit var viewModel: DogViewModel
    private lateinit var image: DogImage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_details)

        // Find views
        val toolbar: Toolbar = findViewById(R.id.toolbarDetails)
        val detailImageView: ImageView = findViewById(R.id.detailImageView)
        val breedTextView: TextView = findViewById(R.id.breedTextView)
        val subBreedTextView: TextView = findViewById(R.id.subBreedTextView)
        val subBreedContainer: LinearLayout = findViewById(R.id.subBreedContainer)
        val urlTextView: TextView = findViewById(R.id.urlTextView)
        val fabFavourite: FloatingActionButton = findViewById(R.id.fabFavourite)

        // Setup Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // Initialise ViewModel
        viewModel = ViewModelProvider(this)[DogViewModel::class.java]

        // Reconstruct DogImage object from Intent
        val url = intent.getStringExtra("url") ?: return
        val breed = intent.getStringExtra("breed") ?: "Unknown"
        val subBreed = intent.getStringExtra("subBreed")
        val isFavourite = intent.getBooleanExtra("isFavourite", false)

        image = DogImage(
            id = url,
            url = url,
            breed = breed,
            subBreed = subBreed,
            isFavourite = isFavourite
        )

        // Configure Views
        Glide.with(this)
            .load(image.url)
            .placeholder(R.drawable.ic_placeholder)
            .error(R.drawable.ic_error)
            .centerCrop()
            .into(detailImageView)

        breedTextView.text = image.breed.replaceFirstChar { it.uppercase() }

        if (image.subBreed.isNullOrEmpty()) {
            subBreedContainer.visibility = View.GONE
        } else {
            subBreedContainer.visibility = View.VISIBLE
            subBreedTextView.text = image.subBreed!!.replaceFirstChar { it.uppercase() }
        }

        urlTextView.text = image.url

        updateFab(fabFavourite)

        // Click Listener
        fabFavourite.setOnClickListener {
            viewModel.toggleFavourite(image)
            updateFab(fabFavourite)

            val resultIntent = Intent().apply {
                putExtra("url", image.url)
                putExtra("isFavourite", image.isFavourite)
            }
            setResult(RESULT_OK, resultIntent)
        }
    }

    private fun updateFab(fab: FloatingActionButton) {
        if (image.isFavourite) {
            fab.setImageResource(R.drawable.ic_heart_filled)
        } else {
            fab.setImageResource(R.drawable.ic_heart_outline)
        }
    }
}
