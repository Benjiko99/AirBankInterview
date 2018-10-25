package com.spiraclesoftware.airbankinterview.transaction.detail.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.spiraclesoftware.airbankinterview.api.ApiService
import com.spiraclesoftware.airbankinterview.transaction.detail.domain.TransactionDetail
import com.spiraclesoftware.airbankinterview.transaction.list.domain.TransactionId
import com.spiraclesoftware.core.data.AppExecutors
import com.spiraclesoftware.core.data.NetworkBoundResource
import com.spiraclesoftware.core.data.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransactionDetailRepository @Inject constructor(
    private val appExecutors: AppExecutors,
    private val apiService: ApiService,
    private val cache: TransactionDetailCache
) {

    fun loadTransactionDetail(transactionId: TransactionId): LiveData<Resource<TransactionDetail>> {
        return object : NetworkBoundResource<TransactionDetail, TransactionDetail>(appExecutors) {

            override fun saveCallResult(data: TransactionDetail) {
                cache.set(transactionId, data)
            }

            override fun shouldFetch(data: TransactionDetail?): Boolean {
                return cache.cacheIsDirty || data == null
            }

            override fun loadFromCache(): LiveData<TransactionDetail> {
                return MutableLiveData<TransactionDetail>().apply { value = cache.get(transactionId) }
            }

            override fun createCall() = apiService.transactionDetail(transactionId)

            override fun onFetchFailed() {
                cache.cacheIsDirty = true
            }
        }.asLiveData()
    }
}