package com.spiraclesoftware.androidsample

import com.spiraclesoftware.androidsample.domain.model.*
import org.threeten.bp.ZonedDateTime
import java.util.*

object TestData {

    val epochDateTime = ZonedDateTime.parse("1970-01-01T00:00:00+00:00")!!

    val transactions: List<Transaction> = arrayListOf(
        Transaction(
            TransactionId(1),
            "Paypal *Steam",
            epochDateTime,
            money("49.99", "EUR"),
            TransferDirection.OUTGOING,
            TransactionCategory.ENTERTAINMENT,
            TransactionStatus.COMPLETED,
            TransactionStatusCode.SUCCESSFUL,
            emptyList(),
            "VISA **9400",
            "Half-Life: Alyx"
        ),
        Transaction(
            TransactionId(2),
            "Salary",
            ZonedDateTime.parse("2019-05-14T09:00:00+00:00"),
            money("1000", "EUR"),
            TransferDirection.INCOMING,
            TransactionCategory.TRANSFERS,
            TransactionStatus.COMPLETED,
            TransactionStatusCode.SUCCESSFUL
        ),
        Transaction(
            TransactionId(3),
            "Groceries",
            ZonedDateTime.parse("2019-05-14T09:00:00+00:00"),
            money("14.99", "USD"),
            TransferDirection.OUTGOING,
            TransactionCategory.GROCERIES,
            TransactionStatus.DECLINED,
            TransactionStatusCode.SPENDING_LIMIT_EXCEEDED
        )
    )

}