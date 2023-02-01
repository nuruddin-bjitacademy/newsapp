package com.graphicless.newsapp.adapter

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.graphicless.newsapp.R
import com.graphicless.newsapp.model.FavoriteArticle
import com.graphicless.newsapp.model.LocalNews
import com.graphicless.newsapp.utils.AppConstants
import com.graphicless.newsapp.utils.MyApplication
import com.graphicless.newsapp.utils.SharedPreference
import com.graphicless.newsapp.utils.Utils
import com.graphicless.newsapp.viewmodel.NewsViewModel

class FavoriteNewsAdapter(private val favoriteArticles: List<FavoriteArticle>) :
    RecyclerView.Adapter<FavoriteNewsAdapter.DataViewHolder>() {

    private lateinit var layout: View

    class DataViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val viewModel: NewsViewModel = NewsViewModel(application = Application())

        fun bind(localNews: LocalNews) {
            val article = localNews.article
            val publishedAt = article.publishedAt
            val source = article.source
            val title = article.title
            val urlToImage = article.urlToImage
            val author = article.author

            val wholeNews: FrameLayout = view.findViewById(R.id.whole_news)
            val newsImage: ImageView = view.findViewById(R.id.img)
            val titleTV: TextView = view.findViewById(R.id.title)
            val sourceTV: TextView = view.findViewById(R.id.source)
            val timeTV: TextView = view.findViewById(R.id.time)
            val favorite: ImageView = view.findViewById(R.id.favorite)
            val authorTV: TextView = view.findViewById(R.id.author)

            val timePrefix = "\u2022"
            timeTV.text = timePrefix.plus(Utils().DateToTimeFormat(publishedAt))
            titleTV.text = title
            authorTV.text = author
            if (source != null) {
                sourceTV.text = source.name
            }

            wholeNews.setOnClickListener {
                val bundle = Bundle()
                val articleWithoutSource = article.copy(source = null)
                bundle.putParcelable(AppConstants.ARTICLE, articleWithoutSource)
                bundle.putBoolean(AppConstants.FAVORITE, localNews.favorite)
                bundle.putString(AppConstants.CATEGORY, localNews.category)
                bundle.putString(AppConstants.SOURCE, article.source?.name)
                bundle.putString(AppConstants.FROM, "favorite")
                view.findNavController()
                    .navigate(R.id.action_favoriteFragment_to_detailsFragment, bundle)
            }

            favorite.setImageResource(R.drawable.icon_favorite_filled)

            favorite.setOnClickListener {
                viewModel.removeFavoriteNews(
                    FavoriteArticle(
                        LocalNews(
                            article,
                            "favorite",
                            true
                        )
                    )
                )
                viewModel.updateNews(LocalNews(article, localNews.category, false))
                Utils().bookmarkRemoved()
            }

            if (urlToImage != "") {
                Glide
                    .with(view.context)
                    .load(urlToImage)
                    .centerCrop()
                    .into(newsImage)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val sharedPreference = SharedPreference(MyApplication.instance)

        if (sharedPreference.getValueString("layout") != null) {
            val layoutOption = sharedPreference.getValueString("layout")
            layout = if (layoutOption == "compact") {
                LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.news_item_focused, parent, false)
            } else {
                LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.news_item, parent, false)
            }

        } else {
            layout = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.news_item_focused, parent, false)
        }

        return DataViewHolder(layout)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val businessArticle = favoriteArticles[position].localNews
        holder.bind(businessArticle)
    }

    override fun getItemCount(): Int {
        return favoriteArticles.size
    }
}