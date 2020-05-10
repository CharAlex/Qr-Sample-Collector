package com.alexchar_dev.samplecollector.presentation.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.alexchar_dev.samplecollector.R
import com.alexchar_dev.samplecollector.data.models.LoginRequest
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private val camPermissionCode = 1001
    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        logInBtn.setOnClickListener {
            viewModel.login(LoginRequest("test@test.test", "testcandpass")).observe(this, androidx.lifecycle.Observer {
                accessTkn.text = it.access
                Toast.makeText(applicationContext, "log in success", Toast.LENGTH_SHORT).show()
                askCamPermission()
            })
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == camPermissionCode && grantResults.isNotEmpty()) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startQrCodeActivity()
            } else {
                Toast.makeText(applicationContext, "Please grant camera permission", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun askCamPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.CAMERA),
            camPermissionCode
        )
    }
    private fun startQrCodeActivity() {
        startActivity(Intent(this, QrScanActivity::class.java))
    }
}
