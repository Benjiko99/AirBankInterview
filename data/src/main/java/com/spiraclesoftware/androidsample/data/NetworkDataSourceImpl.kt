package com.spiraclesoftware.androidsample.data

import android.net.Uri
import com.spiraclesoftware.androidsample.data.mapper.ConversionRateMapper
import com.spiraclesoftware.androidsample.data.mapper.ConversionRatesMapper
import com.spiraclesoftware.androidsample.data.mapper.MoneyMapper
import com.spiraclesoftware.androidsample.data.mapper.TransactionMapper
import com.spiraclesoftware.androidsample.domain.NetworkDataSource
import com.spiraclesoftware.androidsample.domain.Result
import com.spiraclesoftware.androidsample.domain.model.ConversionRates
import com.spiraclesoftware.androidsample.domain.model.Transaction
import com.spiraclesoftware.androidsample.remote.MainApi
import kotlinx.coroutines.flow.flow
import java.util.*

class NetworkDataSourceImpl(
    private val mainApi: MainApi,
) : NetworkDataSource {

    // TODO: Inject?
    private val moneyMapper = MoneyMapper()
    private val transactionMapper = TransactionMapper(moneyMapper)
    private val conversionRateMapper = ConversionRateMapper()
    private val conversionRatesMapper = ConversionRatesMapper(conversionRateMapper)

    override suspend fun fetchTransactions() = flow {
        try {
            emit(Result.Loading)
            emit(Result.Success(tryFetchTransactions()))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    private suspend fun tryFetchTransactions() =
        mainApi.fetchTransactions().items.map { dto ->
            transactionMapper.mapToDomain(dto)
        }

    override suspend fun fetchConversionRates(baseCurrency: Currency): ConversionRates {
        return mainApi.fetchConversionRates(baseCurrency).let { dto ->
            conversionRatesMapper.mapToDomain(dto)
        }
    }

    override suspend fun updateTransactionNote(id: String, note: String): Transaction {
        return mainApi.updateTransactionNote(id, note).let { dto ->
            transactionMapper.mapToDomain(dto)
        }
    }

    override suspend fun updateTransactionCategory(id: String, category: String): Transaction {
        return mainApi.updateTransactionCategory(id, category).let { dto ->
            transactionMapper.mapToDomain(dto)
        }
    }

    override suspend fun removeAttachment(id: String, uri: Uri) {
        // NOTE: We don't have a backend for this feature yet, pretend we got a successful response.
        // Success is indicated by not throwing an exception, so we'll simply do nothing here.

        // TODO: Once we have a backend, use this code
        //return mainApi.removeAttachment(id.value, uri)
    }

    override suspend fun uploadAttachment(id: String, uri: Uri): Uri {
        // NOTE: We don't have a backend for this feature yet, pretend we got a successful response
        return uri

        // TODO: Once we have a backend, use this code
//        val image = uri.toFile().asRequestBody()
//        return mainApi.uploadAttachment(id.value, image)
    }

}
