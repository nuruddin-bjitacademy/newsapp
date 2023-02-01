package com.graphicless.newsapp.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.graphicless.newsapp.R
import com.graphicless.newsapp.adapter.CategoryAdapter
import com.graphicless.newsapp.databinding.FragmentCategoriesBinding
import kotlinx.android.synthetic.main.tab_item_layout.*

class CategoriesFragment : Fragment() {

    private lateinit var _binding: FragmentCategoriesBinding
    private val binding get() = _binding

    private lateinit var categoryAdapter: CategoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoriesBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        (activity as AppCompatActivity?)!!.setSupportActionBar(binding.toolbarCategories)

        val categories = requireContext().resources.getStringArray(R.array.news_categories)

        val icons = arrayOf(
            R.drawable.icon_top_news_tab,
            R.drawable.icon_business_tab,
            R.drawable.icon_entertainment_tab,
            R.drawable.icon_general_tab,
            R.drawable.icon_health_tab,
            R.drawable.icon_science_tab,
            R.drawable.icon_sports_tab,
            R.drawable.icon_technology_tab
        )

        when(binding.tabLayout.selectedTabPosition){
            0 ->{
                tab_icon.setImageDrawable(resources.getDrawable(R.drawable.icon_favorite_filled))
            }
        }

        Log.d("TAG", "tab position: ${binding.tabLayout.selectedTabPosition}")

        categoryAdapter = CategoryAdapter(this)
        binding.pager.adapter = categoryAdapter
        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->

            val tabView = layoutInflater.inflate(R.layout.tab_item_layout, null)
            val tabIcon = tabView.findViewById<ImageView>(R.id.tab_icon)
            val tabText = tabView.findViewById<TextView>(R.id.tab_text)

//            tabIcon.setImageResource(icons[position])
//            tabText.text = categories[position].toUpperCase()
//            tab.customView = tabView
            tab.text = categories[position]
            tab.icon = resources.getDrawable(icons[position])
        }.attach()
    }
}