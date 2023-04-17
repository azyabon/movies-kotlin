package com.example.movies_kotlin.ui.popular_movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.movies_kotlin.data.api.POST_PER_PAGE
import com.example.movies_kotlin.data.api.TheMovieDBInterface
import com.example.movies_kotlin.data.repository.MovieDataSource
import com.example.movies_kotlin.data.repository.MovieDataSourceFactory
import com.example.movies_kotlin.data.repository.NetworkState
import com.example.movies_kotlin.data.vo.Movie
import io.reactivex.disposables.CompositeDisposable

class MoviePageListRepository (private val apiService: TheMovieDBInterface) {

    lateinit var moviePagedList : LiveData<PagedList<Movie>>
    lateinit var moviesDataSourceFactory: MovieDataSourceFactory

    fun fetchLiveMoviePageList (compositeDisposable: CompositeDisposable) : LiveData<PagedList<Movie>> {
        moviesDataSourceFactory = MovieDataSourceFactory(apiService, compositeDisposable)

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(POST_PER_PAGE)
            .build()

        moviePagedList = LivePagedListBuilder(moviesDataSourceFactory, config).build()

        return moviePagedList;
    }

    fun getNetworkState(): LiveData<NetworkState> {
        return Transformations.switchMap<MovieDataSource, NetworkState>(
            moviesDataSourceFactory.movieLiveDataSource, MovieDataSource::networkState)
    }
}