package com.graphicless.newsapp.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.graphicless.newsapp.ui.fragment.CategoryFragment
import com.graphicless.newsapp.utils.AppConstants

class CategoryAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 8

    override fun createFragment(position: Int): Fragment {
        // Return a NEW fragment instance in createFragment(int)
        val fragment = CategoryFragment()
        fragment.arguments = Bundle().apply {
            // Our object is just an integer :-P
            putInt(AppConstants.CATEGORY_TAB_NUMBER, position)
        }
        return fragment
    }
}