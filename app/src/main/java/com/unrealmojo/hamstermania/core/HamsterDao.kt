package com.unrealmojo.hamstermania.core

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.unrealmojo.hamstermania.data.room.HamsterRoom

@Dao
interface HamsterDao {
    @Query("SELECT * FROM hamster_room")
    fun getHamsters(): List<HamsterRoom>

    @Query("DELETE FROM hamster_room")
    fun clearHamsters()

    @Insert
    fun insertHamsters(hamsters: List<HamsterRoom>)
}