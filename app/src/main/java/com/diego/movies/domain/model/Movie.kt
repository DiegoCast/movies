package com.diego.movies.domain.model

import android.os.Parcel
import android.os.Parcelable

data class Movie(val id: Int, val title: String, val imageUrl: String?, val voteAverage: Float, val voteCount: Long, val description: String, val backgroundUrl: String?) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readFloat(),
            parcel.readLong(),
            parcel.readString(),
            parcel.readString()) {
    }
    
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeString(imageUrl)
        parcel.writeFloat(voteAverage)
        parcel.writeLong(voteCount)
        parcel.writeString(description)
        parcel.writeString(backgroundUrl)
    }
    
    override fun describeContents(): Int {
        return 0
    }
    
    companion object CREATOR : Parcelable.Creator<Movie> {
        override fun createFromParcel(parcel: Parcel): Movie {
            return Movie(parcel)
        }
        
        override fun newArray(size: Int): Array<Movie?> {
            return arrayOfNulls(size)
        }
    }
}
