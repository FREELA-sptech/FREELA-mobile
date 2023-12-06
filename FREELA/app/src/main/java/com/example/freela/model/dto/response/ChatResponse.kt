package com.example.freela.model.dto.response

import android.os.Parcel
import android.os.Parcelable
import com.example.freela.model.UserDetails

data class ChatResponse(
    val id: Int,
    val freelancerId: Int,
    val userId: Int,
    val lastUpdate: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeInt(freelancerId)
        parcel.writeInt(userId)
        parcel.writeString(lastUpdate)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ChatResponse> {
        override fun createFromParcel(parcel: Parcel): ChatResponse {
            return ChatResponse(parcel)
        }

        override fun newArray(size: Int): Array<ChatResponse?> {
            return arrayOfNulls(size)
        }
    }
}
