package com.spiraclesoftware.androidsample.ui.transactiondetail.cards

import co.zsmb.rainbowcake.test.base.PresenterTest
import com.spiraclesoftware.androidsample.TestData
import com.spiraclesoftware.androidsample.domain.model.*
import com.spiraclesoftware.androidsample.money
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CardsPresenterTest : PresenterTest() {

    @Test
    fun `Correct cards are generated for transaction`() {
        val transaction = Transaction(
            TransactionId(1),
            "Paypal *Steam",
            TestData.epochDateTime,
            money("49.99", "EUR"),
            TransferDirection.OUTGOING,
            TransactionCategory.ENTERTAINMENT,
            TransactionStatus.COMPLETED,
            TransactionStatusCode.SUCCESSFUL,
            "VISA **9400",
            "Note to self"
        )

        val presenter = CardsPresenter()
        val cards = presenter.getCardsFor(transaction)

        assertEquals(4, cards.count())

        assert(cards[0] is ValuePairCard)
        assert(cards[1] is ValuePairCard)
        assert(cards[2] is ValuePairCard)
        assert(cards[3] is NoteCard)

        ((cards[0] as ValuePairCard).valuePairs).let { valuePairs ->
            assert(valuePairs[0] is ValuePairs.Status)
            assert(valuePairs[1] is ValuePairs.PaymentCard)
            assert(valuePairs[2] is ValuePairs.Statement)
        }

        assert((cards[1] as ValuePairCard).valuePairs[0] is ValuePairs.Category)

        assert((cards[2] as ValuePairCard).valuePairs[0] is ValuePairs.Attachments)
    }

}
