package com.example.benchmark

import androidx.benchmark.macro.ExperimentalBaselineProfilesApi
import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BaselineProfileGenerator {

    @OptIn(ExperimentalBaselineProfilesApi::class)
    @get:Rule
    val baselineRule = BaselineProfileRule()

    @OptIn(ExperimentalBaselineProfilesApi::class)
    @Test
    fun generateBaselineProfile() = baselineRule.collectBaselineProfile(
        packageName = "com.example.android.roomwordssample"
    ) {
        pressHome()
        startActivityAndWait()
    }
}