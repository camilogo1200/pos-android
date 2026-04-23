package com.hawk.common.database.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.hawk.authentication.data.datasources.local.AuthenticationSessionDao
import com.hawk.common.database.HawkDatabase
import com.hawk.common.environment.AppEnvironment
import com.hawk.customers.data.datasources.local.CustomerDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    private const val SCHEMA_ASSETS_PATH = "database/schema"

    @Provides
    @Singleton
    fun provideHawkDatabase(
        @ApplicationContext context: Context
    ): HawkDatabase = Room.databaseBuilder(
        context,
        HawkDatabase::class.java,
        AppEnvironment.databaseName
    ).fallbackToDestructiveMigration()
        .addCallback(provideDatabaseCreationCallback(context))
        .build()

    @Provides
    @Singleton
    fun provideAuthenticationSessionDao(
        hawkDatabase: HawkDatabase
    ): AuthenticationSessionDao = hawkDatabase.authenticationSessionDao()

    @Provides
    @Singleton
    fun provideCustomerDao(
        hawkDatabase: HawkDatabase
    ): CustomerDao = hawkDatabase.customerDao()

    private fun provideDatabaseCreationCallback(
        context: Context
    ): RoomDatabase.Callback = object : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            executeSchemaScripts(
                context = context,
                database = db,
                folder = SCHEMA_ASSETS_PATH
            )
        }
    }

    private fun executeSchemaScripts(
        context: Context,
        database: SupportSQLiteDatabase,
        folder: String
    ) {
        val files = context.assets.list(folder).orEmpty().sorted()
        files.forEach { fileName ->
            val sqlStatement = context.assets
                .open("$folder/$fileName")
                .bufferedReader()
                .use { it.readText() }
                .trim()
                .removeSuffix(";")

            if (sqlStatement.isNotBlank()) {
                database.execSQL(sqlStatement)
            }
        }
    }
}
