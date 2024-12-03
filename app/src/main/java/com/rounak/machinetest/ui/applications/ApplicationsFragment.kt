package com.rounak.machinetest.ui.applications

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.google.android.material.snackbar.Snackbar
import com.rounak.machinetest.R
import com.rounak.machinetest.databinding.FragmentApplicationsBinding
import com.rounak.machinetest.models.AppData
import com.rounak.machinetest.models.ApplicationScreenData
import com.rounak.machinetest.ui.applications.adapter.AppsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class ApplicationsFragment : Fragment() {
    // Get a reference to the ViewModel scoped to this Fragment
    val viewModel by viewModels<ApplicationsViewModel>()

    @Inject
    lateinit var appListAdapter: AppsAdapter

    private lateinit var binding: FragmentApplicationsBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("onCreate", "onCreate: ApplicationsFragment")
        initAdapter()
    }

    private fun initAdapter() {
        appListAdapter.setOnSwitchClickListener(
            switchClickListener = { appData: AppData, position: Int ->
                switchClicked(
                    appData = appData,
                    position = position
                )
            }
        )
        appListAdapter.setContext(requireActivity())
        appListAdapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_applications, container, false)
        setDataBinding()
        initRv()
        createCollectors()
        // Inflate the layout for this fragment
        return binding.root
    }

    private fun setDataBinding() {
        binding.appsViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun initRv() {
        binding.appsRv.setHasFixedSize(true)
        binding.appsRv.apply {
            layoutManager = GridLayoutManager(requireActivity(),1, RecyclerView.VERTICAL, false)
            this.adapter = this@ApplicationsFragment.appListAdapter
        }
        // disable rv item animation
        (binding.appsRv.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
    }

    private fun showLoader(b: Boolean){
        binding.loader.isVisible = b
        binding.searchTextField.isVisible = !b
    }

    private fun showUIMsg(msg: String){
        Snackbar.make(
            binding.root,
            msg,
            Snackbar.LENGTH_SHORT
        ).show()
    }

    private fun showAppListOnUI(appsList: List<AppData>,scrollToTop: Boolean){
        Log.d("onCreate", "showAppListOnUI: called")
        appListAdapter.differ.submitList(appsList)

        if(scrollToTop){
            // scroll to top
            binding.appsRv.post { binding.appsRv.layoutManager?.scrollToPosition(0) }
        }
    }

    private fun createCollectors() {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main.immediate) {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.apps.collectLatest { appsScreenData: ApplicationScreenData ->
                        showAppListOnUI(
                            appsList = appsScreenData.appsDataList,
                            scrollToTop = appsScreenData.scrollToTop
                        )
                    }
                }

                launch {
                    viewModel.loading.collectLatest { isLoading: Boolean ->
                        showLoader(b = isLoading)
                    }
                }
                launch {
                    viewModel.uiMsg.collectLatest { msg ->
                        showUIMsg(msg = msg)
                    }
                }
            }
        }


    }

    private fun switchClicked(appData: AppData, position: Int) {
        Log.d("onCreate", "switchClicked position: $position")
        viewModel.updateAppListOnSwitchSelect(
            appDataSelected = appData
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("onCreate", "onDestroy: ApplicationsFragment")
    }

}