package com.bobby.aad_prep.repo

import androidx.lifecycle.MutableLiveData
import com.bobby.aad_prep.api.NetworkClient
import com.bobby.aad_prep.api.state.Resource
import com.bobby.aad_prep.api.state.Status
import com.bobby.aad_prep.api.model.Repo
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class GithubRepository constructor(private val result: MutableLiveData<Resource<List<Repo>>>) {

    suspend fun searchRepo(query: String, page: Int, itemsPerPage: Int){

        val apiService = NetworkClient().create()
        result.postValue(Resource(status = Status.LOADING))

        GlobalScope.launch {

            try {

                val response = apiService.searchRepos(query = query, page = page, itemsPerPage = itemsPerPage)
                if (response.isSuccessful){
                    result.postValue(Resource(status = Status.SUCCESS, data = response.body()?.items))
                }else{
                    result.postValue(Resource(status = Status.ERROR, message = response.errorBody().toString(),
                        data = response.body()?.items))
                }

            }catch (e: Exception){
                result.postValue(Resource(status = Status.ERROR, message = e.message.toString()))
            }

        }
    }
}