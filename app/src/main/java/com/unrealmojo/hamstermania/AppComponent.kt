package com.unrealmojo.hamstermania

import com.unrealmojo.hamstermania.ui.activity.HamsterDetailActivity
import com.unrealmojo.hamstermania.ui.activity.HamsterListActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class))
interface AppComponent {
    fun inject(hamsterListActivity: HamsterListActivity)
    fun inject(hamsterListActivity: HamsterDetailActivity)
}