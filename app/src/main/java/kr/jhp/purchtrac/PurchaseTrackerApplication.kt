package kr.jhp.purchtrac

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PurchaseTrackerApplication : Application() {
    override fun onCreate() {
        super.onCreate()

    }
}