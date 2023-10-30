package com.example.benchmark

import android.widget.Button
import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.StartupTimingMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.UiSelector
import androidx.test.uiautomator.Until
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


/**
 * This is an example startup benchmark.
 *
 * It navigates to the device's home screen, and launches the default activity.
 *
 * Before running this benchmark:
 * 1) switch your app's active build variant in the Studio (affects Studio runs only)
 * 2) add `<profileable android:shell="true" />` to your app's manifest, within the `<application>` tag
 *
 * Run this benchmark from Studio to see startup measurements, and captured system traces
 * for investigating your app's performance.
 */

@RunWith(AndroidJUnit4::class)
class ExampleStartupBenchmark {
    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    @Test
    fun startup() = benchmarkRule.measureRepeated(
        packageName = "com.example.android.roomwordssample",
        metrics = listOf(StartupTimingMetric()),
        iterations = 5,
        startupMode = StartupMode.COLD
    ) {
        pressHome()
        startActivityAndWait()
    }

    @Test
    fun scroll() {
        benchmarkRule.measureRepeated(
            packageName = "com.example.android.roomwordssample",
            iterations = 5,
            metrics = listOf(FrameTimingMetric()),
            startupMode = StartupMode.COLD,
            setupBlock = {
                pressHome()
                startActivityAndWait()
            }
        ) {
            val recyclerView = device.findObject(By.res(packageName, "recycler_view"))
            recyclerView.setGestureMargin(device.displayWidth / 10)
            repeat(2) { recyclerView.fling(Direction.DOWN) }
            repeat(2) { recyclerView.fling(Direction.UP) }
        }
    }

    @Test
    fun navigate() {
        benchmarkRule.measureRepeated(
            packageName = "com.example.android.roomwordssample",
            iterations = 5,
            metrics = listOf(FrameTimingMetric()),
            startupMode = StartupMode.COLD,
            setupBlock = {
                pressHome()
                startActivityAndWait()
            }
        ) {
            // Click the Add Task Button
            val addButton = device.findObject(By.res(packageName, "fab"))
            addButton?.let {
                it.click()
                if (!device.pressBack()) {
                    device.pressBack()
                }
            }

            // Click a Task
            device.wait(Until.hasObject(By.res(packageName, "task_view")), 5000)
            val task = device.findObject(By.res(packageName, "task_view"))
            task?.let {
                it.click()
                // Press back
                if (!device.pressBack()) {
                    device.pressBack()
                }
            }

            // Click the diary
            device.pressMenu()
            device.wait(Until.hasObject(By.text("View diary entries")), 5000)
            val diary = device.findObject(By.text("View diary entries"))
            diary?.let {
                it.click()
                if (!device.pressBack()) {
                    device.pressBack()
                }
            }

            device.waitForIdle()
        }
    }
}