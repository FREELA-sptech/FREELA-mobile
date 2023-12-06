package com.example.freela.model

import android.os.Parcel
import android.os.Parcelable
import com.example.freela.model.SubCategory

data class User(
    val id: Int,
    val name: String,
    val profilePhoto: ByteArray,
    val rate: Double,
    val uf: String,
    val city: String,
    val isFreelancer: Boolean,
    val description: String?,
    val subCategories: List<SubCategory>
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.createByteArray() ?: byteArrayOf(),
        parcel.readDouble(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readByte() != 0.toByte(),
        parcel.readString(),
        parcel.createTypedArrayList(SubCategory.CREATOR) ?: listOf()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeByteArray(profilePhoto)
        parcel.writeDouble(rate)
        parcel.writeString(uf)
        parcel.writeString(city)
        parcel.writeByte(if (isFreelancer) 1 else 0)
        parcel.writeString(description)
        parcel.writeTypedList(subCategories)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}
