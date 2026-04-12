package dam.A50829.dogbreedxplorer

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar
import dam.A50829.dogbreedxplorer.adapter.DogImageAdapter
import dam.A50829.dogbreedxplorer.adapter.FavouritesAdapter
import dam.A50829.dogbreedxplorer.viewmodel.DogViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: DogViewModel
    private lateinit var dogImageAdapter: DogImageAdapter
    private lateinit var favouritesAdapter: FavouritesAdapter

    private lateinit var favouritesRecyclerView: RecyclerView
    private lateinit var favouritesLabel: TextView
    private lateinit var dogImageRecyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var progressBar: ProgressBar
    private lateinit var errorTextView: TextView

    private val detailsLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val url = result.data?.getStringExtra("url")
            val isFavourite = result.data?.getBooleanExtra("isFavourite", false) ?: false
            
            if (url != null) {
                val currentImages = viewModel.images.value ?: return@registerForActivityResult
                val targetImage = currentImages.find { it.url == url }
                if (targetImage != null && targetImage.isFavourite != isFavourite) {
                    viewModel.toggleFavourite(targetImage)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Find Views
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        favouritesRecyclerView = findViewById(R.id.favouritesRecyclerView)
        favouritesLabel = findViewById(R.id.favouritesLabel)
        dogImageRecyclerView = findViewById(R.id.dogImageRecyclerView)
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        progressBar = findViewById(R.id.progressBar)
        errorTextView = findViewById(R.id.errorTextView)

        // Initialise ViewModel
        viewModel = ViewModelProvider(this)[DogViewModel::class.java]

        // Setup Adapters
        dogImageAdapter = DogImageAdapter(
            onImageClick = { image ->
                val intent = Intent(this, ImageDetailsActivity::class.java).apply {
                    putExtra("url", image.url)
                    putExtra("breed", image.breed)
                    putExtra("subBreed", image.subBreed)
                    putExtra("isFavourite", image.isFavourite)
                }
                detailsLauncher.launch(intent)
            },
            onFavouriteClick = { image ->
                viewModel.toggleFavourite(image)
            }
        )

        favouritesAdapter = FavouritesAdapter(
            onFavouriteClick = { image ->
                val intent = Intent(this, ImageDetailsActivity::class.java).apply {
                    putExtra("url", image.url)
                    putExtra("breed", image.breed)
                    putExtra("subBreed", image.subBreed)
                    putExtra("isFavourite", image.isFavourite)
                }
                detailsLauncher.launch(intent)
            }
        )

        dogImageRecyclerView.adapter = dogImageAdapter
        favouritesRecyclerView.adapter = favouritesAdapter

        // Setup Observers
        viewModel.images.observe(this) { imagesList ->
            dogImageAdapter.submitList(imagesList)
        }

        viewModel.favourites.observe(this) { favList ->
            favouritesAdapter.submitList(favList)
            if (favList.isEmpty()) {
                favouritesRecyclerView.visibility = View.GONE
                favouritesLabel.visibility = View.GONE
            } else {
                favouritesRecyclerView.visibility = View.VISIBLE
                favouritesLabel.visibility = View.VISIBLE
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                if (!swipeRefreshLayout.isRefreshing) {
                    progressBar.visibility = View.VISIBLE
                }
            } else {
                progressBar.visibility = View.GONE
                swipeRefreshLayout.isRefreshing = false
            }
        }

        viewModel.errorMessage.observe(this) { message ->
            if (message != null) {
                if (message.contains("offline", ignoreCase = true) || message.contains("No internet", ignoreCase = true)) {
                    errorTextView.visibility = View.VISIBLE
                    errorTextView.text = message
                } else {
                    Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show()
                }
            } else {
                errorTextView.visibility = View.GONE
            }
        }

        // Setup Listeners
        swipeRefreshLayout.setOnRefreshListener {
            viewModel.loadImages(isNetworkAvailable())
        }

        // On launch: call viewModel.loadImages()
        if (savedInstanceState == null) {
            viewModel.loadImages(isNetworkAvailable())
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}
