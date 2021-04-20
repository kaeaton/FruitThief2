package edu.mills.cs115.fruitthief

import android.app.Application
import android.os.Parcel
import android.os.Parcelable
import timber.log.Timber

class FruitThiefApplication() : Application() {
    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
    }
}