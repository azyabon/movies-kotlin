package com.example.movies_kotlin.ui.single_movie_details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.movies_kotlin.R
import com.example.movies_kotlin.data.api.POSTER_BASE_URL
import com.example.movies_kotlin.data.api.TheMovieDBClient
import com.example.movies_kotlin.data.api.TheMovieDBInterface
import com.example.movies_kotlin.data.repository.NetworkState
import com.example.movies_kotlin.data.vo.MovieDetails
import java.text.NumberFormat
import java.util.*

class SingleMovie : AppCompatActivity() {

    private lateinit var viewModel: SingleMovieViewModel
    private lateinit var movieRepository: MovieDetailsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_movie)

        val progressBar = findViewById<ProgressBar>(R.id.progress_bar)
        val errorHint = findViewById<TextView>(R.id.txt_error)

        val movieId: Int = intent.getIntExtra("id", 1)

        val apiService : TheMovieDBInterface = TheMovieDBClient.getClient()
        movieRepository = MovieDetailsRepository(apiService)

        viewModel = getViewModel(movieId);

        viewModel.movieDetails.observe(this, Observer {
            bindUI(it)
        })

        viewModel.networkState.observe(this, Observer {
            progressBar.visibility = if (it == NetworkState.LOADING) View.VISIBLE else View.GONE
            errorHint.visibility = if (it == NetworkState.ERROR) View.VISIBLE else View.GONE
        })
    }

    fun bindUI(it: MovieDetails) {
        val movieTitle = findViewById<TextView>(R.id.movie_title)
        val movieTagline = findViewById<TextView>(R.id.movie_tagline)
        val movieReleaseDate = findViewById<TextView>(R.id.movie_release_date)
        val movieRating = findViewById<TextView>(R.id.movie_rating)
        val movieRuntime = findViewById<TextView>(R.id.movie_runtime)
        val movieOverview = findViewById<TextView>(R.id.movie_overview)
        val movieBudget = findViewById<TextView>(R.id.movie_budget)
        val movieRevenue = findViewById<TextView>(R.id.movie_revenue)
        val moviePreview = findViewById<ImageView>(R.id.iv_movie_poster)

        movieTitle.text = it.title
        movieTagline.text = it.tagline
        movieReleaseDate.text = it.releaseDate
        movieRating.text = it.rating.toString()
        movieRuntime.text = it.runtime.toString() + " minutes"
        movieOverview.text = it.overview

        val formatCurrency = NumberFormat.getCurrencyInstance(Locale.US)
        movieBudget.text = formatCurrency.format(it.budget)
        movieRevenue.text = formatCurrency.format(it.revenue)

        val moviePosterURL = POSTER_BASE_URL + it.posterPath
        Glide.with(this)
            .load(moviePosterURL)
            .into(moviePreview)
    }

    private fun getViewModel(movieId: Int): SingleMovieViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return SingleMovieViewModel(movieRepository, movieId) as T
            }
        })[SingleMovieViewModel::class.java]
    }
}