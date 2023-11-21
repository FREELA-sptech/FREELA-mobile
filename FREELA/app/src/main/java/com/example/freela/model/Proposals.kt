import android.os.Parcel
import android.os.Parcelable
import com.example.freela.model.User

data class Proposals(
    val id: Int,
    val proposalValue: Double,
    val originUser: User?,
    val description: String,
    val expirationTime: String,
    val destinedOrder: Int,
    val isAccepted: Boolean,
    val isRefused: Boolean
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readDouble(),
        parcel.readParcelable(User::class.java.classLoader),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeDouble(proposalValue)
        parcel.writeParcelable(originUser, flags)
        parcel.writeString(description)
        parcel.writeString(expirationTime)
        parcel.writeInt(destinedOrder)
        parcel.writeByte(if (isAccepted) 1 else 0)
        parcel.writeByte(if (isRefused) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Proposals> {
        override fun createFromParcel(parcel: Parcel): Proposals {
            return Proposals(parcel)
        }

        override fun newArray(size: Int): Array<Proposals?> {
            return arrayOfNulls(size)
        }
    }
}
