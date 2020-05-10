package com.alexchar_dev.samplecollector.presentation.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.SurfaceHolder
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.util.isNotEmpty
import com.alexchar_dev.samplecollector.R
import com.alexchar_dev.samplecollector.presentation.utils.attachFragment
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import kotlinx.android.synthetic.main.activity_qr_scan.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.round


class QrScanActivity : AppCompatActivity() {

    private val camPermissionCode = 1001
    private val locPermissionCode = 1002
    private lateinit var cameraSource: CameraSource //Manages the camera in conjunction with an underlying Detector
    private lateinit var detector: BarcodeDetector
    var mFusedLocationClient: FusedLocationProviderClient? = null
    private val viewModel: QrScanViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_scan)

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            askCamPermission()
        } else {
            setUpControls()
        }

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            askLocationPermission()
        } else {
            getLocation()
        }

        scanBtn.setOnClickListener {
            val (x,y) = getFormattedLoc()
            viewModel.sendQrGeolocation(serialCode.text.toString(), x, y).observe(this, androidx.lifecycle.Observer {
                Toast.makeText(applicationContext, "response code ${it.code()}", Toast.LENGTH_SHORT).show()
            })
        }

        sendResult.setOnClickListener {
            supportFragmentManager.beginTransaction().add(R.id.fragment_container, QrResultFragment.newInstance(serialCode.text.toString())).commit()
        }
    }

    private fun getFormattedLoc(): Pair<Double,Double> {
        return Pair(latLoc.text.toString().toDouble().round(), longLoc.text.toString().toDouble().round())
    }

    private fun Double.round(decimals: Int = 6): Double {
        var multiplier = 1.0
        repeat(decimals) { multiplier *= 10 }
        return round(this * multiplier) / multiplier
    }

    private fun getLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient?.lastLocation?.addOnCompleteListener { task ->
                    val location: Location? = task.getResult()
                    if (location == null) {
                        requestNewLocationData()
                    } else {
                        latLoc.text = location.latitude.toString()
                        longLoc.text = location.longitude.toString()
                    }
                }
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            askLocationPermission()
        }
    }

    private fun requestNewLocationData() {
        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        (mFusedLocationClient as FusedLocationProviderClient).requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }

    private val mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation: Location = locationResult.lastLocation
            latLoc.text = mLastLocation.latitude.toString()
            longLoc.text = mLastLocation.longitude.toString()
        }
    }

    private fun checkPermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
    }

    private fun askLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            locPermissionCode
        )
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
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
                getLocation()
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
