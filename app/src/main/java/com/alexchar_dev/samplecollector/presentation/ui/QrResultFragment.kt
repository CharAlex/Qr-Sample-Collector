package com.alexchar_dev.samplecollector.presentation.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast

import com.alexchar_dev.samplecollector.R
import kotlinx.android.synthetic.main.activity_qr_scan.*
import kotlinx.android.synthetic.main.fragment_qr_result.*
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val SERIAL = "param1"

class QrResultFragment : Fragment() {
    private var serial: String? = null
    private var result: Int? = null
    private val viewModel: QrResultViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            serial = it.getString(SERIAL)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_qr_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        serialId.text = serial

        resultBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                result = i
                val res = "Result : $i"
                resultView.text = res
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) { }

            override fun onStopTrackingTouch(seekBar: SeekBar) { }
        })

        sendResultBtn.setOnClickListener {
            viewModel.sendResult(serial, result).observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                Toast.makeText(context, "response code ${it.code()}", Toast.LENGTH_SHORT).show()
            })
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String) =
            QrResultFragment().apply {
                arguments = Bundle().apply {
                    putString(SERIAL, param1)
                }
            }
    }
}
