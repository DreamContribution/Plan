package com.frank.plan.data

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Entity
data class Bill(
    @PrimaryKey(autoGenerate = true) val id: Long? = null,
    val type: Int,
    val value: Double,
    val time: LocalDate = LocalDate.now()// android min 26
)

data class DayBill(
    val day: LocalDate,
    var totalMoney: Double,
    val bills: MutableList<Bill> = mutableListOf()
)

data class FullInputPerMonth(
    val input: Double
)

@Entity
data class BillType(
    @PrimaryKey val id: Int,
    val name: String,
    val icon: String,
    val weight: Int
)

class Converters {
    @TypeConverter
    fun fromTimestamp(value: String?): LocalDate? {
        return LocalDate.parse(value, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDate?): String? {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd").format(date)
    }
}


@Dao
interface BillDao {

    @Insert
    fun addBill(vararg bills: Bill)

    @Query("SELECT * FROM Bill ORDER BY id DESC")
    fun getAllBill(): Flow<List<Bill>>

    @Query("SELECT * FROM Bill WHERE time>=:startDate and time <=:endDate ORDER BY time DESC")
    fun getBillsByMonth(startDate: String, endDate: String): Flow<List<Bill>>

    @Query("SELECT SUM(value) AS input FROM Bill WHERE time>=:startDate and time <=:endDate")
    fun getFullInputByMonth(startDate: String, endDate: String): Flow<FullInputPerMonth>

    @Delete
    fun deleteBill(targetType: Bill)

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
fun dataBaseTest(context: Application): Flow<List<Bill>> {
    val db =
        Room.databaseBuilder(context, BillDataBase::class.java, "bill")
            .build()

    val billDao = db.billDao()

    val billsByMonth = billDao.getBillsByMonth("2022-03-01", "2022-03-31")
    Log.d(TAG, "dataBaseTest: $billsByMonth")
    return billsByMonth
}