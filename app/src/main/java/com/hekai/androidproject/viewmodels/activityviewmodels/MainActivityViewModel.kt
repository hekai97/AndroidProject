package com.hekai.androidproject.viewmodels.activityviewmodels

import androidx.lifecycle.*
import com.hekai.androidproject.localdatas.LUser
import com.hekai.androidproject.localdatas.LocalUserRepository
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class MainActivityViewModel(private val repository:LocalUserRepository):ViewModel() {
    //当前登录的用户，如果为空则没有用户登录
    val currentUser:LiveData<LUser> = repository.currentUser.asLiveData()
    fun insert(lUser: LUser)=viewModelScope.launch {
        repository.insert(lUser)
    }
    fun deleteAll()=viewModelScope.launch {
        repository.deleteAll()
    }
    fun logOut()=viewModelScope.launch {
        repository.logOut()
    }
    fun updateUserPublishNumber()=viewModelScope.launch {
        repository.updateUserPublishNumber()
    }
}
class MainActivityViewModelFactory(private val repository: LocalUserRepository):ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MainActivityViewModel::class.java)){
            return MainActivityViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknow ViewModel class")
    }

}