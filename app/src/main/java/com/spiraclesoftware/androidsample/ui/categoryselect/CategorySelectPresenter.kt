package com.spiraclesoftware.androidsample.ui.categoryselect

import co.zsmb.rainbowcake.withIOContext
import com.mikepenz.fastadapter.GenericItem
import com.spiraclesoftware.androidsample.data.network.model.TransactionUpdateRequest
import com.spiraclesoftware.androidsample.domain.interactor.TransactionsInteractor
import com.spiraclesoftware.androidsample.domain.model.TransactionCategory
import com.spiraclesoftware.androidsample.domain.model.TransactionId

class CategorySelectPresenter(
    private val transactionsInteractor: TransactionsInteractor
) {

    private val allCategories = TransactionCategory.values().toList()

    fun getListItems(
        selectedCategory: TransactionCategory?
    ): List<GenericItem> {
        return allCategories.map {
            CategoryItem(
                category = it,
                isChecked = it == selectedCategory
            )
        }
    }

    suspend fun updateCategory(id: TransactionId, category: TransactionCategory) = withIOContext {
        val request = TransactionUpdateRequest(category = category)
        transactionsInteractor.updateTransaction(id, request)
    }

}