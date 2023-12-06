package com.example.freela.model

import android.os.Parcel
import android.os.Parcelable

data class Chat(
    val id: Int,
    val freelancerId: UserDetails,
    val userId: UserDetails,
    val lastUpdate: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readParcelable(UserDetails::class.java.classLoader)!!,
        parcel.readParcelable(UserDetails::class.java.classLoader)!!,
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeParcelable(freelancerId, flags)
        parcel.writeParcelable(userId, flags)
        parcel.writeString(lastUpdate)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Chat> {
        override fun createFromParcel(parcel: Parcel): Chat {
            return Chat(parcel)
        }

        override fun newArray(size: Int): Array<Chat?> {
            return arrayOfNulls(size)
        }
    }
}
