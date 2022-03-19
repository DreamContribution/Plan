package com.frank.plan.data

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.room.*
import com.frank.plan.MainActivity
import kotlinx.coroutines.flow.Flow
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*

@Entity
data class Bill(
    @PrimaryKey(autoGenerate = true) val id: Long? = null,
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

    @Query("SELECT * FROM Bill ORDER BY id DESC")
    fun getAllBill(): Flow<List<Bill>>

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

    companion object {
        @Volatile
        private var INSTANCE: BillDataBase? = null

        fun getDatabase(context: Context): BillDataBase {
            return INSTANCE ?: synchronized(this) {
                val instant = Room.databaseBuilder(
                    context,
                    BillDataBase::class.java,
                    "bill"
                ).build()
                INSTANCE = instant
                return instant
            }
        }
    }
}


/**
 * test of data
 */
suspend fun dataBaseTest(context: Application) {
    val db =
        Room.databaseBuilder(context, BillDataBase::class.java, "bill")
            .build()

    val billDao = db.billDao()

    val itemBill = Bill(value = 20.0, type = 1)
    Log.d(MainActivity.TAG, "onCreate: itemBill:$itemBill")
    billDao.addBill(Bill(value = 20.0, type = 1))
    val allBill = billDao.getAllBill().collect {
        it[0]
    }
    Log.d(MainActivity.TAG, "onCreate: allBill:$allBill")
}