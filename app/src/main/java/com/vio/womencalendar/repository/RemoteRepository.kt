package com.vio.womencalendar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vio.womencalendar.app.Injection
import com.vio.womencalendar.model.arcticle.Article
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object RemoteRepository: Repository {

    private val api = Injection.provideServerAPI()

    override fun getArticles(): LiveData<List<Article>> {
        val liveData = MutableLiveData<List<Article>>()

        api.getArticles().enqueue(object : Callback<List<Article>> {
            override fun onResponse(call: Call<List<Article>>?, response: Response<List<Article>>?) {
                if (response != null && response.isSuccessful) {
                    liveData.value = response.body()
                } else {
                    //liveData.value = Either.error(ApiError.REPOS, null)
                }
            }

            override fun onFailure(call: Call<List<Article>>?, t: Throwable?) {
                //liveData.value = Either.error(ApiError.REPOS, null)
            }
        })

        return liveData
    }
}