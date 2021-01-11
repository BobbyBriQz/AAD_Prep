package com.bobby.aad_prep.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bobby.aad_prep.api.state.Resource
import com.bobby.aad_prep.api.model.Repo
import com.bobby.aad_prep.repo.GithubRepository
import kotlinx.coroutines.launch

class GithubViewModel : ViewModel() {
    val repoSearchResultLiveData : MutableLiveData<Resource<List<Repo>>> =  MutableLiveData()
    private val repository = GithubRepository(repoSearchResultLiveData)

    fun searchRepo(query: String, page: Int, itemsPerPage: Int){
        viewModelScope.launch {
            repository.searchRepo(query, page, itemsPerPage)
        }
    }
}
