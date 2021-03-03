package com.spiraclesoftware.androidsample.feature.transaction_list

import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.domain.entity.Money
import com.spiraclesoftware.androidsample.domain.entity.Transaction
import com.spiraclesoftware.androidsample.formatter.DateTimeFormat
import com.spiraclesoftware.androidsample.formatter.MoneyFormat
import com.spiraclesoftware.androidsample.formatter.TransactionCategoryFormatter
import com.spiraclesoftware.androidsample.formatter.TransactionStatusCodeFormatter
import org.koin.java.KoinJavaComponent.inject
import java.time.ZonedDateTime

class TransactionListFormatter {

    private val statusCodeFormatter by inject(TransactionStatusCodeFormatter::class.java)
    private val categoryFormatter by inject(TransactionCategoryFormatter::class.java)

    fun headerModel(
        dateTime: ZonedDateTime,
        contributionToBalance: Money
    ) = HeaderModel(
        date = dateTime.format(DateTimeFormat.PRETTY_DATE),
        contribution = MoneyFormat(contributionToBalance)
            .formatSigned(showSignWhenPositive = false)
    )

    fun transactionModel(transactions: List<Transaction>): List<TransactionModel> =
        transactions.map(::transactionModel)

    fun transactionModel(transaction: Transaction): TransactionModel = with(transaction) {
        val iconRes: Int
        val iconTintRes: Int

        if (isSuccessful()) {
            iconTintRes = categoryFormatter.colorRes(category)
            iconRes = categoryFormatter.drawableRes(category)
        } else {
            iconTintRes = R.color.transaction_status__declined
            iconRes = R.drawable.ic_status_declined
        }

        return TransactionModel(
            id = id,
            name = name,
            iconRes = iconRes,
            iconTintRes = iconTintRes,
            amount = MoneyFormat(signedMoney).format(this),
            processingDate = processingDate.format(DateTimeFormat.PRETTY_DATE_TIME),
            statusCodeRes = statusCodeFormatter.stringRes(statusCode),
            contributesToAccountBalance = contributesToAccountBalance()
        )
    }

    fun emptyState(
        listIsEmpty: Boolean,
        filterIsActive: Boolean
    ): EmptyState? {
        if (!listIsEmpty) return null

        return if (filterIsActive) {
            EmptyState(
                image = R.drawable.ic_empty_search_results,
                caption = R.string.empty_state__no_results__caption,
                message = R.string.empty_state__no_results__message
            )
        } else {
            EmptyState(
                caption = R.string.empty_state__no_transactions__caption,
                message = R.string.empty_state__no_transactions__message
            )
        }
    }

}