package com.graphicless.newsapp.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.transition.TransitionManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.graphicless.newsapp.R
import com.graphicless.newsapp.adapter.NewsAdapter
import com.graphicless.newsapp.databinding.FragmentCategoryBinding
import com.graphicless.newsapp.utils.CheckNetwork
import com.graphicless.newsapp.utils.AppConstants
import com.graphicless.newsapp.utils.MyApplication
import com.graphicless.newsapp.utils.Utils
import com.graphicless.newsapp.viewmodel.NewsViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_categories.*
import kotlinx.android.synthetic.main.tab_item_layout.view.*


class CategoryFragment : Fragment() {

    private var searchView: SearchView? = null
    private var queryTextListener: SearchView.OnQueryTextListener? = null

    private val viewModel: NewsViewModel by viewModels()

    private lateinit var _binding: FragmentCategoryBinding
    private val binding get() = _binding

    private lateinit var categories: Any
    private lateinit var category: String

    private val handler = Handler()

    private var tagAll: Boolean = true
    private var tagTitle: Boolean = false
    private var tagContent: Boolean = false
    private var tagDescription: Boolean = false

    private var lastRefreshTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val toolbar = parentFragment?.view?.findViewById<Toolbar>(R.id.toolbar_categories)

        val bottomNavigationView = activity?.bottom_nav_menu

        binding.rvNews.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (dy > 0) {
                    // Scrolling up
                    bottomNavigationView?.height?.toFloat()
                        ?.let { bottomNavigationView.animate()
                            ?.translationY(it) }
                    bottomNavigationView?.height?.toFloat()
                        ?.let { binding.btnSearch.animate().translationY(it) }
                } else {
                    binding.btnSearch.animate().translationY(0f)
                    bottomNavigationView?.animate()?.translationY(0f)
                    if (binding.appbarCategory.isVisible) {
                        if (toolbar != null) {
                            toolbar.visibility = View.GONE
                        }
                    } else {
                        if (toolbar != null) {
                            toolbar.visibility = View.VISIBLE
                        }
                    }
                }
            }
        })

        requireActivity().bottom_nav_menu.visibility = View.VISIBLE

        categories = view.context.resources.getStringArray(R.array.news_categories)

        binding.btnSearch.setOnClickListener {
            binding.btnSearch.animate().translationX(500f)
            binding.appbarCategory.visibility = View.VISIBLE
            handler.postDelayed({
                searchOpen()
            }, 100)


        }
        binding.searchView.setOnCloseListener {
            binding.appbarCategory.visibility = View.GONE
            searchClose()
            binding.btnSearch.animate().translationX(0f)
            return@setOnCloseListener true
        }

        searchView = binding.searchView

        queryTextListener = object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(query: String): Boolean {
                Log.d("TAG", "onQueryTextChange: called")
                if (category != "top news") {
                    val searchQuery = "%$query%"
                    searchInCategory(category, searchQuery)
                    Log.i("onQueryTextChange", "query: $query and category: $category")
                } else {
                    val searchQuery = "%$query%"
                    searchInTopNews(searchQuery)
                    Log.i("onQueryTextChange", "query: $query and category: $category")
                }
                return true
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                return true
            }
        }
        searchView!!.setOnQueryTextListener(queryTextListener)


        arguments?.takeIf { it.containsKey(AppConstants.CATEGORY_TAB_NUMBER) }?.apply {
            category = (categories as Array<*>)[getInt(AppConstants.CATEGORY_TAB_NUMBER)] as String
            when (getInt(AppConstants.CATEGORY_TAB_NUMBER)) {
                0 -> {
                    viewModel.topNews.observe(requireActivity()) {
                        // Save state
                        val recyclerViewState = binding.rvNews.layoutManager?.onSaveInstanceState()
                        // Restore state
                        binding.rvNews.layoutManager?.onRestoreInstanceState(recyclerViewState)
                        val adapter = NewsAdapter(it)
                        binding.rvNews.adapter = adapter
                        binding.loading.visibility = View.GONE
                        if(it.isEmpty()){
                            binding.tvNoData.visibility = View.VISIBLE
                        }
                    }
                    binding.swipeToRefresh.setOnRefreshListener {
                        val currentTime = SystemClock.elapsedRealtime()
                        if (currentTime - lastRefreshTime < 5000) {
                            Toast.makeText(MyApplication.instance, "Please wait 5 seconds before refreshing again", Toast.LENGTH_SHORT).show()
                        } else {
                            if (CheckNetwork().isConnected) {
                                val isComplete = viewModel.reInsertTopNews(AppConstants.COUNTRY)
                                if (isComplete)
                                    binding.swipeToRefresh.isRefreshing = false
                            } else {
                                binding.swipeToRefresh.isRefreshing = false
                                Utils().internetUnavailableToast()
                            }
                            lastRefreshTime = currentTime
                        }
                        binding.swipeToRefresh.isRefreshing = false
                    }
                }
                1 -> {
                    viewModel.businessNews.observe(requireActivity()) {
                        val recyclerViewState = binding.rvNews.layoutManager?.onSaveInstanceState()
                        binding.rvNews.layoutManager?.onRestoreInstanceState(recyclerViewState)
                        val adapter = NewsAdapter(it)
                        binding.rvNews.adapter = adapter
                        binding.loading.visibility = View.GONE
                        if(it.isEmpty()){
                            binding.tvNoData.visibility = View.VISIBLE
                        }
                    }

                    binding.swipeToRefresh.setOnRefreshListener {
                        val currentTime = SystemClock.elapsedRealtime()
                        if (currentTime - lastRefreshTime < 5000) {
                            Toast.makeText(MyApplication.instance, "Please wait 5 seconds before refreshing again", Toast.LENGTH_SHORT).show()
                        } else {
                            if (CheckNetwork().isConnected) {
                                val isComplete = viewModel.reInsertNews(AppConstants.BUSINESS)
                                if (isComplete)
                                    binding.swipeToRefresh.isRefreshing = false
                            } else {
                                binding.swipeToRefresh.isRefreshing = false
                                Utils().internetUnavailableToast()
                            }
                            lastRefreshTime = currentTime
                        }
                        binding.swipeToRefresh.isRefreshing = false
                    }
                }
                2 -> {
                    viewModel.entertainmentNews.observe(requireActivity()) {
                        val recyclerViewState = binding.rvNews.layoutManager?.onSaveInstanceState()
                        binding.rvNews.layoutManager?.onRestoreInstanceState(recyclerViewState)
                        val adapter = NewsAdapter(it)
                        binding.rvNews.adapter = adapter
                        binding.loading.visibility = View.GONE
                        if(it.isEmpty()){
                            binding.tvNoData.visibility = View.VISIBLE
                        }
                    }

                    binding.swipeToRefresh.setOnRefreshListener {
                        val currentTime = SystemClock.elapsedRealtime()
                        if (currentTime - lastRefreshTime < 5000) {
                            Toast.makeText(MyApplication.instance, "Please wait 5 seconds before refreshing again", Toast.LENGTH_SHORT).show()
                        } else {
                            if (CheckNetwork().isConnected) {
                                val isComplete = viewModel.reInsertNews(AppConstants.ENTERTAINMENT)
                                if (isComplete)
                                    binding.swipeToRefresh.isRefreshing = false
                            } else {
                                binding.swipeToRefresh.isRefreshing = false
                                Utils().internetUnavailableToast()
                            }
                            lastRefreshTime = currentTime
                        }
                        binding.swipeToRefresh.isRefreshing = false
                    }
                }
                3 -> {
                    viewModel.generalNews.observe(requireActivity()) {
                        val recyclerViewState = binding.rvNews.layoutManager?.onSaveInstanceState()
                        binding.rvNews.layoutManager?.onRestoreInstanceState(recyclerViewState)
                        val adapter = NewsAdapter(it)
                        binding.rvNews.adapter = adapter
                        binding.loading.visibility = View.GONE
                        if(it.isEmpty()){
                            binding.tvNoData.visibility = View.VISIBLE
                        }
                    }

                    binding.swipeToRefresh.setOnRefreshListener {
                        val currentTime = SystemClock.elapsedRealtime()
                        if (currentTime - lastRefreshTime < 5000) {
                            Toast.makeText(MyApplication.instance, "Please wait 5 seconds before refreshing again", Toast.LENGTH_SHORT).show()
                        } else {
                            if (CheckNetwork().isConnected) {
                                val isComplete = viewModel.reInsertNews(AppConstants.GENERAL)
                                if (isComplete)
                                    binding.swipeToRefresh.isRefreshing = false
                            } else {
                                binding.swipeToRefresh.isRefreshing = false
                                Utils().internetUnavailableToast()
                            }
                            lastRefreshTime = currentTime
                        }
                        binding.swipeToRefresh.isRefreshing = false
                    }
                }
                4 -> {
                    viewModel.healthNews.observe(requireActivity()) {
                        val recyclerViewState = binding.rvNews.layoutManager?.onSaveInstanceState()
                        binding.rvNews.layoutManager?.onRestoreInstanceState(recyclerViewState)
                        val adapter = NewsAdapter(it)
                        binding.rvNews.adapter = adapter
                        binding.loading.visibility = View.GONE
                        if(it.isEmpty()){
                            binding.tvNoData.visibility = View.VISIBLE
                        }
                    }

                    binding.swipeToRefresh.setOnRefreshListener {
                        val currentTime = SystemClock.elapsedRealtime()
                        if (currentTime - lastRefreshTime < 5000) {
                            Toast.makeText(MyApplication.instance, "Please wait 5 seconds before refreshing again", Toast.LENGTH_SHORT).show()
                        } else {
                            if (CheckNetwork().isConnected) {
                                val isComplete = viewModel.reInsertNews(AppConstants.HEALTH)
                                if (isComplete)
                                    binding.swipeToRefresh.isRefreshing = false
                            } else {
                                binding.swipeToRefresh.isRefreshing = false
                                Utils().internetUnavailableToast()
                            }
                            lastRefreshTime = currentTime
                        }
                        binding.swipeToRefresh.isRefreshing = false
                    }
                }
                5 -> {
                    viewModel.scienceNews.observe(requireActivity()) {
                        val recyclerViewState = binding.rvNews.layoutManager?.onSaveInstanceState()
                        binding.rvNews.layoutManager?.onRestoreInstanceState(recyclerViewState)
                        val adapter = NewsAdapter(it)
                        binding.rvNews.adapter = adapter
                        binding.loading.visibility = View.GONE
                        if(it.isEmpty()){
                            binding.tvNoData.visibility = View.VISIBLE
                        }
                    }

                    binding.swipeToRefresh.setOnRefreshListener {
                        val currentTime = SystemClock.elapsedRealtime()
                        if (currentTime - lastRefreshTime < 5000) {
                            Toast.makeText(MyApplication.instance, "Please wait 5 seconds before refreshing again", Toast.LENGTH_SHORT).show()
                        } else {
                            if (CheckNetwork().isConnected) {
                                val isComplete = viewModel.reInsertNews(AppConstants.SCIENCE)
                                if (isComplete)
                                    binding.swipeToRefresh.isRefreshing = false
                            } else {
                                binding.swipeToRefresh.isRefreshing = false
                                Utils().internetUnavailableToast()
                            }
                            lastRefreshTime = currentTime
                        }
                        binding.swipeToRefresh.isRefreshing = false
                    }
                }
                6 -> {
                    viewModel.sportsNews.observe(requireActivity()) {
                        val recyclerViewState = binding.rvNews.layoutManager?.onSaveInstanceState()
                        binding.rvNews.layoutManager?.onRestoreInstanceState(recyclerViewState)
                        val adapter = NewsAdapter(it)
                        binding.rvNews.adapter = adapter
                        binding.loading.visibility = View.GONE
                        if(it.isEmpty()){
                            binding.tvNoData.visibility = View.VISIBLE
                        }
                    }

                    binding.swipeToRefresh.setOnRefreshListener {
                        val currentTime = SystemClock.elapsedRealtime()
                        if (currentTime - lastRefreshTime < 5000) {
                            Toast.makeText(MyApplication.instance, "Please wait 5 seconds before refreshing again", Toast.LENGTH_SHORT).show()
                        } else {
                            if (CheckNetwork().isConnected) {
                                val isComplete = viewModel.reInsertNews(AppConstants.SPORTS)
                                if (isComplete)
                                    binding.swipeToRefresh.isRefreshing = false
                            } else {
                                binding.swipeToRefresh.isRefreshing = false
                                Utils().internetUnavailableToast()
                            }
                            lastRefreshTime = currentTime
                        }
                        binding.swipeToRefresh.isRefreshing = false
                    }
                }
                7 -> {
                    viewModel.technologyNews.observe(requireActivity()) {
                        val recyclerViewState = binding.rvNews.layoutManager?.onSaveInstanceState()
                        binding.rvNews.layoutManager?.onRestoreInstanceState(recyclerViewState)
                        val adapter = NewsAdapter(it)
                        binding.rvNews.adapter = adapter
                        binding.loading.visibility = View.GONE
                        if(it.isEmpty()){
                            binding.tvNoData.visibility = View.VISIBLE
                        }
                    }

                    binding.swipeToRefresh.setOnRefreshListener {
                        val currentTime = SystemClock.elapsedRealtime()
                        if (currentTime - lastRefreshTime < 5000) {
                            Toast.makeText(MyApplication.instance, "Please wait 5 seconds before refreshing again", Toast.LENGTH_SHORT).show()
                        } else {
                            if (CheckNetwork().isConnected) {
                                val isComplete = viewModel.reInsertNews(AppConstants.TECHNOLOGY)
                                if (isComplete)
                                    binding.swipeToRefresh.isRefreshing = false
                            } else {
                                binding.swipeToRefresh.isRefreshing = false
                                Utils().internetUnavailableToast()
                            }
                            lastRefreshTime = currentTime
                        }
                        binding.swipeToRefresh.isRefreshing = false
                    }
                }
            }
        }

        // Start of search
        binding.tagAll.background = resources.getDrawable(R.drawable.round_color)

        binding.tagAll.setOnClickListener {
            if (tagAll && !tagTitle && !tagContent && !tagDescription) {
                Toast.makeText(
                    MyApplication.instance,
                    "Select at least one tag",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            if (tagTitle && tagContent && tagDescription) {
                tagTitle = false
                tagContent = false
                tagDescription = false
            }
            tagAll = !tagAll
            binding.tagTitle.background = resources.getDrawable(R.drawable.round_color)
            binding.tagContent.background = resources.getDrawable(R.drawable.round_color)
            binding.tagDescription.background = resources.getDrawable(R.drawable.round_color)
            if (tagAll) {
                binding.tagAll.background = resources.getDrawable(R.drawable.round_white)
            } else {
                binding.tagAll.background = resources.getDrawable(R.drawable.round_color)
            }
        }

        binding.tagTitle.setOnClickListener {
            tagAll = false
            binding.tagAll.background = resources.getDrawable(R.drawable.round_white)
            tagTitle = !tagTitle
            if (tagTitle) {
                binding.tagTitle.background = resources.getDrawable(R.drawable.round_color)
            } else {
                binding.tagTitle.background = resources.getDrawable(R.drawable.round_white)
            }
        }

        binding.tagContent.setOnClickListener {
            tagAll = false
            binding.tagAll.background = resources.getDrawable(R.drawable.round_white)
            tagContent = !tagContent
            if (tagContent) {
                binding.tagContent.background = resources.getDrawable(R.drawable.round_color)
            } else {
                binding.tagContent.background = resources.getDrawable(R.drawable.round_white)
            }
        }

        binding.tagDescription.setOnClickListener {
            tagAll = false
            binding.tagAll.background = resources.getDrawable(R.drawable.round_white)
            tagDescription = !tagDescription
            if (tagDescription) {
                binding.tagDescription.background = resources.getDrawable(R.drawable.round_color)
            } else {
                binding.tagDescription.background = resources.getDrawable(R.drawable.round_white)
            }
        }
        // End of search
    }
    // End of onViewCreated

    private fun searchInTopNews(searchQuery: String) {
        if (tagTitle && tagContent && tagDescription) {
            tagAll = true
            tagTitle = false
            tagContent = false
            tagDescription = false
            binding.tagAll.background = resources.getDrawable(R.drawable.round_color)
            binding.tagTitle.background = resources.getDrawable(R.drawable.round_white)
            binding.tagContent.background = resources.getDrawable(R.drawable.round_white)
            binding.tagDescription.background = resources.getDrawable(R.drawable.round_white)
        }
        if (tagAll) {
            viewModel.searchFromTopNews(AppConstants.COUNTRY, searchQuery).observe(this) {
                Log.i("TAG", "searchDatabase: $it")
                val adapter = NewsAdapter(it)
                binding.rvNews.adapter = adapter
            }
        } else if (tagTitle && tagContent) {
            viewModel.searchFromTopNewsTitleAndContent(AppConstants.COUNTRY, searchQuery)
                .observe(this) {
                    Log.i("TAG", "searchDatabase: $it")
                    val adapter = NewsAdapter(it)
                    binding.rvNews.adapter = adapter
                }
        } else if (tagTitle && tagDescription) {
            viewModel.searchFromTopNewsTitleAndDescription(AppConstants.COUNTRY, searchQuery)
                .observe(this) {
                    Log.i("TAG", "searchDatabase: $it")
                    val adapter = NewsAdapter(it)
                    binding.rvNews.adapter = adapter
                }
        } else if (tagContent && tagDescription) {
            viewModel.searchFromTopNewsContentAndDescription(AppConstants.COUNTRY, searchQuery)
                .observe(this) {
                    Log.i("TAG", "searchDatabase: $it")
                    val adapter = NewsAdapter(it)
                    binding.rvNews.adapter = adapter
                }
        } else if (tagTitle) {
            viewModel.searchFromTopNewsTitle(AppConstants.COUNTRY, searchQuery).observe(this) {
                Log.i("TAG", "searchDatabase: $it")
                val adapter = NewsAdapter(it)
                binding.rvNews.adapter = adapter
            }
        } else if (tagContent) {
            viewModel.searchFromTopNewsContent(AppConstants.COUNTRY, searchQuery).observe(this) {
                Log.i("TAG", "searchDatabase: $it")
                val adapter = NewsAdapter(it)
                binding.rvNews.adapter = adapter
            }
        } else if (tagDescription) {
            viewModel.searchFromTopNewsDescription(AppConstants.COUNTRY, searchQuery).observe(this) {
                Log.i("TAG", "searchDatabase: $it")
                val adapter = NewsAdapter(it)
                binding.rvNews.adapter = adapter
            }
        } else {
            Toast.makeText(MyApplication.instance, "Searching can't complete", Toast.LENGTH_SHORT)
                .show()
        }

    }

    private fun searchInCategory(category: String, query: String) {
        if (tagTitle && tagContent && tagDescription) {
            tagAll = true
            tagTitle = false
            tagContent = false
            tagDescription = false
            binding.tagAll.background = resources.getDrawable(R.drawable.round_color)
            binding.tagTitle.background = resources.getDrawable(R.drawable.round_white)
            binding.tagContent.background = resources.getDrawable(R.drawable.round_white)
            binding.tagDescription.background = resources.getDrawable(R.drawable.round_white)
        }
        if (tagAll) {
            viewModel.searchFromCategory(category, query).observe(this) {
                Log.i("TAG", "searchDatabase: $it")
                val adapter = NewsAdapter(it)
                binding.rvNews.adapter = adapter
            }
        } else if (tagTitle && tagContent) {
            viewModel.searchFromCategoryTitleAndContent(category, query).observe(this) {
                Log.i("TAG", "searchDatabase: $it")
                val adapter = NewsAdapter(it)
                binding.rvNews.adapter = adapter
            }
        } else if (tagTitle && tagDescription) {
            viewModel.searchFromCategoryTitleAndDescription(category, query).observe(this) {
                Log.i("TAG", "searchDatabase: $it")
                val adapter = NewsAdapter(it)
                binding.rvNews.adapter = adapter
            }
        } else if (tagContent && tagDescription) {
            viewModel.searchFromCategoryContentAndDescription(category, query).observe(this) {
                Log.i("TAG", "searchDatabase: $it")
                val adapter = NewsAdapter(it)
                binding.rvNews.adapter = adapter
            }
        } else if (tagTitle) {
            viewModel.searchFromCategoryTitle(category, query).observe(this) {
                Log.i("TAG", "searchDatabase: $it")
                val adapter = NewsAdapter(it)
                binding.rvNews.adapter = adapter
            }
        } else if (tagContent) {
            viewModel.searchFromCategoryContent(category, query).observe(this) {
                Log.i("TAG", "searchDatabase: $it")
                val adapter = NewsAdapter(it)
                binding.rvNews.adapter = adapter
            }
        } else if (tagDescription) {
            viewModel.searchFromCategoryDescription(category, query).observe(this) {
                Log.i("TAG", "searchDatabase: $it")
                val adapter = NewsAdapter(it)
                binding.rvNews.adapter = adapter
            }
        } else {
            Toast.makeText(MyApplication.instance, "Searching can't complete", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun searchOpen() {
        binding.searchView.visibility = View.VISIBLE
        binding.searchView.isIconified = false
        binding.searchView.requestFocus()
        binding.tagAll.visibility = View.VISIBLE
        binding.tagTitle.visibility = View.VISIBLE
        binding.tagDescription.visibility = View.VISIBLE
        binding.tagContent.visibility = View.VISIBLE
        searchView!!.queryHint = "Search in ${category.capitalize()}"
        TransitionManager.beginDelayedTransition(binding.toolbarCategory)
        val toolbar = requireParentFragment().view?.findViewById<Toolbar>(R.id.toolbar_categories)
        if (toolbar != null) {
            toolbar.visibility = View.GONE
        }
    }

    private fun searchClose() {
        TransitionManager.beginDelayedTransition(binding.toolbarCategory)
        binding.searchView.visibility = View.GONE
        binding.tagAll.visibility = View.GONE
        binding.tagTitle.visibility = View.GONE
        binding.tagDescription.visibility = View.GONE
        binding.tagContent.visibility = View.GONE
        binding.btnSearch.setImageResource(R.drawable.ic_baseline_search_24)
    }

    fun reInsertNews() {
        Log.d("TAG", "reInsertNews: called")
        viewModel.insertTopNews(AppConstants.COUNTRY)
        viewModel.insertNews("business")
        viewModel.insertNews("entertainment")
        viewModel.insertNews("general")
        viewModel.insertNews("health")
        viewModel.insertNews("science")
        viewModel.insertNews("sports")
        viewModel.insertNews("technology")
    }

}


