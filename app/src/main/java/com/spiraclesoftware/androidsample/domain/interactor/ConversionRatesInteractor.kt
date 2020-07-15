package com.spiraclesoftware.androidsample.domain.interactor

import com.spiraclesoftware.androidsample.data.memory.MemoryDataSource
import com.spiraclesoftware.androidsample.data.network.NetworkDataSource
import com.spiraclesoftware.androidsample.domain.model.ConversionRates
import com.spiraclesoftware.androidsample.domain.model.CurrencyCode

class ConversionRatesInteractor(
    private val networkDataSource: NetworkDataSource,
    private val memoryDataSource: MemoryDataSource
) {

    suspend fun fetchConversionRates(baseCurrency: CurrencyCode): ConversionRates {
        return networkDataSource.fetchConversionRates(baseCurrency).also {
            memoryDataSource.saveConversionRates(baseCurrency, it)
        }
    }

    suspend fun getConversionRates(baseCurrency: CurrencyCode): ConversionRates {
        val cached = memoryDataSource.getConversionRates(baseCurrency)
        return cached ?: fetchConversionRates(baseCurrency)
    }

}
