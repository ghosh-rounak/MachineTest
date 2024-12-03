package com.rounak.machinetest.ui

import android.content.res.Resources
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.google.android.material.tabs.TabLayoutMediator
import com.rounak.machinetest.R
import com.rounak.machinetest.constants.Constant.TAB1
import com.rounak.machinetest.constants.Constant.TAB2
import com.rounak.machinetest.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setDataBinding()
        setTabLayoutWithViewPager()
        setOnClicks()
    }

    private fun setOnClicks(){
        binding.backImgBtn.setOnClickListener{
            finish()
        }
    }





    private fun setDataBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.mainViewModel = viewModel
        binding.lifecycleOwner = this
    }

    private fun setTabLayoutWithViewPager() {
        binding.viewPager.adapter = PagerAdapter(supportFragmentManager, lifecycle)

        TabLayoutMediator(binding.tabLayout,binding.viewPager){tab,index ->
            tab.text = when(index){
                0 -> {TAB1}
                1 -> {TAB2}
                else -> {throw Resources.NotFoundException("Position Not Found")}
            }
        }.attach()
    }
}