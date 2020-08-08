package com.shostak.cloudinary_vs.utils

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.redmadrobot.inputmask.MaskedTextChangedListener.Companion.installOn
import com.redmadrobot.inputmask.MaskedTextChangedListener.ValueListener
import com.shostak.cloudinary_vs.R
import com.shostak.cloudinary_vs.model.SubTitle


class SubtitlesAdapter :
    ListAdapter<SubTitle, SubtitlesAdapter.ShoppingItemHolder>(
        DiffCallback()
    ) {

    var onDeleteClicked: ((subtitle: SubTitle) -> Unit)? = null
    var onTitleChanged: ((id: Int, text: String, start: String, end: String) -> Unit)? = null
    private var updatePaused: Boolean = false
    var requestFocusOnLastItem: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingItemHolder {
        val inflater = LayoutInflater.from(parent.context)
        val contactView = inflater.inflate(R.layout.timing_item, parent, false)
        return ShoppingItemHolder(contactView)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).id.toLong()
    }

    override fun onBindViewHolder(holder: ShoppingItemHolder, position: Int) {
        val subtitleItem = getItem(position)

        updatePaused = true
        holder.text.setText(subtitleItem.text)
        holder.start.setText(subtitleItem.start_timing)
        holder.end.setText(subtitleItem.end_timing)
        holder.text.setSelection(holder.text.length())
        updatePaused = false

        if (requestFocusOnLastItem && position == currentList.lastIndex) {
            requestFocusOnLastItem = false
            requestFocusAndShowKeyboard(holder.text)
        }
    }

    open inner class ShoppingItemHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {

        val text: EditText = itemView.findViewById(R.id.text)
        val start: EditText = itemView.findViewById(R.id.start)
        val end: EditText = itemView.findViewById(R.id.end)
        val delete: View = itemView.findViewById(R.id.delete)

        override fun onClick(v: View?) {
            when (v) {
                delete -> {
                    if (adapterPosition == RecyclerView.NO_POSITION)
                        return

                    onDeleteClicked?.invoke(getItem(adapterPosition))
                }
            }
        }


        init {
            delete.setOnClickListener(this)

            val textWatcher = object : TextWatcher {
                override fun afterTextChanged(s: Editable) {
                    if (adapterPosition == RecyclerView.NO_POSITION || s.isBlank() || updatePaused)
                        return

                    getItem(adapterPosition).text = text.text.toString()

                    onTitleChanged?.invoke(
                        getItem(adapterPosition).id,
                        text.text.toString(),
                        start.text.toString(),
                        end.text.toString()
                    )

                }

                override fun beforeTextChanged(
                    s: CharSequence, start: Int,
                    count: Int, after: Int
                ) {
                }

                override fun onTextChanged(
                    s: CharSequence, start: Int,
                    before: Int, count: Int
                ) {
                }
            }

            addMaskInputFilterListener(start)
            addMaskInputFilterListener(end)
            text.addTextChangedListener(textWatcher)

        }

        private fun addMaskInputFilterListener(editText: EditText) {
            val listener = installOn(
                editText,
                "[00]{:}[00]{.}[0]",
                object : ValueListener {
                    override fun onTextChanged(
                        maskFilled: Boolean,
                        extractedValue: String,
                        formattedValue: String
                    ) {
                        if (adapterPosition == RecyclerView.NO_POSITION || updatePaused)
                            return

                        getItem(adapterPosition).start_timing = start.text.toString()
                        getItem(adapterPosition).end_timing = end.text.toString()

                        onTitleChanged?.invoke(
                            getItem(adapterPosition).id,
                            text.text.toString(),
                            start.text.toString(),
                            end.text.toString()
                        )
                    }
                }
            )

            editText.hint = listener.placeholder()
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<SubTitle>() {
        override fun areItemsTheSame(
            oldItem: SubTitle,
            newItem: SubTitle
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: SubTitle,
            newItem: SubTitle
        ): Boolean {
            return oldItem == newItem
        }
    }

}