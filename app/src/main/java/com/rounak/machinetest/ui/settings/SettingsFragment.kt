package com.rounak.machinetest.ui.settings

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.rounak.machinetest.R
import com.rounak.machinetest.databinding.FragmentSettingsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue


@AndroidEntryPoint
class SettingsFragment : Fragment() {
    // Get a reference to the ViewModel scoped to this Fragment
    val viewModel by viewModels<SettingsViewModel>()

    private lateinit var binding: FragmentSettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("onCreate", "onCreate: SettingsFragment")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false)
        setDataBinding()
        // Inflate the layout for this fragment
        return binding.root
    }

    private fun setDataBinding() {
        binding.settingsViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("onCreate", "onDestroy: SettingsFragment")
    }

}