package com.spiraclesoftware.androidsample.feature.transaction_detail

import android.graphics.Color
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.graphics.ColorUtils
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import co.zsmb.rainbowcake.base.OneShotEvent
import coil.load
import com.google.android.material.snackbar.Snackbar
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.GenericFastAdapter
import com.mikepenz.fastadapter.adapters.GenericModelAdapter
import com.mikepenz.fastadapter.adapters.ModelAdapter
import com.mikepenz.fastadapter.diff.FastAdapterDiffUtil
import com.mikepenz.fastadapter.listeners.addClickListener
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.component.image_picker.ImagePicker
import com.spiraclesoftware.androidsample.databinding.TransactionDetailFragmentBinding
import com.spiraclesoftware.androidsample.domain.entity.TransactionId
import com.spiraclesoftware.androidsample.extension.*
import com.spiraclesoftware.androidsample.feature.text_input.TextInputFragment
import com.spiraclesoftware.androidsample.feature.transaction_detail.TransactionDetailViewModel.*
import com.spiraclesoftware.androidsample.feature.transaction_detail.TransactionDetailViewState.Content
import com.spiraclesoftware.androidsample.feature.transaction_detail.TransactionDetailViewState.Error
import com.spiraclesoftware.androidsample.feature.transaction_detail.cards.item.*
import com.spiraclesoftware.androidsample.feature.transaction_detail.cards.item.model.*
import com.spiraclesoftware.androidsample.framework.Model
import com.spiraclesoftware.androidsample.framework.StandardFragment
import com.spiraclesoftware.androidsample.util.DelightUI
import com.stfalcon.imageviewer.StfalconImageViewer
import io.cabriole.decorator.LinearMarginDecoration
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf

class TransactionDetailFragment :
    StandardFragment<TransactionDetailFragmentBinding, TransactionDetailViewState, TransactionDetailViewModel>() {

    override fun provideViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        TransactionDetailFragmentBinding.inflate(inflater, container, false)

    override fun provideViewModel(): TransactionDetailViewModel {
        TransactionDetailFragmentArgs.fromBundle(requireArguments()).let { args ->
            val id = TransactionId(args.transactionId)
            return getViewModel { parametersOf(id) }
        }
    }

    companion object {
        const val NOTE_INPUT_REQUEST_KEY = "noteInputRequest"
    }

    private lateinit var imagePicker: ImagePicker
    private lateinit var fastAdapter: GenericFastAdapter
    private lateinit var itemAdapter: GenericModelAdapter<Model>

    private fun onAddAttachmentClicked() {
        viewModel.onAddAttachment()
    }

    private fun onNoteChanged(note: String?) {
        viewModel.onNoteChanged(note.orEmpty())
    }
    
    override fun render(viewState: TransactionDetailViewState): Unit = with(binding) {
        renderErrorLayout(viewState)

        when (viewState) {
            is Content -> {
                FastAdapterDiffUtil[itemAdapter] = viewState.detailModel.cardModels

                with(viewState.detailModel) {
                    toolbar.title = name
                    nameView.text = name
                    dateView.text = processingDate
                    renderAmountText(formattedMoney, contributesToBalance)
                    renderCategoryIcon(iconRes, iconTintRes)
                }
            }
        }
    }

    private fun renderAmountText(formattedMoney: String, contributesToBalance: Boolean) = with(binding) {
        amountView.text = formattedMoney
        if (!contributesToBalance) {
            amountView.addPaintFlag(Paint.STRIKE_THRU_TEXT_FLAG)
        }
    }

    private fun renderCategoryIcon(iconRes: Int, iconTintRes: Int) = with(binding) {
        val tint: Int = color(iconTintRes)
        iconView.setImageDrawable(tintedDrawable(iconRes, tint))

        val fadedTint = ColorUtils.setAlphaComponent(tint, 255 / 100 * 15)
        iconView.background = tintedDrawable(R.drawable.shp_circle, fadedTint)
    }

    private fun renderErrorLayout(viewState: TransactionDetailViewState): Unit = with(binding) {
        errorMessageView.isVisible = viewState is Error

        if (viewState is Error) {
            errorMessageView.text = viewState.message
        }
    }

    override fun onEvent(event: OneShotEvent) {
        when (event) {
            is NavigateEvent -> {
                findNavController().navigate(event.navDirections)
            }
            is NavigateToCardDetailEvent -> {
                showToast(R.string.not_implemented, Toast.LENGTH_SHORT)
            }
            is DownloadStatementEvent -> {
                showToast(R.string.not_implemented, Toast.LENGTH_SHORT)
            }
            is OpenAttachmentPickerEvent -> {
                openAttachmentPicker(viewModel::onAttachmentPicked)
            }
            is OpenAttachmentViewerEvent -> {
                openAttachmentViewer(event.images, event.startPosition)
            }
            is NotifyAttachmentsLimitReachedEvent -> {
                showSnackbar(R.string.transaction_detail__attachments__error__limit_reached, Snackbar.LENGTH_LONG)
            }
            is NotifyOfFailureEvent -> {
                showToast(event.stringRes, Toast.LENGTH_LONG)
            }
        }
    }

    private fun openAttachmentPicker(onAttachmentPicked: (Uri) -> Unit) {
        imagePicker.showDialog(requireContext()) { imageUri ->
            onAttachmentPicked(imageUri)
        }
    }

    private fun openAttachmentViewer(images: List<Uri>, startPosition: Int) {
        StfalconImageViewer.Builder(context, images) { view, uri ->
            view.load(uri) {
                error(tintedDrawable(R.drawable.ic_image_error, Color.WHITE))
            }
        }
            .withStartPosition(startPosition)
            .withHiddenStatusBar(false)
            .show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setFragmentResultListener(NOTE_INPUT_REQUEST_KEY) { _, bundle ->
            val note = bundle.getString(TextInputFragment.RESULT_KEY)
            onNoteChanged(note)
        }

        imagePicker = ImagePicker().also {
            it.registerResultListeners(this)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setupFastItemAdapter()
        setupRecyclerView()
    }

    override fun onDestroyView() = with(binding) {
        recyclerView.adapter = null
        super.onDestroyView()
    }

    private fun setupToolbar() = with(binding) {
        toolbar.setupWithNavController(findNavController())
        DelightUI.setupToolbarTitleAppearingOnScroll(toolbar, scrollView) {
            toolbar.height + nameView.height
        }
    }

    private fun setupFastItemAdapter() {
        itemAdapter = ModelAdapter.models { model: Model ->
            when (model) {
                is ValuePairCardModel -> ValuePairCardItem(model, viewModel)
                is StatusCardModel -> StatusCardItem(model)
                is CategoryCardModel -> CategoryCardItem(model, viewModel)
                is AttachmentsCardModel -> AttachmentsCardItem(model, viewModel)
                is NoteCardModel -> NoteCardItem(model, viewModel)
                else -> throw IllegalStateException()
            }
        }
        fastAdapter = FastAdapter.with(itemAdapter).apply {
            // Prevent clicking on the CardView of each item
            attachDefaultListeners = false
            setHasStableIds(true)

            addClickListener({ vh: AttachmentsCardItem.ViewHolder -> vh.binding.actionView }) { _, _, _, _ ->
                onAddAttachmentClicked()
            }
        }
    }

    private fun setupRecyclerView() = with(binding) {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = fastAdapter
            itemAnimator = null
            addItemDecoration(
                LinearMarginDecoration.createVertical(
                    verticalMargin = dpToPx(16)
                )
            )
        }
    }

}