package com.example.myapplication

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [User::class],
    //views = [CustomerTaskDetailView::class],
    version = 0
)
abstract class DataBase : RoomDatabase() {

    abstract fun userDao(): UsersDao
    //abstract fun customerTaskDao(): CustomerTaskDao
    //abstract fun customerTaskDetailViewDao(): CustomerTaskDetailViewDao

    //Behaves like a static attribute
    companion object {
        @Volatile
        private var INSTANCE: DataBase? = null

        fun getCustomerDatabaseInstance(context: Context): DataBase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                DataBase::class.java,
                "Customers.db"
            )
                .fallbackToDestructiveMigration()
                //May use migration objets or each new schema
                //.addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                .build()

        fun getCustomerDatabaseInstance(
            context: Context,
            scope: CoroutineScope
        ): DataBase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context, scope).also { INSTANCE = it }
            }

        /** Populate DB through the use of a RoomDatabase.Callback use in Room.databaseBuilder(). */
        private fun buildDatabase(context: Context, scope: CoroutineScope) =
            Room.databaseBuilder(
                context.applicationContext,
                DataBase::class.java,
                "Customers.db"
            )
                .fallbackToDestructiveMigration()
                //Use migration objects for each new schema evolution
                //.addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                //Use RoomDatabase.Callback() to clear and repopulate DB instead of migrating
                .addCallback(CustomersDatabaseCallback(scope))
                .build()
    }

    /**
     * The RoomDatabase.Callback() is called on DB databaseBuilder():
     *  1. override onOpen(): clear and repopulate DB whenever app is started;
     *  2. override the onCreate(): populate DB only the first time the app is launched.
     */
    private class CustomersDatabaseCallback(private val scope: CoroutineScope) :
        RoomDatabase.Callback() {

        /** Override onOpen() to clear and populate DB every time app is started. */
        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            // To keep DB data through app restarts comment coroutine exec:
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    cleanAndPopulateCustomersDatabase(database.userDao())
                }
            }
        }

        /** Overrite onCreate() to populate DB only first time app is launched. */
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            //To clear and repopulate DB every time app is started comment coroutine exec:
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    //cleanAndPopulateCustomersDatabase(database.customerDao())
                }
            }
        }

        /**
         * Remove all customers from DB and populate with some customers.
         */
        fun cleanAndPopulateCustomersDatabase(customerDao: UsersDao) {
            // Clear all customers from DB
            customerDao.deleteAllcustomers()
            //Populate with some Patinhas customers
            for (i in 1..LoaderCustomersContentDatabase.COUNT) {
                //CREATE
                val customer: User =
                    User(
                        i, "Tio Patinhas $i",
                    )
                Log.e(
                    this.javaClass.simpleName,
                    "addSampleItemsToDatabase(): create customer = $customer"
                )
                //INSERT
                val id: Long = customerDao.insertCustomer(customer)
                Log.e(
                    this.javaClass.simpleName,
                    "addSampleItemsToDatabase(): added record id = $id"
                )
            }
        }
    }

    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(
                "CREATE TABLE `tasktypes` (`id` INTEGER, `tasktitle` TEXT, " +
                        "PRIMARY KEY(`id`))"
            )
        }
    }

    val MIGRATION_2_3: Migration
        get() = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE tasktypes ADD COLUMN taskpriority INTEGER")
            }
        }
}