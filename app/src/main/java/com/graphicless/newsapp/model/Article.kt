package com.graphicless.newsapp.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Entity(tableName = "article_table")
@Parcelize
data class Article(
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String?,
    var source: @RawValue Source?,
    val title: String?,
    @PrimaryKey
    val url: String,
    var urlToImage: String?
):Parcelable