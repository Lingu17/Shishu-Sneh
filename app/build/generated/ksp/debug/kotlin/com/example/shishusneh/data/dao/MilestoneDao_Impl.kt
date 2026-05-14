package com.example.shishusneh.`data`.dao

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.example.shishusneh.`data`.entities.Milestone
import javax.`annotation`.processing.Generated
import kotlin.Boolean
import kotlin.Int
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
public class MilestoneDao_Impl(
  __db: RoomDatabase,
) : MilestoneDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfMilestone: EntityInsertAdapter<Milestone>

  private val __updateAdapterOfMilestone: EntityDeleteOrUpdateAdapter<Milestone>
  init {
    this.__db = __db
    this.__insertAdapterOfMilestone = object : EntityInsertAdapter<Milestone>() {
      protected override fun createQuery(): String = "INSERT OR ABORT INTO `milestones` (`id`,`monthNumber`,`description`,`isAchieved`) VALUES (nullif(?, 0),?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: Milestone) {
        statement.bindLong(1, entity.id.toLong())
        statement.bindLong(2, entity.monthNumber.toLong())
        statement.bindText(3, entity.description)
        val _tmp: Int = if (entity.isAchieved) 1 else 0
        statement.bindLong(4, _tmp.toLong())
      }
    }
    this.__updateAdapterOfMilestone = object : EntityDeleteOrUpdateAdapter<Milestone>() {
      protected override fun createQuery(): String = "UPDATE OR ABORT `milestones` SET `id` = ?,`monthNumber` = ?,`description` = ?,`isAchieved` = ? WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: Milestone) {
        statement.bindLong(1, entity.id.toLong())
        statement.bindLong(2, entity.monthNumber.toLong())
        statement.bindText(3, entity.description)
        val _tmp: Int = if (entity.isAchieved) 1 else 0
        statement.bindLong(4, _tmp.toLong())
        statement.bindLong(5, entity.id.toLong())
      }
    }
  }

  public override suspend fun insertMilestones(milestones: List<Milestone>): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfMilestone.insert(_connection, milestones)
  }

  public override suspend fun updateMilestone(milestone: Milestone): Unit = performSuspending(__db, false, true) { _connection ->
    __updateAdapterOfMilestone.handle(_connection, milestone)
  }

  public override fun getAllMilestones(): Flow<List<Milestone>> {
    val _sql: String = "SELECT * FROM milestones ORDER BY monthNumber ASC"
    return createFlow(__db, false, arrayOf("milestones")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfMonthNumber: Int = getColumnIndexOrThrow(_stmt, "monthNumber")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfIsAchieved: Int = getColumnIndexOrThrow(_stmt, "isAchieved")
        val _result: MutableList<Milestone> = mutableListOf()
        while (_stmt.step()) {
          val _item: Milestone
          val _tmpId: Int
          _tmpId = _stmt.getLong(_columnIndexOfId).toInt()
          val _tmpMonthNumber: Int
          _tmpMonthNumber = _stmt.getLong(_columnIndexOfMonthNumber).toInt()
          val _tmpDescription: String
          _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          val _tmpIsAchieved: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsAchieved).toInt()
          _tmpIsAchieved = _tmp != 0
          _item = Milestone(_tmpId,_tmpMonthNumber,_tmpDescription,_tmpIsAchieved)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteAll() {
    val _sql: String = "DELETE FROM milestones"
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
