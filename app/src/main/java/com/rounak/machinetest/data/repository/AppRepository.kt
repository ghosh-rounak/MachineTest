package com.rounak.machinetest.data.repository
import android.util.Log
import com.rounak.machinetest.data.network.RetrofitService
import com.rounak.machinetest.data.network.responses.App
import com.rounak.machinetest.data.network.responses.AppsResponse
import com.rounak.machinetest.models.AppData
import com.rounak.machinetest.utils.InternetConnection
import com.rounak.machinetest.utils.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppRepository @Inject constructor(
    private val api: RetrofitService,
    private val internetConnection: InternetConnection
){

    private fun isInternetAvailable(): Boolean = internetConnection.isNetworkAvailable()

    internal fun getAppsList(
    ): Flow<Response<List<AppData>>> = flow {
        if (!isInternetAvailable()) {
            emit(Response.Error(errorMessage = "No Internet", errorCode = null))
        } else {
            val response: retrofit2.Response<AppsResponse> = api.getAppList()
            emit(handleAppsResponse(response = response))
        }
    }.catch { exception ->
        exception.printStackTrace()
        emit(
            Response.Error(
                errorMessage = exception.localizedMessage ?: "Unknown Error",
                errorCode = null
            )
        )
    }

    private fun handleAppsResponse(
        response: retrofit2.Response<AppsResponse>
    ): Response<List<AppData>> =
        if (response.isSuccessful) {
            val resultResponse = response.body()
            if (resultResponse == null) {
                Response.Error(
                    errorMessage = "An unknown error occurred",
                    errorCode = response.code()
                )
            } else {
                val successResponse: AppsResponse = resultResponse
                Log.d("onCreate", "handleAppsResponse Success: $successResponse")
                val success: Boolean = successResponse.success
                val message: String = successResponse.message
                if (success) {
                    val uiAppList: List<AppData> = successResponse.actualData.appList.map {dataItem: App ->
                        AppData(
                            id = dataItem.appId,
                            appName = dataItem.appName,
                            appImgUrl = dataItem.appIcon,
                            isActive = dataItem.status.equals("active",ignoreCase = true)
                        )
                    }
                    Response.Success(uiAppList)
                } else {
                    Response.Error(
                        errorMessage = message,
                        errorCode = null
                    )
                }
            }

        } else {
            val errorResponse: String = response.errorBody()?.string() ?: "Unknown Error"
            Log.d("onCreate", "handleAppsResponse Error: $errorResponse")
            // 4XX or 5XX error returned by backend api
            val apiError = "Something went wrong on server side!"
            Response.Error(
                errorMessage = apiError,
                errorCode = response.code()
            )
        }
}