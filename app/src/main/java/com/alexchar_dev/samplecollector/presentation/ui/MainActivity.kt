package com.alexchar_dev.samplecollector.presentation.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.alexchar_dev.samplecollector.R
import com.alexchar_dev.samplecollector.data.models.LoginRequest
import com.alexchar_dev.samplecollector.data.network.GwfApiService
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        logInBtn.setOnClickListener {
            viewModel.login(LoginRequest("test@test.test", "testcandpass")).observe(this, androidx.lifecycle.Observer {
                accessTkn.text = it.access
                Toast.makeText(applicationContext, "log in success", Toast.LENGTH_SHORT).show()
            })
        }

    }
}
