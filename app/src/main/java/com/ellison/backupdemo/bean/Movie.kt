package com.ellison.backupdemo.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
class Movie(
    @ColumnInfo(name = "movie_name", defaultValue = "Harry Potter")
    var Title: String,
    @ColumnInfo(name = "movie_year", defaultValue = "1991")
    var Year: String,
    @ColumnInfo(name = "movie_id", defaultValue = "imdb324523")
    var imdbID: String,
    @ColumnInfo(name = "movie_type", defaultValue = "Movie")
    var Type: String,
    @ColumnInfo(name = "movie_poster", defaultValue = "https://ddd/dad.img")
    var Poster: String
) : Serializable {
    @PrimaryKey(autoGenerate = true)
    var id = 1

    private var serialVersionUID = 7897981L

    fun getSerialVersionUID(): Long {
        return serialVersionUID
    }

    fun setSerialVersionUID(serialVersionUID: Long) {
        this.serialVersionUID = serialVersionUID
    }

    override fun toString(): String {
        return "Movie(Title='$Title', Year='$Year', imdbID='$imdbID', Type='$Type', Poster='$Poster')"
    }
}

val fakeMovie = Movie("Harry Potter",
    "1991",
    "imdb324523",
    "Movie",
    "https://ddd/dad.img")