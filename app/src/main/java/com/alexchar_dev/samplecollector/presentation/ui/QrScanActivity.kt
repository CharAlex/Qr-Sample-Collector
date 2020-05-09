package com.alexchar_dev.samplecollector.presentation.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.SurfaceHolder
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.util.isNotEmpty
import com.alexchar_dev.samplecollector.R
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import kotlinx.android.synthetic.main.activity_qr_scan.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.lang.Exception

class QrScanActivity : AppCompatActivity() {

    private val camPermissionCode = 1001
    private lateinit var cameraSource: CameraSource //Manages the camera in conjunction with an underlying Detector
    private lateinit var detector: BarcodeDetector
    private val viewModel: QrScanViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_scan)

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            askCamPermission()
        } else { //TODO move permission check before accessing this activity
            setUpControls()
        }

        scanBtn.setOnClickListener {
            viewModel.sendQrGeolocation(serialCode.text.toString(), 40.22, 12.00).observe(this, androidx.lifecycle.Observer {
                Toast.makeText(applicationContext, "response code ${it.code()}", Toast.LENGTH_SHORT).show()
            })
        }
    }

    private fun setUpControls() {
        detector = BarcodeDetector.Builder(this).build()
        cameraSource = CameraSource.Builder(this, detector)
            .setAutoFocusEnabled(true)
            .build()
        camSurface.holder.addCallback(surfaceCallBack)
        detector.setProcessor(processor)
    }

    private fun askCamPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.CAMERA),
            camPermissionCode
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == camPermissionCode && grantResults.isNotEmpty()) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setUpControls()
            } else {
                Toast.makeText(applicationContext, "Please grant camera permission", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val surfaceCallBack = object :  SurfaceHolder.Callback {
        override fun surfaceChanged(p0: SurfaceHolder?, p1: Int, p2: Int, p3: Int) { }

        override fun surfaceDestroyed(p0: SurfaceHolder?) {
            cameraSource.stop()
        }

        override fun surfaceCreated(p0: SurfaceHolder?) {
            try {
                cameraSource.start(p0)
            } catch (e: Exception) {
                TODO()
            }
        }
    }

    //Post-processing action to be executed for each detection
    private val processor = object :Detector.Processor<Barcode> {
        override fun release() { }

        override fun receiveDetections(p0: Detector.Detections<Barcode>?) {
            if(p0 != null && p0.detectedItems.isNotEmpty()) {
                val qrCodes = p0.detectedItems
                val code = qrCodes.valueAt(0)
                serialCode.text = code.displayValue
            }
        }
    }
}
