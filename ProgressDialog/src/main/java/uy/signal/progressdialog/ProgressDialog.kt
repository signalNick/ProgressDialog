package uy.signal.progressdialog

import android.app.Dialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.RotateDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import uy.signal.progressdialog.databinding.ProgressDialogBinding
import java.lang.IllegalStateException
import kotlin.concurrent.thread

class ProgressDialog private constructor(builder: Builder): DialogFragment() {

    private lateinit var binding : ProgressDialogBinding

    private var icon: Drawable?
    private var tint: ColorStateList?
    private var backgroundColor: Int?
    private var customText: String?
    private var dismissible: Boolean

    init {
        this.icon = builder.icon
        this.tint = builder.tint
        this.customText = builder.customText
        this.backgroundColor = builder.backgroundColor
        this.dismissible = builder.dismissible
    }

    class Builder{
        var icon: Drawable? = null
            private set
        var tint: ColorStateList? = null
            private set
        var customText: String? = null
            private set
        var backgroundColor: Int? = null
            private set
        var dismissible: Boolean = false
            private set

        fun icon(icon: Drawable?) = apply { this.icon = icon }
        fun tint(tint: ColorStateList?) = apply { this.tint = tint }
        fun customText(text: String?) = apply { this.customText = text }
        fun cardBackgroundColor(color: Int?) = apply { this.backgroundColor = color}
        fun dismissible(flag: Boolean) = apply { this.dismissible = flag }
        fun build() = ProgressDialog(this)

    }


    @Volatile private var working: Boolean = false
    @Volatile private var state: Int = 0

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val d = Dialog(requireContext())
        d.window?.setBackgroundDrawableResource(android.R.color.transparent)
        d.setCanceledOnTouchOutside(dismissible)
        return d
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ProgressDialogBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val defaultText: String? = context?.getString(R.string.wait_text)

        icon?.let {
            val roatatingIcon = RotateDrawable()
            roatatingIcon.drawable = icon
            binding.pBar.indeterminateDrawable = roatatingIcon
        } ?: run {
            tint?.let {
                binding.pBar.indeterminateTintList
            }
        }
        backgroundColor?.let {
            binding.root.setCardBackgroundColor(it)
        }
        customText ?: run {
            customText = defaultText
        }

        binding.textView.text = customText

        working = true
        thread {
            while (working) {
                try {
                    requireActivity().runOnUiThread {
                        when (state) {
                            0 -> {
                                binding.dot1.visibility = View.VISIBLE
                                binding.dot2.visibility = View.INVISIBLE
                                binding.dot3.visibility = View.INVISIBLE
                            }
                            1 -> {
                                binding.dot1.visibility = View.VISIBLE
                                binding.dot2.visibility = View.VISIBLE
                                binding.dot3.visibility = View.INVISIBLE
                            }
                            2 -> {
                                binding.dot1.visibility = View.VISIBLE
                                binding.dot2.visibility = View.VISIBLE
                                binding.dot3.visibility = View.VISIBLE
                            }
                            else -> {
                                binding.dot1.visibility = View.INVISIBLE
                                binding.dot2.visibility = View.INVISIBLE
                                binding.dot3.visibility = View.INVISIBLE
                            }
                        }
                    }
                } catch (e: IllegalStateException){
                    working = false
                }

                state++
                if (state == 3){
                    state = 0
                }

                try {
                    Thread.sleep(500)
                } catch (e: InterruptedException){
                    e.printStackTrace()
                }
            }
        }
    }

    override fun dismiss() {
        working = false
        super.dismiss()
    }



}