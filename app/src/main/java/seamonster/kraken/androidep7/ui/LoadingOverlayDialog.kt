package seamonster.kraken.androidep7.ui

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import seamonster.kraken.androidep7.databinding.DialogOverlayBinding

class LoadingOverlayDialog(context: Context): Dialog(context) {

    private lateinit var binding: DialogOverlayBinding
    var text: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogOverlayBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (!text.isNullOrBlank()) {
            binding.textLoading.text = text
        }
        setCancelable(false)
    }

}