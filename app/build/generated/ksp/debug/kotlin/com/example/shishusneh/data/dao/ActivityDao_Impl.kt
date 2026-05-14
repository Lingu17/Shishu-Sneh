package com.example.shishusneh.`data`.dao

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.example.shishusneh.`data`.entities.ActivityRecord
import javax.`annotation`.processing.Generated
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
public class ActivityDao_Impl(
  __db: RoomDatabase,
) : ActivityDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfActivityRecord: EntityInsertAdapter<ActivityRecord>

  private val __deleteAdapterOfActivityRecord: EntityDeleteOrUpdateAdapter<ActivityRecord>

  private val __updateAdapterOfActivityRecord: EntityDeleteOrUpdateAdapter<ActivityRecord>
  init {
    this.__db = __db
    this.__insertAdapterOfActivityRecord = object : EntityInsertAdapter<ActivityRecord>() {
      protected override fun createQuery(): String = "INSERT OR REPLACE INTO `activity_records` (`id`,`type`,`title`,`subtitle`,`timestamp`,`iconResId`,`colorResId`,`notes`,`durationMinutes`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: ActivityRecord) {
        statement.bindLong(1, entity.id.toLong())
        statement.bindText(2, entity.type)
        statement.bindText(3, entity.title)
        statement.bindText(4, entity.subtitle)
        statement.bindLong(5, entity.timestamp)
        statement.bindLong(6, entity.iconResId.toLong())
        statement.bindLong(7, entity.colorResId.toLong())
        statement.bindText(8, entity.notes)
        statement.bindLong(9, entity.durationMinutes.toLong())
      }
    }
    this.__deleteAdapterOfActivityRecord = object : EntityDeleteOrUpdateAdapter<ActivityRecord>() {
      protected override fun createQuery(): String = "DELETE FROM `activity_records` WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: ActivityRecord) {
        statement.bindLong(1, entity.id.toLong())
      }
    }
    this.__updateAdapterOfActivityRecord = object : EntityDeleteOrUpdateAdapter<ActivityRecord>() {
      protected override fun createQuery(): String = "UPDATE OR ABORT `activity_records` SET `id` = ?,`type` = ?,`title` = ?,`subtitle` = ?,`timestamp` = ?,`iconResId` = ?,`colorResId` = ?,`notes` = ?,`durationMinutes` = ? WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: ActivityRecord) {
        statement.bindLong(1, entity.id.toLong())
        statement.bindText(2, entity.type)
        statement.bindText(3, entity.title)
        statement.bindText(4, entity.subtitle)
        statement.bindLong(5, entity.timestamp)
        statement.bindLong(6, entity.iconResId.toLong())
        statement.bindLong(7, entity.colorResId.toLong())
        statement.bindText(8, entity.notes)
        statement.bindLong(9, entity.durationMinutes.toLong())
        statement.bindLong(10, entity.id.toLong())
      }
    }
  }

  public override suspend fun insertActivity(activity: ActivityRecord): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfActivityRecord.insert(_connection, activity)
  }

  public override suspend fun deleteActivity(activity: ActivityRecord): Unit = performSuspending(__db, false, true) { _connection ->
    __deleteAdapterOfActivityRecord.handle(_connection, activity)
  }

  public override suspend fun updateActivity(activity: ActivityRecord): Unit = performSuspending(__db, false, true) { _connection ->
    __updateAdapterOfActivityRecord.handle(_connection, activity)
  }

  public override fun getAllActivities(): Flow<List<ActivityRecord>> {
    val _sql: String = "SELECT * FROM activity_records ORDER BY timestamp DESC"
    return createFlow(__db, false, arrayOf("activity_records")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfType: Int = getColumnIndexOrThrow(_stmt, "type")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfSubtitle: Int = getColumnIndexOrThrow(_stmt, "subtitle")
        val _columnIndexOfTimestamp: Int = getColumnIndexOrThrow(_stmt, "timestamp")
        val _columnIndexOfIconResId: Int = getColumnIndexOrThrow(_stmt, "iconResId")
        val _columnIndexOfColorResId: Int = getColumnIndexOrThrow(_stmt, "colorResId")
        val _columnIndexOfNotes: Int = getColumnIndexOrThrow(_stmt, "notes")
        val _columnIndexOfDurationMinutes: Int = getColumnIndexOrThrow(_stmt, "durationMinutes")
        val _result: MutableList<ActivityRecord> = mutableListOf()
        while (_stmt.step()) {
          val _item: ActivityRecord
          val _tmpId: Int
          _tmpId = _stmt.getLong(_columnIndexOfId).toInt()
          val _tmpType: String
          _tmpType = _stmt.getText(_columnIndexOfType)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpSubtitle: String
          _tmpSubtitle = _stmt.getText(_columnIndexOfSubtitle)
          val _tmpTimestamp: Long
          _tmpTimestamp = _stmt.getLong(_columnIndexOfTimestamp)
          val _tmpIconResId: Int
          _tmpIconResId = _stmt.getLong(_columnIndexOfIconResId).toInt()
          val _tmpColorResId: Int
          _tmpColorResId = _stmt.getLong(_columnIndexOfColorResId).toInt()
          val _tmpNotes: String
          _tmpNotes = _stmt.getText(_columnIndexOfNotes)
          val _tmpDurationMinutes: Int
          _tmpDurationMinutes = _stmt.getLong(_columnIndexOfDurationMinutes).toInt()
          _item = ActivityRecord(_tmpId,_tmpType,_tmpTitle,_tmpSubtitle,_tmpTimestamp,_tmpIconResId,_tmpColorResId,_tmpNotes,_tmpDurationMinutes)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getTodayActivities(todayStart: Long): Flow<List<ActivityRecord>> {
    val _sql: String = "SELECT * FROM activity_records WHERE timestamp >= ? ORDER BY timestamp DESC"
    return createFlow(__db, false, arrayOf("activity_records")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, todayStart)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfType: Int = getColumnIndexOrThrow(_stmt, "type")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfSubtitle: Int = getColumnIndexOrThrow(_stmt, "subtitle")
        val _columnIndexOfTimestamp: Int = getColumnIndexOrThrow(_stmt, "timestamp")
        val _columnIndexOfIconResId: Int = getColumnIndexOrThrow(_stmt, "iconResId")
        val _columnIndexOfColorResId: Int = getColumnIndexOrThrow(_stmt, "colorResId")
        val _columnIndexOfNotes: Int = getColumnIndexOrThrow(_stmt, "notes")
        val _columnIndexOfDurationMinutes: Int = getColumnIndexOrThrow(_stmt, "durationMinutes")
        val _result: MutableList<ActivityRecord> = mutableListOf()
        while (_stmt.step()) {
          val _item: ActivityRecord
          val _tmpId: Int
          _tmpId = _stmt.getLong(_columnIndexOfId).toInt()
          val _tmpType: String
          _tmpType = _stmt.getText(_columnIndexOfType)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpSubtitle: String
          _tmpSubtitle = _stmt.getText(_columnIndexOfSubtitle)
          val _tmpTimestamp: Long
          _tmpTimestamp = _stmt.getLong(_columnIndexOfTimestamp)
          val _tmpIconResId: Int
          _tmpIconResId = _stmt.getLong(_columnIndexOfIconResId).toInt()
          val _tmpColorResId: Int
          _tmpColorResId = _stmt.getLong(_columnIndexOfColorResId).toInt()
          val _tmpNotes: String
          _tmpNotes = _stmt.getText(_columnIndexOfNotes)
          val _tmpDurationMinutes: Int
          _tmpDurationMinutes = _stmt.getLong(_columnIndexOfDurationMinutes).toInt()
          _item = ActivityRecord(_tmpId,_tmpType,_tmpTitle,_tmpSubtitle,_tmpTimestamp,_tmpIconResId,_tmpColorResId,_tmpNotes,_tmpDurationMinutes)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteAll() {
    val _sql: String = "DELETE FROM activity_records"
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
