package com.rounak.machinetest.ui

import android.content.res.Resources
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.rounak.machinetest.constants.Constant.TAB_COUNT
import com.rounak.machinetest.ui.applications.ApplicationsFragment
import com.rounak.machinetest.ui.settings.SettingsFragment

class PagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle)
    :FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int = TAB_COUNT

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> {ApplicationsFragment()}
            1 -> {SettingsFragment()}
            else -> {throw Resources.NotFoundException("Position Not Found")}
        }
    }

}