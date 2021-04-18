package com.spiraclesoftware.androidsample.feature.transaction_detail

import android.net.Uri
import androidx.navigation.NavDirections
import co.zsmb.rainbowcake.base.OneShotEvent
import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.domain.Result
import com.spiraclesoftware.androidsample.domain.entity.Transaction
import com.spiraclesoftware.androidsample.domain.entity.TransactionId
import com.spiraclesoftware.androidsample.feature.text_input.TextInputType
import com.spiraclesoftware.androidsample.feature.transaction_detail.TransactionDetailFragment.Companion.NOTE_INPUT_REQUEST_KEY
import com.spiraclesoftware.androidsample.feature.transaction_detail.TransactionDetailFragmentDirections.Companion.toCategorySelect
import com.spiraclesoftware.androidsample.feature.transaction_detail.TransactionDetailFragmentDirections.Companion.toTextInput
import com.spiraclesoftware.androidsample.feature.transaction_detail.TransactionDetailViewState.*
import com.spiraclesoftware.androidsample.feature.transaction_detail.cards.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import timber.log.Timber

class TransactionDetailViewModel(
    private val transactionId: TransactionId,
    private val detailPresenter: TransactionDetailPresenter
) : RainbowCakeViewModel<TransactionDetailViewState>(Initial) {

    data class NavigateEvent(val navDirections: NavDirections) : OneShotEvent

    object NavigateToCardDetailEvent : OneShotEvent

    object DownloadStatementEvent : OneShotEvent

    object OpenAttachmentPickerEvent : OneShotEvent

    data class OpenAttachmentViewerEvent(
        val images: List<Uri>,
        val startPosition: Int
    ) : OneShotEvent

    object NotifyAttachmentsLimitReachedEvent : OneShotEvent

    data class NotifyOfFailureEvent(val stringRes: Int) : OneShotEvent

    private val attachmentUploads = MutableStateFlow<List<Uri>>(emptyList())

    init {
        produceViewStateFromDataFlow()
    }

    private fun produceViewStateFromDataFlow() = executeNonBlocking {
        detailPresenter.flowDetailModel(transactionId, attachmentUploads)
            .collect { result ->
                viewState = when (result) {
                    is Result.Loading -> Initial
                    is Result.Success -> Content(result.data)
                    is Result.Error -> Error(result.exception.message)
                    else -> throw IllegalStateException()
                }
            }
    }

    fun openCardDetail() {
        postEvent(NavigateToCardDetailEvent)
    }

    fun downloadStatement() {
        postEvent(DownloadStatementEvent)
    }

    fun selectCategory() = execute {
        val navDirections = toCategorySelect(
            transactionId.value,
            initialCategory = detailPresenter.getCategory()
        )
        postEvent(NavigateEvent(navDirections))
    }

    fun viewAttachment(uri: Uri) = execute {
        val images = detailPresenter.getAttachments()
        val startPosition = images.indexOf(uri)
        postEvent(OpenAttachmentViewerEvent(images, startPosition))
    }

    fun addAttachment() = execute {
        val attachments = detailPresenter.getAttachments()
        val totalCount = attachments.size + attachmentUploads.value.size

        if (totalCount >= Transaction.MAX_ATTACHMENTS) {
            postEvent(NotifyAttachmentsLimitReachedEvent)
            return@execute
        }
        postEvent(OpenAttachmentPickerEvent)
    }

    fun removeAttachment(uri: Uri) = execute {
        detailPresenter.removeAttachment(uri)
    }

    fun cancelUpload(uri: Uri) {
        // Not yet implemented
    }

    fun openNoteInput() = execute {
        val navDirections = toTextInput(
            TextInputType.NOTE,
            NOTE_INPUT_REQUEST_KEY,
            initialValue = detailPresenter.getNote()
        )

        postEvent(NavigateEvent(navDirections))
    }

    fun onAttachmentPicked(imageUri: Uri) = executeNonBlocking {
        attachmentUploads.value = attachmentUploads.value.plus(imageUri)

        try {
            delay(3000) // Simulate network delay
            detailPresenter.uploadAttachment(imageUri)
            attachmentUploads.value = attachmentUploads.value.minus(imageUri)
        } catch (e: Exception) {
            Timber.e(e)
            postEvent(NotifyOfFailureEvent(R.string.unknown_error))
        }
    }

    fun onNoteChanged(note: String) = executeNonBlocking {
        try {
            detailPresenter.updateNote(note)
        } catch (e: Exception) {
            Timber.e(e)
            postEvent(NotifyOfFailureEvent(R.string.unknown_error))
        }
    }

}
