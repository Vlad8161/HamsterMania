package com.unrealmojo.hamstermania.data.room

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.unrealmojo.hamstermania.data.ui.Hamster

@Entity(tableName = "hamster_room")
data class HamsterRoom(
        @PrimaryKey(autoGenerate = true) var id: Long? = null,
        @ColumnInfo(name = "title") val title: String,
        @ColumnInfo(name = "description") val description: String,
        @ColumnInfo(name = "image") val image: String?
) {
    constructor(hamster: Hamster) : this(
            title = hamster.title,
            description = hamster.description,
            image = hamster.image
    )
}