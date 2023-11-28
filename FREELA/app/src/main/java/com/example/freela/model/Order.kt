package com.example.freela.model

import Proposals
import android.os.Parcel
import android.os.Parcelable

data class Order(
    val id: Int,
    val description: String?,
    val title: String?,
    val value: Double,
    val user: User?,
    val deadline: String?,
    val subCategories: List<SubCategory>?,
    val photos: List<Photo>?,
    val proposals: List<Proposals>?,
    val isAccepted: Boolean
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readDouble(),
        parcel.readParcelable(User::class.java.classLoader),
        parcel.readString(),
        parcel.createTypedArrayList(SubCategory.CREATOR),
        mutableListOf<Photo>().apply {
            parcel.readTypedList(this, Photo.CREATOR)
        },
        mutableListOf<Proposals>().apply {
            parcel.readTypedList(this, Proposals.CREATOR)
        },
        parcel.readByte() != 0.toByte()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(description)
        parcel.writeString(title)
        parcel.writeDouble(value)
        parcel.writeParcelable(user, flags) // Escrevendo um objeto Parcelable de User
        parcel.writeString(deadline)
        parcel.writeTypedList(subCategories) // Escrevendo uma lista de objetos Parcelable de SubCategory
        parcel.writeTypedList(photos) // Escrevendo uma lista de objetos Parcelable de Photo
        parcel.writeTypedList(proposals) // Escrevendo uma lista de objetos Parcelable de Proposals
        parcel.writeByte(if (isAccepted) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Order> {
        override fun createFromParcel(parcel: Parcel): Order {
            return Order(parcel)
        }

        override fun newArray(size: Int): Array<Order?> {
            return arrayOfNulls(size)
        }
    }
}
