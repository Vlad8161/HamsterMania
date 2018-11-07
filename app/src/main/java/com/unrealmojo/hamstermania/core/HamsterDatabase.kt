package com.unrealmojo.hamstermania.core

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.unrealmojo.hamstermania.data.room.HamsterRoom

@Database(entities = arrayOf(HamsterRoom::class), version = 1)
abstract class HamsterDatabase: RoomDatabase() {
    abstract fun getHamsterDao(): HamsterDao
}