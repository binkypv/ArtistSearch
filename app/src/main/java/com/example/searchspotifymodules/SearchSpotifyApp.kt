package com.example.searchspotifymodules

import android.app.Application
import com.example.di.Modules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class SearchSpotifyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@SearchSpotifyApp)
            modules(
                Modules.apiModule,
                Modules.viewModelModule,
                Modules.netModule,
                Modules.repositoryModule
            )
        }
    }

}