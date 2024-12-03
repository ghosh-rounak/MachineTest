package com.rounak.machinetest.ui.applications

import android.util.Log
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collectLatest
import com.rounak.machinetest.data.repository.AppRepository
import com.rounak.machinetest.models.AppData
import com.rounak.machinetest.models.ApplicationScreenData
import com.rounak.machinetest.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ApplicationsViewModel @Inject constructor(
     private val repository: AppRepository
) : ViewModel(), Observable{
    @Bindable
    val inputSearch: MutableStateFlow<String?> = MutableStateFlow(null)

    private val _uiMsg = Channel<String>()
    val uiMsg = _uiMsg.receiveAsFlow()

    private val _apps: MutableStateFlow<ApplicationScreenData> = MutableStateFlow(
        ApplicationScreenData(
            appsDataList = listOf(),
            scrollToTop = true
        )
    )
    val apps: StateFlow<ApplicationScreenData> = _apps.asStateFlow()

    private val _loading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private var allAppList: List<AppData> = listOf()
    private var serverAppList: ArrayList<AppData> = arrayListOf()

    init {
        Log.d("onCreate", "init: ApplicationsViewModel")

        viewModelScope.launch{
            setUpInputSearchCollector()
        }

        viewModelScope.launch{
            getAppListFromServer()
        }
    }

    private fun showLoader(b:Boolean){
        _loading.value = b
    }
    private suspend fun setUpInputSearchCollector(){
            inputSearch
                .filterNotNull()
                .collectLatest { searchQuery: String ->
                    updateAppListOnSearch(searchQuery = searchQuery)
                }
    }

    private suspend fun getAppListFromServer(){
        showLoader(true)
        repository.getAppsList().collectLatest { response:Response<List<AppData>> ->
            when (response) {
                is Response.Success -> {
                    serverAppList = ArrayList(response.successData!!)
                    allAppList = serverAppList.map { it }
                    updateUIAppList(
                        appList = allAppList,
                        scrollToTop = true
                    )
                    showLoader(false)
                }
                is Response.Error -> {
                    val error = response.errorMessage!!
                    val errorCode:Int? = response.errorCode
                    Log.d("onCreate", "getAppListFromServer error: $error")
                    Log.d("onCreate", "getAppListFromServer errorCode: $errorCode")
                    showLoader(false)
                    _uiMsg.send(error)
                }
                is Response.Loading -> {
                    /*NO-OP */
                }
            }
        }
    }

    private fun updateUIAppList(
        appList: List<AppData>,
        scrollToTop: Boolean
    ){
        _apps.value = ApplicationScreenData(
            appsDataList = appList,
            scrollToTop = scrollToTop
        )
    }

    internal fun updateAppListOnSwitchSelect(appDataSelected: AppData){
        val originalIndex = serverAppList.indexOfFirst { d: AppData ->
            d.id == appDataSelected.id
        }
        serverAppList[originalIndex] = AppData(
            id = appDataSelected.id,
            appName = appDataSelected.appName,
            appImgUrl = appDataSelected.appImgUrl,
            isActive = !appDataSelected.isActive
        )
        val modifiedAppList: List<AppData> = allAppList.map { appData: AppData ->
            if (appData.id == appDataSelected.id){
                 AppData(
                    id = appDataSelected.id,
                    appName = appDataSelected.appName,
                    appImgUrl = appDataSelected.appImgUrl,
                    isActive = !appDataSelected.isActive
                )
            }else{
                 appData
            }
        }

        allAppList = modifiedAppList


        updateUIAppList(
            appList = allAppList,
            scrollToTop = false
        )
    }

    private fun updateAppListOnSearch(searchQuery: String){
        //searchQuery.isBlank()
        if(searchQuery.isEmpty()){
            /*
            serverAppList.forEachIndexed { index: Int, sAppData: AppData ->
                allAppList.find { a: AppData ->
                    a.id == sAppData.id
                }?.let { updatedAppDataItem ->
                    serverAppList[index] = updatedAppDataItem
                }
            }
            */


            /*
            for(i in 0 until serverAppList.size){
                val originalApp = serverAppList[i]
                val updatedItem = allAppList.find { a: AppData ->
                    a.id == originalApp.id
                }
                updatedItem?.let { updated ->
                    serverAppList[i] = updated
                    Log.d("ClearTest", "i: $i")
                }
            }
            */


            //Log.d("ClearTest", "serverAppList: $serverAppList")
            //Log.d("ClearTest", "allAppList: $allAppList")

            allAppList = serverAppList.map { it }
            updateUIAppList(
                appList = allAppList,
                scrollToTop = true
            )
        }else{
            val modifiedAppList: List<AppData> = allAppList.filter { appData: AppData ->
                appData.appName.startsWith(searchQuery,ignoreCase = true)
            }
            allAppList = modifiedAppList
            updateUIAppList(
                appList = allAppList,
                scrollToTop = true
            )
        }
    }






    override fun onCleared() {
        super.onCleared()
        Log.d("onCreate", "onCleared: ApplicationsViewModel")
    }





    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }
}