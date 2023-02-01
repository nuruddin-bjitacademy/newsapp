package com.graphicless.newsapp.ui.fragment

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.transition.TransitionManager
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.graphicless.newsapp.R
import com.graphicless.newsapp.adapter.FavoriteNewsAdapter
import com.graphicless.newsapp.databinding.FragmentFavoriteBinding
import com.graphicless.newsapp.utils.MyApplication
import com.graphicless.newsapp.viewmodel.NewsViewModel
import kotlinx.android.synthetic.main.activity_main.*

class FavoriteFragment : Fragment() {

    private var searchView: SearchView? = null
    private var queryTextListener: SearchView.OnQueryTextListener? = null

    private val viewModel: NewsViewModel by viewModels()

    private lateinit var _binding: FragmentFavoriteBinding
    private val binding get() = _binding

    private var tagAll: Boolean = true
    private var tagTitle: Boolean = false
    private var tagContent: Boolean = false
    private var tagDescription: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        requireActivity().bottom_nav_menu.visibility = View.VISIBLE

        (activity as AppCompatActivity?)!!.setSupportActionBar(binding.toolbarFavorite)

        viewModel.favoriteNews.observe(requireActivity()) {
            val recyclerViewState = binding.rvFavoriteNews.layoutManager?.onSaveInstanceState()
            binding.rvFavoriteNews.layoutManager?.onRestoreInstanceState(recyclerViewState)
            binding.rvFavoriteNews.adapter = FavoriteNewsAdapter(it)
            binding.loading.visibility = View.GONE
            if(it.isEmpty()){
                binding.tvNoData.visibility = View.VISIBLE
            }
        }

        val bottomNavigationView = activity?.bottom_nav_menu
        binding.rvFavoriteNews.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
                }
            }
        })

        binding.btnSearch.setOnClickListener {
            binding.searchView.visibility = View.VISIBLE
            binding.searchView.isIconified = false
            binding.searchView.requestFocus()
            searchView!!.queryHint = "Search in Favorites"
            binding.layoutTag.visibility = View.VISIBLE
            binding.btnSearch.animate().translationX(500f)
        }

        searchView = binding.searchView

        queryTextListener = object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(query: String): Boolean {
                    val searchQuery = "%$query%"
                    searchInFavoriteNews(searchQuery)
                    Log.i("onQueryTextChange", "query: $query")

                return true
            }

            override fun onQueryTextSubmit(query: String): Boolean {
//                    Log.i("onQueryTextSubmit", query)
                return true
            }
        }
        searchView!!.setOnQueryTextListener(queryTextListener)

        binding.searchView.setOnCloseListener {
            TransitionManager.beginDelayedTransition(binding.toolbarFavorite)
            binding.searchView.visibility = View.GONE
            binding.layoutTag.visibility = View.GONE
            binding.btnSearch.animate().translationX(0f)
            return@setOnCloseListener true
        }

        // Start of tag in search
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
        // End of tag in search
    } // End onCreate

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
        val searchItem: MenuItem = menu.findItem(R.id.menu_search)
        val searchManager =
            requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager

        searchView = searchItem.actionView as SearchView?
        if (searchView != null) {
            searchView!!.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))

            queryTextListener = object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(query: String): Boolean {
                    val searchQuery = "%$query%"
                    searchInFavoriteNews(searchQuery)
                    return true
                }

                override fun onQueryTextSubmit(query: String): Boolean {
//                    Log.i("onQueryTextSubmit", query)
                    return true
                }
            }
            searchView!!.setOnQueryTextListener(queryTextListener)
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun searchInFavoriteNews(searchQuery: String) {
        if(tagTitle && tagContent && tagDescription){
            tagAll = true
            tagTitle = false
            tagContent = false
            tagDescription = false
            binding.tagAll.background = resources.getDrawable(R.drawable.round_color)
            binding.tagTitle.background = resources.getDrawable(R.drawable.round_white)
            binding.tagContent.background = resources.getDrawable(R.drawable.round_white)
            binding.tagDescription.background = resources.getDrawable(R.drawable.round_white)
        }
        if(tagAll){
            viewModel.searchFromFavorite(searchQuery).observe(this) {
                Log.i("TAG", "searchDatabase: $it")
                val adapter = FavoriteNewsAdapter(it)
                binding.rvFavoriteNews.adapter = adapter
            }
        }
        else if(tagTitle && tagContent){
            viewModel.searchFromFavoriteTitleAndContent(searchQuery).observe(this) {
                Log.i("TAG", "searchDatabase: $it")
                val adapter = FavoriteNewsAdapter(it)
                binding.rvFavoriteNews.adapter = adapter
            }
        }
        else if(tagTitle && tagDescription){
            viewModel.searchFromFavoriteTitleAndDescription(searchQuery).observe(this) {
                Log.i("TAG", "searchDatabase: $it")
                val adapter = FavoriteNewsAdapter(it)
                binding.rvFavoriteNews.adapter = adapter
            }
        }
        else if(tagContent && tagDescription){
            viewModel.searchFromFavoriteContentAndDescription(searchQuery).observe(this) {
                Log.i("TAG", "searchDatabase: $it")
                val adapter = FavoriteNewsAdapter(it)
                binding.rvFavoriteNews.adapter = adapter
            }
        }
        else if(tagTitle){
            viewModel.searchFromFavoriteTitle(searchQuery).observe(this) {
                Log.i("TAG", "searchDatabase: $it")
                val adapter = FavoriteNewsAdapter(it)
                binding.rvFavoriteNews.adapter = adapter
            }
        }
        else if(tagContent){
            viewModel.searchFromFavoriteContent(searchQuery).observe(this) {
                Log.i("TAG", "searchDatabase: $it")
                val adapter = FavoriteNewsAdapter(it)
                binding.rvFavoriteNews.adapter = adapter
            }
        }
        else if(tagDescription){
            viewModel.searchFromFavoriteDescription(searchQuery).observe(this) {
                Log.i("TAG", "searchDatabase: $it")
                val adapter = FavoriteNewsAdapter(it)
                binding.rvFavoriteNews.adapter = adapter
            }
        }
        else{
            Toast.makeText(MyApplication.instance, "Searching can't complete", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_search -> {

            }
            else -> {}
        }
        searchView!!.setOnQueryTextListener(queryTextListener)
        return super.onOptionsItemSelected(item)
    }

}