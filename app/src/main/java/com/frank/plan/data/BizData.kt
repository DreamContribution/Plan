package com.frank.plan.data

import androidx.room.*
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*

@Entity
data class Bill(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val type: Int,
    val value: Double,
    val time: LocalDateTime = LocalDateTime.now()// android min 26
)

data class DayBill(
    val day: Date,
    val totalMoney: Double,
    val bills: List<Bill>
)

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): LocalDateTime? {
        return value?.let {
            return Instant.ofEpochMilli(value).atZone(ZoneOffset.ofHours(8)).toLocalDateTime()
        }
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?): Long? {
        return date?.toInstant(ZoneOffset.ofHours(8))?.toEpochMilli()

    }
}


@Entity
data class BillType(
    @PrimaryKey val id: Int,
    val name: String,
    val icon: String,
    val weight: Int
)


@Dao
interface BillDao {

    @Insert
    fun addBill(vararg bills: Bill)

    @Query("SELECT * FROM Bill")
    fun getAllBill(): List<Bill>

//    // TODO: SQL
//    fun getBillsByMonth(): List<DayBill>
//
//    @Query("SELECT * FROM BillType")
//    fun getAllBillType(): List<BillType>
//
//    @Insert
//    fun addBillType(vararg types: BillType)
//
//    @Delete
//    fun deleteBillType(targetType: BillType)

}

@Database(entities = [Bill::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class BillDataBase : RoomDatabase() {
    abstract fun billDao(): BillDao
}