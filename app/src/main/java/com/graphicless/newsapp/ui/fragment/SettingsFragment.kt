package com.graphicless.newsapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.graphicless.newsapp.R
import com.graphicless.newsapp.databinding.FragmentSettingsBinding
import com.graphicless.newsapp.utils.*
import com.graphicless.newsapp.viewmodel.NewsViewModel
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : Fragment() {

    private lateinit var _binding: FragmentSettingsBinding
    private val binding get() = _binding

    val viewModel: NewsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreference = SharedPreference(requireContext())

        if (sharedPreference.getValueString("theme") != null) {
            val myTheme = sharedPreference.getValueString("theme")
            switch_theme.isChecked = myTheme != "light"
        } else {
            switch_theme.isChecked = false
        }

        switch_theme.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                sharedPreference.save("theme", "dark")
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                sharedPreference.save("theme", "light")
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        val items = resources.getStringArray(R.array.country_name)
        val adapter =
            ArrayAdapter(MyApplication.instance, android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerSelectCountry.adapter = adapter

        binding.spinnerSelectCountry.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    // Do something when item is selected
                    AppConstants.COUNTRY = resources.getStringArray(R.array.country_code)[position]
//                    Constraints.COUNTRY_SPINNER_POSITION = position
                    binding.spinnerSelectCountry.setSelection(position)
                    sharedPreference.save("country_position", position)
                    if (CheckNetwork().isConnected) {
                        viewModel.reInsertTopNews(resources.getStringArray(R.array.country_code)[position])
                    }else {
//                        Utils().internetUnavailableToast()
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Do something when nothing is selected
                }
            }

        if (sharedPreference.isContains("country_position")) {
            val countryPosition = sharedPreference.getValueInt("country_position")
            binding.spinnerSelectCountry.setSelection(countryPosition)
        } else {
            binding.spinnerSelectCountry.setSelection(52)
        }

        binding.layoutRadioButton1.setOnClickListener {
            binding.layoutRadioButton2.isChecked = false
            binding.layoutRadioButton1.isChecked = true
            sharedPreference.save("layout", "compact")
        }

        binding.layoutRadioButton2.setOnClickListener {
            binding.layoutRadioButton1.isChecked = false
            binding.layoutRadioButton2.isChecked = true
            sharedPreference.save("layout", "full")
        }

        if (sharedPreference.getValueString("layout") != null) {
            val layout = sharedPreference.getValueString("layout")
            if (layout == "compact") {
                binding.layoutRadioButton1.isChecked = true
                binding.layoutRadioButton2.isChecked = false
            }else{
                binding.layoutRadioButton2.isChecked = true
                binding.layoutRadioButton1.isChecked = false
            }
        } else {
            binding.layoutRadioButton1.isChecked = true
            binding.layoutRadioButton2.isChecked = false
        }
    }

}
