package com.developer.ivan.easymadbus.presentation.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.developer.ivan.easymadbus.R
import com.developer.ivan.easymadbus.presentation.models.UIBusStop
import com.developer.ivan.easymadbus.presentation.models.UIStopFavourite

class ConfirmDialog : DialogFragment() {

    private lateinit var mItem: Pair<UIBusStop, UIStopFavourite>
    private var mPosition: Int = 0
    var mListener: OnActionElementsListener? = null

    interface OnActionElementsListener {
        fun onConfirm(item: Pair<UIBusStop, UIStopFavourite>)
        fun onCancel(item: Pair<UIBusStop, UIStopFavourite>, position: Int)
    }

    companion object {

        const val ARG_ITEM = "argItem"
        const val ARG_POSITION = "argPosition"

        fun newInstance(item: Pair<UIBusStop, UIStopFavourite>, position: Int): ConfirmDialog =
            ConfirmDialog().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_ITEM, item)
                    putInt(ARG_POSITION, position)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mItem = arguments?.getSerializable(ARG_ITEM) as Pair<UIBusStop, UIStopFavourite>
        mPosition = arguments?.getInt(ARG_POSITION, 0) ?: 0
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(context)
            .setMessage(context?.getString(R.string.delete_message, mItem.first.name))
            .setPositiveButton(R.string.yes) { _, _ -> mListener?.onConfirm(mItem) }
            .setNegativeButton(R.string.no) { _, _ -> mListener?.onCancel(mItem, mPosition) }
            .setOnDismissListener { mListener?.onCancel(mItem,mPosition) }
            .create()
}