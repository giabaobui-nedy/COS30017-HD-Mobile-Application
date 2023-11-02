package com.example.baselineprofile

import androidx.benchmark.macro.BaselineProfileMode
import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.StartupTimingMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.Until
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class StartupBenchmarks {

    @get:Rule
    val rule = MacrobenchmarkRule()

    @Test
    fun startupCompilationNone() =
            start(CompilationMode.None())
    @Test
    fun startupCompilationBaselineProfiles() =
            start(CompilationMode.Partial(BaselineProfileMode.Require))

    @Test
    fun scrollCompilationNone() =
            scroll(CompilationMode.None())
    @Test
    fun scrollCompilationBaselineProfiles() =
            scroll(CompilationMode.Partial(BaselineProfileMode.Require))

    @Test
    fun navigateCompilationNone() =
            navigate(CompilationMode.None())
    @Test
    fun navigateCompilationBaselineProfiles() =
            navigate(CompilationMode.Partial(BaselineProfileMode.Require))

    private fun start(compilationMode: CompilationMode) {
        rule.measureRepeated(
            packageName = "com.example.android.roomwordssample",
            metrics = listOf(StartupTimingMetric()),
            compilationMode = compilationMode,
            startupMode = StartupMode.COLD,
            iterations = 10,
            setupBlock = {
                pressHome()
            },
            measureBlock = {
                startActivityAndWait()
            }
        )
    }

    private fun scroll(compilationMode: CompilationMode) {
        rule.measureRepeated(
            packageName = "com.example.android.roomwordssample",
            metrics = listOf(StartupTimingMetric(), FrameTimingMetric()),
            compilationMode = compilationMode,
            startupMode = StartupMode.COLD,
            iterations = 10,
            setupBlock = {
                pressHome()
            },
            measureBlock = {
                startActivityAndWait()
                addTasksAndScroll()
            }
        )
    }

    private fun navigate(compilationMode: CompilationMode) {
        rule.measureRepeated(
            packageName = "com.example.android.roomwordssample",
            metrics = listOf(StartupTimingMetric(), FrameTimingMetric()),
            compilationMode = compilationMode,
            startupMode = StartupMode.COLD,
            iterations = 10,
            setupBlock = {
                pressHome()
            },
            measureBlock = {
                startActivityAndWait()
                addTasksAndNavigate()
            }
        )
    }
}

fun MacrobenchmarkScope.startup() {
    pressHome()
    startActivityAndWait()
}

fun MacrobenchmarkScope.scroll() {

    if (device.hasObject(By.res(packageName, "recycler_view"))) {
        val recyclerView = device.findObject(By.res(packageName, "recycler_view"))
        recyclerView?.let {
            it.setGestureMargin(device.displayWidth / 15)
            if (it.fling(Direction.DOWN)) { it.fling(Direction.DOWN) }
            if (it.fling(Direction.UP)) {it.fling(Direction.UP)}
        }
    } else {
        device.wait(Until.hasObject(By.res(packageName, "recycler_view")), 5000)
        val recyclerView = device.findObject(By.res(packageName, "recycler_view"))
        recyclerView?.let {
            it.setGestureMargin(device.displayWidth / 15)
            if (it.fling(Direction.DOWN)) { it.fling(Direction.DOWN) }
            if (it.fling(Direction.UP)) {it.fling(Direction.UP)}
        }
    }
}

fun MacrobenchmarkScope.navigate() {

    // Click a Task
    if (device.hasObject(By.res(packageName, "task_view"))) {
        val task = device.findObject(By.res(packageName, "task_view"))
        task?.let {
            it.click()
            // Press back
            if (!device.pressBack()) {
                device.pressBack()
            }
        }
    } else {
        device.wait(Until.hasObject(By.res(packageName, "task_view")), 5000)
        val task = device.findObject(By.res(packageName, "task_view"))
        task?.let {
            it.click()
            // Press back
            if (!device.pressBack()) {
                device.pressBack()
            }
        }
    }

    // Click the diary
    device.pressMenu()
    if (device.hasObject(By.text("View diary entries"))) {
        val diary = device.findObject(By.text("View diary entries"))
        diary?.let {
            it.click()
            if (!device.pressBack()) {
                device.pressBack()
            }
        }
    } else {
        device.wait(Until.hasObject(By.text("View diary entries")), 5000)
        val diary = device.findObject(By.text("View diary entries"))
        diary?.let {
            it.click()
            if (!device.pressBack()) {
                device.pressBack()
            }
        }
    }
}

fun MacrobenchmarkScope.addTasks() {

    if (device.hasObject(By.res(packageName, "fab"))) {
        val addButton = device.findObject(By.res(packageName, "fab"))
        addButton?.let {
            addButton.click()
        }
    } else {
        device.wait(Until.hasObject(By.res(packageName, "add_random_task")), 5000)
        val addButton = device.findObject(By.res(packageName, "fab"))
        addButton?.let {
            addButton.click()
        }
    }


    if (device.hasObject(By.res(packageName, "add_random_task"))) {
        val addRandomTaskButton = device.findObject(By.res(packageName, "add_random_task"))
        addRandomTaskButton?.let {
            addRandomTaskButton.click()
        }
    } else {
        device.wait(Until.hasObject(By.res(packageName, "add_random_task")), 5000)
        val addRandomTaskButton = device.findObject(By.res(packageName, "add_random_task"))
        addRandomTaskButton?.let {
            addRandomTaskButton.click()
        }
    }

}

fun MacrobenchmarkScope.setUp() {
    startup()
    repeat(10) {
        addTasks()
    }
}

fun MacrobenchmarkScope.addTasksAndScroll() {
    addTasks()
    scroll()
}

fun MacrobenchmarkScope.addTasksAndNavigate() {
    addTasks()
    navigate()
}