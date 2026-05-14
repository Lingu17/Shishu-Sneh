package com.example.shishusneh.`data`

import androidx.room.InvalidationTracker
import androidx.room.RoomOpenDelegate
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.room.util.TableInfo
import androidx.room.util.TableInfo.Companion.read
import androidx.room.util.dropFtsSyncTriggers
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import com.example.shishusneh.`data`.dao.ActivityDao
import com.example.shishusneh.`data`.dao.ActivityDao_Impl
import com.example.shishusneh.`data`.dao.BabyDao
import com.example.shishusneh.`data`.dao.BabyDao_Impl
import com.example.shishusneh.`data`.dao.GrowthDao
import com.example.shishusneh.`data`.dao.GrowthDao_Impl
import com.example.shishusneh.`data`.dao.MilestoneDao
import com.example.shishusneh.`data`.dao.MilestoneDao_Impl
import com.example.shishusneh.`data`.dao.UserDao
import com.example.shishusneh.`data`.dao.UserDao_Impl
import com.example.shishusneh.`data`.dao.VaccinationDao
import com.example.shishusneh.`data`.dao.VaccinationDao_Impl
import javax.`annotation`.processing.Generated
import kotlin.Lazy
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.MutableList
import kotlin.collections.MutableMap
import kotlin.collections.MutableSet
import kotlin.collections.Set
import kotlin.collections.mutableListOf
import kotlin.collections.mutableMapOf
import kotlin.collections.mutableSetOf
import kotlin.reflect.KClass

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class AppDatabase_Impl : AppDatabase() {
  private val _babyDao: Lazy<BabyDao> = lazy {
    BabyDao_Impl(this)
  }

  private val _growthDao: Lazy<GrowthDao> = lazy {
    GrowthDao_Impl(this)
  }

  private val _vaccinationDao: Lazy<VaccinationDao> = lazy {
    VaccinationDao_Impl(this)
  }

  private val _milestoneDao: Lazy<MilestoneDao> = lazy {
    MilestoneDao_Impl(this)
  }

  private val _userDao: Lazy<UserDao> = lazy {
    UserDao_Impl(this)
  }

  private val _activityDao: Lazy<ActivityDao> = lazy {
    ActivityDao_Impl(this)
  }

  protected override fun createOpenDelegate(): RoomOpenDelegate {
    val _openDelegate: RoomOpenDelegate = object : RoomOpenDelegate(6, "ddc7cd41660c171c0f1c31c5c962f6cd", "f511309694a4b46d2848db9afd12919d") {
      public override fun createAllTables(connection: SQLiteConnection) {
        connection.execSQL("CREATE TABLE IF NOT EXISTS `baby_profile` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `dateOfBirthMillis` INTEGER NOT NULL, `gender` TEXT NOT NULL, `profileImageUri` TEXT)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `growth_records` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `dateMillis` INTEGER NOT NULL, `weightKg` REAL NOT NULL, `heightCm` REAL NOT NULL)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `vaccinations` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `diseasePrevented` TEXT NOT NULL, `dueDateMillis` INTEGER NOT NULL, `isCompleted` INTEGER NOT NULL)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `milestones` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `monthNumber` INTEGER NOT NULL, `description` TEXT NOT NULL, `isAchieved` INTEGER NOT NULL)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `users` (`email` TEXT NOT NULL, `password` TEXT NOT NULL, `fullName` TEXT NOT NULL, PRIMARY KEY(`email`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `activity_records` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `type` TEXT NOT NULL, `title` TEXT NOT NULL, `subtitle` TEXT NOT NULL, `timestamp` INTEGER NOT NULL, `iconResId` INTEGER NOT NULL, `colorResId` INTEGER NOT NULL, `notes` TEXT NOT NULL, `durationMinutes` INTEGER NOT NULL)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)")
        connection.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'ddc7cd41660c171c0f1c31c5c962f6cd')")
      }

      public override fun dropAllTables(connection: SQLiteConnection) {
        connection.execSQL("DROP TABLE IF EXISTS `baby_profile`")
        connection.execSQL("DROP TABLE IF EXISTS `growth_records`")
        connection.execSQL("DROP TABLE IF EXISTS `vaccinations`")
        connection.execSQL("DROP TABLE IF EXISTS `milestones`")
        connection.execSQL("DROP TABLE IF EXISTS `users`")
        connection.execSQL("DROP TABLE IF EXISTS `activity_records`")
      }

      public override fun onCreate(connection: SQLiteConnection) {
      }

      public override fun onOpen(connection: SQLiteConnection) {
        internalInitInvalidationTracker(connection)
      }

      public override fun onPreMigrate(connection: SQLiteConnection) {
        dropFtsSyncTriggers(connection)
      }

      public override fun onPostMigrate(connection: SQLiteConnection) {
      }

      public override fun onValidateSchema(connection: SQLiteConnection): RoomOpenDelegate.ValidationResult {
        val _columnsBabyProfile: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsBabyProfile.put("id", TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsBabyProfile.put("name", TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsBabyProfile.put("dateOfBirthMillis", TableInfo.Column("dateOfBirthMillis", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsBabyProfile.put("gender", TableInfo.Column("gender", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsBabyProfile.put("profileImageUri", TableInfo.Column("profileImageUri", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysBabyProfile: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesBabyProfile: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoBabyProfile: TableInfo = TableInfo("baby_profile", _columnsBabyProfile, _foreignKeysBabyProfile, _indicesBabyProfile)
        val _existingBabyProfile: TableInfo = read(connection, "baby_profile")
        if (!_infoBabyProfile.equals(_existingBabyProfile)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |baby_profile(com.example.shishusneh.data.entities.BabyProfile).
              | Expected:
              |""".trimMargin() + _infoBabyProfile + """
              |
              | Found:
              |""".trimMargin() + _existingBabyProfile)
        }
        val _columnsGrowthRecords: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsGrowthRecords.put("id", TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsGrowthRecords.put("dateMillis", TableInfo.Column("dateMillis", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsGrowthRecords.put("weightKg", TableInfo.Column("weightKg", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsGrowthRecords.put("heightCm", TableInfo.Column("heightCm", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysGrowthRecords: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesGrowthRecords: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoGrowthRecords: TableInfo = TableInfo("growth_records", _columnsGrowthRecords, _foreignKeysGrowthRecords, _indicesGrowthRecords)
        val _existingGrowthRecords: TableInfo = read(connection, "growth_records")
        if (!_infoGrowthRecords.equals(_existingGrowthRecords)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |growth_records(com.example.shishusneh.data.entities.GrowthRecord).
              | Expected:
              |""".trimMargin() + _infoGrowthRecords + """
              |
              | Found:
              |""".trimMargin() + _existingGrowthRecords)
        }
        val _columnsVaccinations: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsVaccinations.put("id", TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsVaccinations.put("name", TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsVaccinations.put("diseasePrevented", TableInfo.Column("diseasePrevented", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsVaccinations.put("dueDateMillis", TableInfo.Column("dueDateMillis", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsVaccinations.put("isCompleted", TableInfo.Column("isCompleted", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysVaccinations: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesVaccinations: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoVaccinations: TableInfo = TableInfo("vaccinations", _columnsVaccinations, _foreignKeysVaccinations, _indicesVaccinations)
        val _existingVaccinations: TableInfo = read(connection, "vaccinations")
        if (!_infoVaccinations.equals(_existingVaccinations)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |vaccinations(com.example.shishusneh.data.entities.Vaccination).
              | Expected:
              |""".trimMargin() + _infoVaccinations + """
              |
              | Found:
              |""".trimMargin() + _existingVaccinations)
        }
        val _columnsMilestones: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsMilestones.put("id", TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsMilestones.put("monthNumber", TableInfo.Column("monthNumber", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsMilestones.put("description", TableInfo.Column("description", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsMilestones.put("isAchieved", TableInfo.Column("isAchieved", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysMilestones: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesMilestones: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoMilestones: TableInfo = TableInfo("milestones", _columnsMilestones, _foreignKeysMilestones, _indicesMilestones)
        val _existingMilestones: TableInfo = read(connection, "milestones")
        if (!_infoMilestones.equals(_existingMilestones)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |milestones(com.example.shishusneh.data.entities.Milestone).
              | Expected:
              |""".trimMargin() + _infoMilestones + """
              |
              | Found:
              |""".trimMargin() + _existingMilestones)
        }
        val _columnsUsers: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsUsers.put("email", TableInfo.Column("email", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUsers.put("password", TableInfo.Column("password", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUsers.put("fullName", TableInfo.Column("fullName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysUsers: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesUsers: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoUsers: TableInfo = TableInfo("users", _columnsUsers, _foreignKeysUsers, _indicesUsers)
        val _existingUsers: TableInfo = read(connection, "users")
        if (!_infoUsers.equals(_existingUsers)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |users(com.example.shishusneh.data.entities.User).
              | Expected:
              |""".trimMargin() + _infoUsers + """
              |
              | Found:
              |""".trimMargin() + _existingUsers)
        }
        val _columnsActivityRecords: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsActivityRecords.put("id", TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsActivityRecords.put("type", TableInfo.Column("type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsActivityRecords.put("title", TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsActivityRecords.put("subtitle", TableInfo.Column("subtitle", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsActivityRecords.put("timestamp", TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsActivityRecords.put("iconResId", TableInfo.Column("iconResId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsActivityRecords.put("colorResId", TableInfo.Column("colorResId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsActivityRecords.put("notes", TableInfo.Column("notes", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsActivityRecords.put("durationMinutes", TableInfo.Column("durationMinutes", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysActivityRecords: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesActivityRecords: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoActivityRecords: TableInfo = TableInfo("activity_records", _columnsActivityRecords, _foreignKeysActivityRecords, _indicesActivityRecords)
        val _existingActivityRecords: TableInfo = read(connection, "activity_records")
        if (!_infoActivityRecords.equals(_existingActivityRecords)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |activity_records(com.example.shishusneh.data.entities.ActivityRecord).
              | Expected:
              |""".trimMargin() + _infoActivityRecords + """
              |
              | Found:
              |""".trimMargin() + _existingActivityRecords)
        }
        return RoomOpenDelegate.ValidationResult(true, null)
      }
    }
    return _openDelegate
  }

  protected override fun createInvalidationTracker(): InvalidationTracker {
    val _shadowTablesMap: MutableMap<String, String> = mutableMapOf()
    val _viewTables: MutableMap<String, Set<String>> = mutableMapOf()
    return InvalidationTracker(this, _shadowTablesMap, _viewTables, "baby_profile", "growth_records", "vaccinations", "milestones", "users", "activity_records")
  }

  public override fun clearAllTables() {
    super.performClear(false, "baby_profile", "growth_records", "vaccinations", "milestones", "users", "activity_records")
  }

  protected override fun getRequiredTypeConverterClasses(): Map<KClass<*>, List<KClass<*>>> {
    val _typeConvertersMap: MutableMap<KClass<*>, List<KClass<*>>> = mutableMapOf()
    _typeConvertersMap.put(BabyDao::class, BabyDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(GrowthDao::class, GrowthDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(VaccinationDao::class, VaccinationDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(MilestoneDao::class, MilestoneDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(UserDao::class, UserDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(ActivityDao::class, ActivityDao_Impl.getRequiredConverters())
    return _typeConvertersMap
  }

  public override fun getRequiredAutoMigrationSpecClasses(): Set<KClass<out AutoMigrationSpec>> {
    val _autoMigrationSpecsSet: MutableSet<KClass<out AutoMigrationSpec>> = mutableSetOf()
    return _autoMigrationSpecsSet
  }

  public override fun createAutoMigrations(autoMigrationSpecs: Map<KClass<out AutoMigrationSpec>, AutoMigrationSpec>): List<Migration> {
    val _autoMigrations: MutableList<Migration> = mutableListOf()
    return _autoMigrations
  }

  public override fun babyDao(): BabyDao = _babyDao.value

  public override fun growthDao(): GrowthDao = _growthDao.value

  public override fun vaccinationDao(): VaccinationDao = _vaccinationDao.value

  public override fun milestoneDao(): MilestoneDao = _milestoneDao.value

  public override fun userDao(): UserDao = _userDao.value

  public override fun activityDao(): ActivityDao = _activityDao.value
}
