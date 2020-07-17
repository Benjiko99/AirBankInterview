package com.spiraclesoftware.androidsample.ui.transactionlist

import android.view.LayoutInflater
import android.view.ViewGroup
import com.mikepenz.fastadapter.binding.AbstractBindingItem
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.databinding.TransactionListHeaderItemBinding
import com.spiraclesoftware.androidsample.domain.model.Money
import com.spiraclesoftware.androidsample.ui.shared.DateTimeFormat
import com.spiraclesoftware.androidsample.ui.shared.MoneyFormat
import org.threeten.bp.ZonedDateTime

class HeaderItem(
    private val dateTime: ZonedDateTime,
    private val contributionToBalance: Money
) : AbstractBindingItem<TransactionListHeaderItemBinding>() {

    override val type: Int
        get() = R.id.transaction__list__header_item

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): TransactionListHeaderItemBinding {
        return TransactionListHeaderItemBinding.inflate(inflater, parent, false)
    }

    override fun bindView(binding: TransactionListHeaderItemBinding, payloads: List<Any>) {
        val formattedContribution = MoneyFormat(contributionToBalance)
            .formatSigned(showSignWhenPositive = false)

        binding.contributionsText = formattedContribution
        binding.dateText = dateTime.format(DateTimeFormat.PRETTY_DATE)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as HeaderItem

        if (dateTime != other.dateTime) return false
        if (contributionToBalance != other.contributionToBalance) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + dateTime.hashCode()
        result = 31 * result + contributionToBalance.hashCode()
        return result
    }

}
