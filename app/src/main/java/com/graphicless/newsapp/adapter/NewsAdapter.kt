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
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
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


class NewsAdapter(val localNews: List<LocalNews>) :
    ListAdapter<LocalNews, NewsAdapter.DataViewHolder>(DiffCallback) {

    private lateinit var layout: View

    class DataViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        private val viewModel: NewsViewModel = NewsViewModel(application = Application())

        fun bind(localNews: LocalNews) {

            val article = localNews.article
            val category = localNews.category
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
            timeTV.text = timePrefix.plus(" ").plus(Utils().DateToTimeFormat(publishedAt))
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
                bundle.putString(AppConstants.FROM, "home")
                view.findNavController()
                    .navigate(R.id.action_categoriesFragment_to_detailsFragment, bundle)
            }
            if (!localNews.favorite) {
                favorite.setImageResource(R.drawable.ic_twotone_favorite_24)
            } else {
                favorite.setImageResource(R.drawable.icon_favorite_filled)
            }

            favorite.setOnClickListener {
                if (!localNews.favorite) {
                    viewModel.insertFavoriteNews(
                        FavoriteArticle(
                            LocalNews(
                                article,
                                "favorite",
                                true
                            )
                        )
                    )
                    viewModel.updateNews(LocalNews(article, category, true))
                    favorite.setImageResource(R.drawable.icon_favorite_filled)
                    Utils().bookmarkAdded()
                } else {
                    viewModel.removeFavoriteNews(
                        FavoriteArticle(
                            LocalNews(
                                article,
                                "favorite",
                                true
                            )
                        )
                    )
                    viewModel.updateNews(LocalNews(article, category, false))
                    favorite.setImageResource(R.drawable.ic_twotone_favorite_24)
                    Utils().bookmarkRemoved()
                }
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

    companion object DiffCallback : DiffUtil.ItemCallback<LocalNews>() {
        override fun areItemsTheSame(oldItem: LocalNews, newItem: LocalNews): Boolean {
            return oldItem.article.url == newItem.article.url
        }

        override fun areContentsTheSame(oldItem: LocalNews, newItem: LocalNews): Boolean {
            return oldItem.article.url == newItem.article.url
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
        val businessArticles = localNews[position]
        holder.bind(businessArticles)
    }

    override fun getItemCount(): Int {
        return localNews.size
    }
}