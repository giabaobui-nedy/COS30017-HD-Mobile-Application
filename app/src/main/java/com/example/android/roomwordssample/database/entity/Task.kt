package com.example.android.roomwordssample.database.entity

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task_table")
data class Task (
    @PrimaryKey(autoGenerate = true) val id:Int,
    @ColumnInfo(name = "name") val name:String,
    @ColumnInfo(name = "detail") val detail:String,
    @ColumnInfo(name = "date") val date:String,
    @ColumnInfo(name = "time") val time:String,
    @ColumnInfo(name = "duration") val duration:Int,
    @ColumnInfo(name = "frequency") val frequency:String,
    @ColumnInfo(name = "done") val isDone:Boolean ) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readByte() != 0.toByte()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(detail)
        parcel.writeString(date)
        parcel.writeString(time)
        parcel.writeInt(duration)
        parcel.writeString(frequency)
        parcel.writeBoolean(isDone)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Task> {
        override fun createFromParcel(parcel: Parcel): Task {
            return Task(parcel)
        }

        override fun newArray(size: Int): Array<Task?> {
            return arrayOfNulls(size)
        }
    }
}