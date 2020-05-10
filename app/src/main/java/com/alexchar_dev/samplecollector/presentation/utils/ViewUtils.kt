package com.alexchar_dev.samplecollector.presentation.utils

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

fun Activity.attachFragment(manager: FragmentManager, containerId: Int, view: Fragment, tag: String) {
    manager.beginTransaction()
        .replace(containerId, view, tag)
        .commitNowAllowingStateLoss()
}