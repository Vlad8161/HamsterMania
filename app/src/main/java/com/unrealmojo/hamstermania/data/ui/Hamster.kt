package com.unrealmojo.hamstermania.data.ui

import android.content.Context
import com.unrealmojo.hamstermania.data.room.HamsterRoom
import com.unrealmojo.hamstermania.data.network.HamsterNetwork

data class Hamster(
        val title: String,
        val description: String,
        val image: String?
) {
    constructor(hamsterNetwork: HamsterNetwork, context: Context): this(
            hamsterNetwork.title ?: context.getString(R.string.hamster_title_default),
            hamsterNetwork.description ?: context.getString(R.string.hamster_description_default),
            hamsterNetwork.image
    )

    constructor(hamsterRoom: HamsterRoom) : this(
            hamsterRoom.title,
            hamsterRoom.description,
            hamsterRoom.image
    )

    constructor(hamsterSuggestion: HamsterSuggestion) : this(
            hamsterSuggestion.title,
            hamsterSuggestion.description,
            hamsterSuggestion.image
    )
}