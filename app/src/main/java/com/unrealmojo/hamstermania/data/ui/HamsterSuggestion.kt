package com.unrealmojo.hamstermania.data.ui

import android.os.Parcel
import android.os.Parcelable
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion

class HamsterSuggestion(
        val title: String,
        val description: String,
        val image: String?
) : SearchSuggestion {
    override fun getBody(): String {
        return title
    }

    constructor(hamster: Hamster) : this(
            hamster.title,
            hamster.description,
            hamster.image
    )

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(image)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<HamsterSuggestion> {
        override fun createFromParcel(parcel: Parcel): HamsterSuggestion {
            return HamsterSuggestion(parcel)
        }

        override fun newArray(size: Int): Array<HamsterSuggestion?> {
            return arrayOfNulls(size)
        }
    }
}