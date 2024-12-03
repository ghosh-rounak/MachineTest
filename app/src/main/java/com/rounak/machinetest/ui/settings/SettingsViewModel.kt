package com.rounak.machinetest.ui.settings

import android.util.Log
import androidx.databinding.Observable
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
) : ViewModel(), Observable{


    init {
        Log.d("onCreate", "init: SettingsViewModel")
    }


    override fun onCleared() {
        super.onCleared()
        Log.d("onCreate", "onCleared: SettingsViewModel")
    }




    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }
}