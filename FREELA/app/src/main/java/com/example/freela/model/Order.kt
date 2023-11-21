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
    val photos: List<ByteArray>?,
    val proposals: List<Proposals>?,
    val isAccepted: Boolean
) : Parcelable {
    // ... Restante do código

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readDouble(),
        parcel.readParcelable(User::class.java.classLoader), // Lendo um objeto Parcelable de User
        parcel.readString(),
        parcel.createTypedArrayList(SubCategory.CREATOR), // Lendo uma lista de objetos Parcelable de SubCategory
        mutableListOf<ByteArray>().apply {
            parcel.readList(this, ByteArray::class.java.classLoader) // Lendo uma lista de ByteArray
        },
        parcel.createTypedArrayList(Proposals.CREATOR), // Lendo uma lista de objetos Parcelable de Proposals
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
        parcel.writeList(photos) // Escrevendo uma lista de ByteArray
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
