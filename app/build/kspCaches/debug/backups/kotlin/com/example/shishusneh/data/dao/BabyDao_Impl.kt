package com.example.shishusneh.`data`.dao

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.example.shishusneh.`data`.entities.BabyProfile
import javax.`annotation`.processing.Generated
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class BabyDao_Impl(
  __db: RoomDatabase,
) : BabyDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfBabyProfile: EntityInsertAdapter<BabyProfile>

  private val __deleteAdapterOfBabyProfile: EntityDeleteOrUpdateAdapter<BabyProfile>
  init {
    this.__db = __db
    this.__insertAdapterOfBabyProfile = object : EntityInsertAdapter<BabyProfile>() {
      protected override fun createQuery(): String = "INSERT OR REPLACE INTO `baby_profile` (`id`,`name`,`dateOfBirthMillis`,`gender`,`profileImageUri`) VALUES (nullif(?, 0),?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: BabyProfile) {
        statement.bindLong(1, entity.id.toLong())
        statement.bindText(2, entity.name)
        statement.bindLong(3, entity.dateOfBirthMillis)
        statement.bindText(4, entity.gender)
        val _tmpProfileImageUri: String? = entity.profileImageUri
        if (_tmpProfileImageUri == null) {
          statement.bindNull(5)
        } else {
          statement.bindText(5, _tmpProfileImageUri)
        }
      }
    }
    this.__deleteAdapterOfBabyProfile = object : EntityDeleteOrUpdateAdapter<BabyProfile>() {
      protected override fun createQuery(): String = "DELETE FROM `baby_profile` WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: BabyProfile) {
        statement.bindLong(1, entity.id.toLong())
      }
    }
  }

  public override suspend fun insertBabyProfile(profile: BabyProfile): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfBabyProfile.insert(_connection, profile)
  }

  public override suspend fun deleteBabyProfile(profile: BabyProfile): Unit = performSuspending(__db, false, true) { _connection ->
    __deleteAdapterOfBabyProfile.handle(_connection, profile)
  }

  public override fun getBabyProfile(): Flow<BabyProfile?> {
    val _sql: String = "SELECT * FROM baby_profile LIMIT 1"
    return createFlow(__db, false, arrayOf("baby_profile")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfDateOfBirthMillis: Int = getColumnIndexOrThrow(_stmt, "dateOfBirthMillis")
        val _columnIndexOfGender: Int = getColumnIndexOrThrow(_stmt, "gender")
        val _columnIndexOfProfileImageUri: Int = getColumnIndexOrThrow(_stmt, "profileImageUri")
        val _result: BabyProfile?
        if (_stmt.step()) {
          val _tmpId: Int
          _tmpId = _stmt.getLong(_columnIndexOfId).toInt()
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpDateOfBirthMillis: Long
          _tmpDateOfBirthMillis = _stmt.getLong(_columnIndexOfDateOfBirthMillis)
          val _tmpGender: String
          _tmpGender = _stmt.getText(_columnIndexOfGender)
          val _tmpProfileImageUri: String?
          if (_stmt.isNull(_columnIndexOfProfileImageUri)) {
            _tmpProfileImageUri = null
          } else {
            _tmpProfileImageUri = _stmt.getText(_columnIndexOfProfileImageUri)
          }
          _result = BabyProfile(_tmpId,_tmpName,_tmpDateOfBirthMillis,_tmpGender,_tmpProfileImageUri)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteAll() {
    val _sql: String = "DELETE FROM baby_profile"
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
