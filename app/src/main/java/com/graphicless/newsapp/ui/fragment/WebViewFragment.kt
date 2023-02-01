package com.graphicless.newsapp.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.graphicless.newsapp.R
import com.graphicless.newsapp.databinding.FragmentWebViewBinding
import com.graphicless.newsapp.utils.AppConstants
import kotlinx.android.synthetic.main.activity_main.*

class WebViewFragment : Fragment() {

    private lateinit var _binding: FragmentWebViewBinding
    private val binding get() = _binding

    private val args: WebViewFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWebViewBinding.inflate(inflater, container, false)

        return binding.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().bottom_nav_menu.visibility = View.GONE

        binding.webView.apply {
            loadUrl(args.article.url)
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true
        }

        binding.btnBack.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelable(AppConstants.ARTICLE, args.article)
            bundle.putBoolean(AppConstants.FAVORITE, args.favorite)
            bundle.putString(AppConstants.CATEGORY, args.category)
            bundle.putString(AppConstants.SOURCE, args.source)
            bundle.putString(AppConstants.FROM, args.cameFrom)
            view.findNavController()
                .navigate(R.id.action_webViewFragment_to_detailsFragment, bundle)
        }
    }
}