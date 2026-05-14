package com.example.shishusneh.`data`.dao

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.example.shishusneh.`data`.entities.User
import javax.`annotation`.processing.Generated
import kotlin.Int
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.reflect.KClass

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class UserDao_Impl(
  __db: RoomDatabase,
) : UserDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfUser: EntityInsertAdapter<User>
  init {
    this.__db = __db
    this.__insertAdapterOfUser = object : EntityInsertAdapter<User>() {
      protected override fun createQuery(): String = "INSERT OR ABORT INTO `users` (`email`,`password`,`fullName`) VALUES (?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: User) {
        statement.bindText(1, entity.email)
        statement.bindText(2, entity.password)
        statement.bindText(3, entity.fullName)
      }
    }
  }

  public override suspend fun insertUser(user: User): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfUser.insert(_connection, user)
  }

  public override suspend fun getUserByEmail(email: String): User? {
    val _sql: String = "SELECT * FROM users WHERE email = ? LIMIT 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, email)
        val _columnIndexOfEmail: Int = getColumnIndexOrThrow(_stmt, "email")
        val _columnIndexOfPassword: Int = getColumnIndexOrThrow(_stmt, "password")
        val _columnIndexOfFullName: Int = getColumnIndexOrThrow(_stmt, "fullName")
        val _result: User?
        if (_stmt.step()) {
          val _tmpEmail: String
          _tmpEmail = _stmt.getText(_columnIndexOfEmail)
          val _tmpPassword: String
          _tmpPassword = _stmt.getText(_columnIndexOfPassword)
          val _tmpFullName: String
          _tmpFullName = _stmt.getText(_columnIndexOfFullName)
          _result = User(_tmpEmail,_tmpPassword,_tmpFullName)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
