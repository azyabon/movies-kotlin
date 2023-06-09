package com.example.movies_kotlin.data.api

import com.example.movies_kotlin.data.vo.MovieDetails
import com.example.movies_kotlin.data.vo.MovieResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TheMovieDBInterface {

    // https://api.themoviedb.org/3/movie/popular?api_key=36d3b9492c7c489a5890ffdecffba2e5&page=1
    // https://api.themoviedb.org/3/movie/299534?api_key=36d3b9492c7c489a5890ffdecffba2e5

    @GET("movie/popular")
    fun getPopularMovie(@Query("page") page: Int): Single<MovieResponse>

    @GET("movie/{movie_id}")
    fun getMovieDetails(@Path("movie_id") id: Int): Single<MovieDetails>

}