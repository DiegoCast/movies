package com.diego.movies.data.repository

import javax.inject.Inject

class ConfigurationRepository @Inject constructor() {
    
    var imageBaseUrl = ""
    var imageSize = ""
    
    fun updateConfiguration(imageBaseUrl: String, imageSizeList: List<String>) {
        this.imageBaseUrl = imageBaseUrl
        when {
            imageSizeList.contains("w500") -> this.imageSize = "w500"
            imageSizeList.contains("w342") -> this.imageSize = "w342"
            imageSizeList.contains("w185") -> this.imageSize = "w185"
            imageSizeList.contains("w154") -> this.imageSize = "w154"
            else -> this.imageSize = imageSizeList[0]
        }
    }
}