package com.rounak.machinetest.data.network

import com.rounak.machinetest.data.network.responses.AppsResponse
import retrofit2.Response
import retrofit2.http.POST

interface RetrofitService {

    @POST("apps/list?kid_id=378")
    suspend fun getAppList(
    ): Response<AppsResponse>

}