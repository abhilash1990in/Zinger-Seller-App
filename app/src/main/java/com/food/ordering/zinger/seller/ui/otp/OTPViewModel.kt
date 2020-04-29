package com.food.ordering.zinger.seller.ui.otp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.food.ordering.zinger.seller.data.local.Resource
import com.food.ordering.zinger.seller.data.model.Response
import com.food.ordering.zinger.seller.data.model.UserModel
import com.food.ordering.zinger.seller.data.model.UserShopListModel
import com.food.ordering.zinger.seller.data.model.UserShopModel
import com.food.ordering.zinger.seller.data.retrofit.SellerRepository
import com.food.ordering.zinger.seller.data.retrofit.UserRespository
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class OTPViewModel(private val userRespository: UserRespository,private val sellerRepository: SellerRepository) : ViewModel() {
    private val performLogin = MutableLiveData<Resource<Response<UserShopListModel>>>()
    val performLoginStatus: LiveData<Resource<Response<UserShopListModel>>>
        get() = performLogin

    fun login(userModel: UserModel) {
        viewModelScope.launch {
            try {
                performLogin.value = Resource.loading()
                val response = userRespository.login(userModel)
                if(response.code==1)
                    performLogin.value = Resource.success(response)
                else
                    performLogin.value = Resource.error(message = response.message)
            } catch (e: Exception) {
                println("fetch stats failed ${e.message}")
                if (e is UnknownHostException) {
                    performLogin.value = Resource.offlineError()
                } else {
                    performLogin.value = Resource.error(e)
                }
            }
        }
    }


    private val acceptInvite = MutableLiveData<Resource<Response<UserShopListModel>>>()
    val acceptInviteResponse: LiveData<Resource<Response<UserShopListModel>>>
        get() = acceptInvite

    fun acceptInvite(userShop: UserShopModel) {

        viewModelScope.launch {
            try {
                acceptInvite.value = Resource.loading()
                val response = sellerRepository.acceptInvite(userShop)

                if (response.code == 1)
                    acceptInvite.value = Resource.success(response)
                else
                    acceptInvite.value = Resource.error(message = response.message)

            } catch (e: Exception) {
                if (e is UnknownHostException) {
                    acceptInvite.value = Resource.offlineError()
                } else {
                    acceptInvite.value = Resource.error(e)
                }
            }
        }

    }
}