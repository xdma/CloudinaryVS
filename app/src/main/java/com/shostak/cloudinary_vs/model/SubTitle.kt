package com.shostak.cloudinary_vs.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SubTitle(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "text") var text: String = "",
    @ColumnInfo(name = "start_timing") var start_timing: String = "",
    @ColumnInfo(name = "end_timing") var end_timing: String = "",
    @ColumnInfo(name = "publicid") var public_id: String = "",
    @ColumnInfo(name = "raw_position") var position: Int = 0
) {

    override fun equals(other: Any?): Boolean {
        return (other is SubTitle && this.text == other.text && this.start_timing == other.start_timing && this.end_timing == other.end_timing && this.public_id == other.public_id && other.position == this.position)
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + text.hashCode()
        result = 31 * result + start_timing.hashCode()
        result = 31 * result + end_timing.hashCode()
        result = 31 * result + public_id.hashCode()
        result = 31 * result + position
        return result
    }


}