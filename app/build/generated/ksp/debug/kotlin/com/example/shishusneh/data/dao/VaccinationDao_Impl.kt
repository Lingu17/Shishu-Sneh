package com.example.shishusneh.`data`.dao

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.example.shishusneh.`data`.entities.Vaccination
import javax.`annotation`.processing.Generated
import kotlin.Boolean
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
public class VaccinationDao_Impl(
  __db: RoomDatabase,
) : VaccinationDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfVaccination: EntityInsertAdapter<Vaccination>

  private val __updateAdapterOfVaccination: EntityDeleteOrUpdateAdapter<Vaccination>
  init {
    this.__db = __db
    this.__insertAdapterOfVaccination = object : EntityInsertAdapter<Vaccination>() {
      protected override fun createQuery(): String = "INSERT OR ABORT INTO `vaccinations` (`id`,`name`,`diseasePrevented`,`dueDateMillis`,`isCompleted`) VALUES (nullif(?, 0),?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: Vaccination) {
        statement.bindLong(1, entity.id.toLong())
        statement.bindText(2, entity.name)
        statement.bindText(3, entity.diseasePrevented)
        statement.bindLong(4, entity.dueDateMillis)
        val _tmp: Int = if (entity.isCompleted) 1 else 0
        statement.bindLong(5, _tmp.toLong())
      }
    }
    this.__updateAdapterOfVaccination = object : EntityDeleteOrUpdateAdapter<Vaccination>() {
      protected override fun createQuery(): String = "UPDATE OR ABORT `vaccinations` SET `id` = ?,`name` = ?,`diseasePrevented` = ?,`dueDateMillis` = ?,`isCompleted` = ? WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: Vaccination) {
        statement.bindLong(1, entity.id.toLong())
        statement.bindText(2, entity.name)
        statement.bindText(3, entity.diseasePrevented)
        statement.bindLong(4, entity.dueDateMillis)
        val _tmp: Int = if (entity.isCompleted) 1 else 0
        statement.bindLong(5, _tmp.toLong())
        statement.bindLong(6, entity.id.toLong())
      }
    }
  }

  public override suspend fun insertVaccinations(vaccinations: List<Vaccination>): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfVaccination.insert(_connection, vaccinations)
  }

  public override suspend fun updateVaccination(vaccination: Vaccination): Unit = performSuspending(__db, false, true) { _connection ->
    __updateAdapterOfVaccination.handle(_connection, vaccination)
  }

  public override fun getAllVaccinations(): Flow<List<Vaccination>> {
    val _sql: String = "SELECT * FROM vaccinations ORDER BY dueDateMillis ASC"
    return createFlow(__db, false, arrayOf("vaccinations")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfDiseasePrevented: Int = getColumnIndexOrThrow(_stmt, "diseasePrevented")
        val _columnIndexOfDueDateMillis: Int = getColumnIndexOrThrow(_stmt, "dueDateMillis")
        val _columnIndexOfIsCompleted: Int = getColumnIndexOrThrow(_stmt, "isCompleted")
        val _result: MutableList<Vaccination> = mutableListOf()
        while (_stmt.step()) {
          val _item: Vaccination
          val _tmpId: Int
          _tmpId = _stmt.getLong(_columnIndexOfId).toInt()
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpDiseasePrevented: String
          _tmpDiseasePrevented = _stmt.getText(_columnIndexOfDiseasePrevented)
          val _tmpDueDateMillis: Long
          _tmpDueDateMillis = _stmt.getLong(_columnIndexOfDueDateMillis)
          val _tmpIsCompleted: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsCompleted).toInt()
          _tmpIsCompleted = _tmp != 0
          _item = Vaccination(_tmpId,_tmpName,_tmpDiseasePrevented,_tmpDueDateMillis,_tmpIsCompleted)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteAll() {
    val _sql: String = "DELETE FROM vaccinations"
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
