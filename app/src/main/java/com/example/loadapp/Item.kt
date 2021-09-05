package com.example.loadapp

import androidx.annotation.StringRes

enum class Item(val url: String, @StringRes val full_name_id: Int) {
    GLIDE(
        "https://github.com/bumptech/glide/archive/master.zip",
        R.string.glide
    ),
    UDACITY(
        "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip",
        R.string.udacity
    ),
    RETROFIT(
        "https://github.com/square/retrofit/archive/master.zip",
        R.string.retrofit
    ),
    NULL("", R.string.null_file)
}