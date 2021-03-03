package com.spiraclesoftware.androidsample.feature.transaction_detail.cards.item

import android.view.LayoutInflater
import android.view.ViewGroup
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.databinding.StatusCardItemBinding
import com.spiraclesoftware.androidsample.extension.string
import com.spiraclesoftware.androidsample.extension.stringOrNull
import com.spiraclesoftware.androidsample.feature.transaction_detail.cards.item.model.StatusCardModel

class StatusCardItem(
    model: StatusCardModel
) : ModelBindingCardItem<StatusCardModel, StatusCardItemBinding>(model) {

    override var identifier: Long = R.id.status_card_item.toLong()

    override val type = R.id.status_card_item

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?) =
        StatusCardItemBinding.inflate(inflater, parent, false)

    override fun bindView(binding: StatusCardItemBinding, payloads: List<Any>) {
        val ctx = binding.root.context

        val status = ctx.string(model.statusRes)
        val statusCode = ctx.stringOrNull(model.statusCodeRes)
        binding.bodyText = "$status ∙ $statusCode"
    }

    override fun equals(other: Any?): Boolean {
        if (!super.equals(other)) return false

        other as StatusCardItem
        if (model != other.model) return false
        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + model.hashCode()
        return result
    }

}