package com.spiraclesoftware.androidsample.domain.model

import com.spiraclesoftware.core.domain.Identifiable
import java.util.*

data class ConversionRates(
    val baseCurrency: Currency,
    val validityDate: Date,
    val rates: List<ConversionRate>
) : Identifiable<CurrencyCode> {

    override fun getUniqueId() = baseCurrency.currencyCode()

    /**
     * @return the conversion rate for a given currency
     */
    fun findByCurrency(currency: Currency): ConversionRate? {
        return if (currency == baseCurrency) {
            ConversionRate(baseCurrency, 1f)
        } else {
            rates.find { it.currency == currency }
        }
    }

}
