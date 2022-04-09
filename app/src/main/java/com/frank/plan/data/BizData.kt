package com.frank.plan.data

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Room中表结构，单次账单
 */
@Entity
data class Bill(
    @PrimaryKey(autoGenerate = true) val id: Long? = null,
    val type: Int,
    val value: Double,
    val time: LocalDate = LocalDate.now()// android min 26
)

/**
 * 单日账单
 */
data class DayBill(
    val day: LocalDate,
    var totalMoney: Double,
    val bills: MutableList<Bill> = mutableListOf()
)

/**
 * 月度总收入统计（Room中使用）
 */
data class FullInputPerMonth(
    val input: Double
)

/**
 * 页面中账单类型
 */
data class ItemTabUIData(
    val name: String,
    var icon: ImageVector? = null,
    var iconResource: Painter? = null
)

/**
 * 账单添加中单个按钮的数据
 */
data class InputData(
    val content: String,
    val type: Int,
    @DrawableRes val icon: Int? = null,
    val imageVector: ImageVector? = null
)

/**
 * Room中类型的转化
 */
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