package com.example.shishusneh.`data`.dao

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.example.shishusneh.`data`.entities.GrowthRecord
import javax.`annotation`.processing.Generated
import kotlin.Float
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class GrowthDao_Impl(
  __db: RoomDatabase,
) : GrowthDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfGrowthRecord: EntityInsertAdapter<GrowthRecord>

  private val __deleteAdapterOfGrowthRecord: EntityDeleteOrUpdateAdapter<GrowthRecord>
  init {
    this.__db = __db
    this.__insertAdapterOfGrowthRecord = object : EntityInsertAdapter<GrowthRecord>() {
      protected override fun createQuery(): String = "INSERT OR ABORT INTO `growth_records` (`id`,`dateMillis`,`weightKg`,`heightCm`) VALUES (nullif(?, 0),?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: GrowthRecord) {
        statement.bindLong(1, entity.id.toLong())
        statement.bindLong(2, entity.dateMillis)
        statement.bindDouble(3, entity.weightKg.toDouble())
        statement.bindDouble(4, entity.heightCm.toDouble())
      }
    }
    this.__deleteAdapterOfGrowthRecord = object : EntityDeleteOrUpdateAdapter<GrowthRecord>() {
      protected override fun createQuery(): String = "DELETE FROM `growth_records` WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: GrowthRecord) {
        statement.bindLong(1, entity.id.toLong())
      }
    }
  }

  public override suspend fun insertGrowthRecord(record: GrowthRecord): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfGrowthRecord.insert(_connection, record)
  }

  public override suspend fun deleteGrowthRecord(record: GrowthRecord): Unit = performSuspending(__db, false, true) { _connection ->
    __deleteAdapterOfGrowthRecord.handle(_connection, record)
  }

  public override fun getAllGrowthRecords(): Flow<List<GrowthRecord>> {
    val _sql: String = "SELECT * FROM growth_records ORDER BY dateMillis ASC"
    return createFlow(__db, false, arrayOf("growth_records")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfDateMillis: Int = getColumnIndexOrThrow(_stmt, "dateMillis")
        val _columnIndexOfWeightKg: Int = getColumnIndexOrThrow(_stmt, "weightKg")
        val _columnIndexOfHeightCm: Int = getColumnIndexOrThrow(_stmt, "heightCm")
        val _result: MutableList<GrowthRecord> = mutableListOf()
        while (_stmt.step()) {
          val _item: GrowthRecord
          val _tmpId: Int
          _tmpId = _stmt.getLong(_columnIndexOfId).toInt()
          val _tmpDateMillis: Long
          _tmpDateMillis = _stmt.getLong(_columnIndexOfDateMillis)
          val _tmpWeightKg: Float
          _tmpWeightKg = _stmt.getDouble(_columnIndexOfWeightKg).toFloat()
          val _tmpHeightCm: Float
          _tmpHeightCm = _stmt.getDouble(_columnIndexOfHeightCm).toFloat()
          _item = GrowthRecord(_tmpId,_tmpDateMillis,_tmpWeightKg,_tmpHeightCm)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteAll() {
    val _sql: String = "DELETE FROM growth_records"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
