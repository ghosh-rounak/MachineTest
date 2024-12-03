package com.rounak.machinetest.ui.applications.adapter
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.rounak.machinetest.databinding.RvItemAppBinding
import com.rounak.machinetest.models.AppData
import javax.inject.Inject
import com.rounak.machinetest.R


class AppsAdapter @Inject constructor(
) : RecyclerView.Adapter<AppsAdapter.AppViewHolder>() {

    private lateinit var ctx: Context
    private lateinit var switchClickListener:(AppData, Int)->Unit

    internal fun setOnSwitchClickListener(
        switchClickListener:(AppData, Int)->Unit
    ){
        this.switchClickListener = switchClickListener
    }

    internal fun setContext(ctx: Context){
        this.ctx = ctx
    }

    private val differCallback = object : DiffUtil.ItemCallback<AppData>() {
        override fun areItemsTheSame(oldItem: AppData, newItem: AppData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: AppData, newItem: AppData): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding : RvItemAppBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.rv_item_app,parent,false)
        return AppViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        holder.bind(
            differ.currentList[position],
            switchClickListener
        )
    }




    inner class AppViewHolder(
        private val binding: RvItemAppBinding
    ): RecyclerView.ViewHolder(binding.root){

        fun bind(
            appData: AppData,
            switchClickListener:(AppData, Int)->Unit
        ){
            binding.appData = appData

            binding.activeSwitchContainer.setOnClickListener{
                switchClickListener(appData,bindingAdapterPosition)
            }

            binding.executePendingBindings()
        }
    }

}

