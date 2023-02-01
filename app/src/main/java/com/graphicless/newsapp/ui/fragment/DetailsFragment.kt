package com.graphicless.newsapp.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.core.graphics.ColorUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.graphicless.newsapp.R
import com.graphicless.newsapp.databinding.FragmentDetailsBinding
import com.graphicless.newsapp.model.Article
import com.graphicless.newsapp.model.FavoriteArticle
import com.graphicless.newsapp.model.LocalNews
import com.graphicless.newsapp.model.Source
import com.graphicless.newsapp.utils.CheckNetwork
import com.graphicless.newsapp.utils.AppConstants
import com.graphicless.newsapp.utils.Utils
import com.graphicless.newsapp.viewmodel.NewsViewModel
import kotlinx.android.synthetic.main.activity_main.*
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.github.chrisbanes.photoview.PhotoView

class DetailsFragment : Fragment() {

    private lateinit var _binding: FragmentDetailsBinding
    private val binding get() = _binding

    private val args: DetailsFragmentArgs by navArgs()
    private val viewModel: NewsViewModel by viewModels()

    var newsImageDrawable: Drawable? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        requireActivity().bottom_nav_menu.visibility = View.GONE

        val article: Article = args.article

        article.source = Source("id", args.source)
        var favorite = args.favorite

        binding.author.text = article.author

        val backgroundColor = binding.time.backgroundTintList?.defaultColor ?: Color.WHITE
        val whiteContrast = ColorUtils.calculateContrast(backgroundColor, Color.WHITE)
        val blackContrast = ColorUtils.calculateContrast(backgroundColor, Color.BLACK)
        val textColor = if (whiteContrast > blackContrast) Color.WHITE else Color.BLACK
        binding.time.setTextColor(textColor)

        Glide.with(view.context).load(article.urlToImage).into(binding.newsImage)

        Glide.with(view.context)
            .load(article.urlToImage)
            .into(object : CustomTarget<Drawable>() {
                override fun onLoadCleared(placeholder: Drawable?) {}
                override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                    // Use the drawable here
                    newsImageDrawable = resource
                }
            })

        if(newsImageDrawable == null)
            newsImageDrawable = resources.getDrawable(R.drawable.image_unavailable)

        binding.newsImage.setImageDrawable(newsImageDrawable)
        binding.tvTitle.text = article.title
        binding.tvDescription.text = article.description
        binding.tvContent.text = article.content

        binding.newsImage.setOnClickListener {
            val photoView = PhotoView(view.context)
            photoView.setImageDrawable(newsImageDrawable)
            val builder = AlertDialog.Builder(view.context)
            builder.setView(photoView)
            builder.show()
        }

        binding.btnReadFullArticle.setOnClickListener {
            if(CheckNetwork().isConnected){
                val bundle = Bundle()
                bundle.putParcelable(AppConstants.ARTICLE, args.article)
                bundle.putBoolean(AppConstants.FAVORITE, args.favorite)
                bundle.putString(AppConstants.CATEGORY, args.category)
                bundle.putString(AppConstants.SOURCE, args.source)
                bundle.putString(AppConstants.FROM, args.cameFrom)
                view.findNavController()
                    .navigate(R.id.action_detailsFragment_to_webViewFragment, bundle)
            }else Utils().internetUnavailableToast()
        }

        // Sets the favorite icon
        if (!args.favorite) {
            binding.favorite.setImageResource(R.drawable.ic_twotone_favorite_24)
        } else {
            binding.favorite.setImageResource(R.drawable.icon_favorite_filled)
        }

        // Favorite icon click listener
        binding.favorite.setOnClickListener {
            if (!favorite) {
                viewModel.insertFavoriteNews(FavoriteArticle(LocalNews(article,"favorite",true)))
                viewModel.updateNews(LocalNews(article, args.category, true))
                binding.favorite.setImageResource(R.drawable.icon_favorite_filled)
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
                viewModel.updateNews(LocalNews(article, args.category, false))
                binding.favorite.setImageResource(R.drawable.ic_twotone_favorite_24)
                Utils().bookmarkRemoved()
            }
            favorite = !favorite
        }
        // Favorite icon click listener END

        binding.btnBack.setOnClickListener{
            when(args.cameFrom){
                "home" -> {
                    view.findNavController()
                        .navigate(R.id.action_detailsFragment_to_categoriesFragment)
                }
                "favorite" -> {
                    view.findNavController().navigate(R.id.action_detailsFragment_to_favoriteFragment)
                }
            }
        }

    }

    override fun onResume() {
        super.onResume()
        val window: Window = requireActivity().window
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
    }

    override fun onPause() {
        super.onPause()
        val window: Window = requireActivity().window
        window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }

}