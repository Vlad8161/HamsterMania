package com.unrealmojo.hamstermania.core

import com.unrealmojo.hamstermania.data.ui.Hamster
import com.unrealmojo.hamstermania.data.room.HamsterRoom

class HamsterStorage(private val mHamsterDatabase: HamsterDatabase) {
    fun getHamsters(): List<Hamster> {
        return mHamsterDatabase.getHamsterDao()
                .getHamsters()
                .map { Hamster(it) }
    }

    fun updateHamsters(hamsters: List<Hamster>) {
        mHamsterDatabase.getHamsterDao().clearHamsters()
        mHamsterDatabase.getHamsterDao().insertHamsters(hamsters.map { HamsterRoom(it) })
    }
}
