package com.rishika.budgettracker.ui.theme

import android.content.Context
import androidx.room.Room
import com.rishika.budgettracker.AppDatabase

object DatabaseProvider {
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        if (INSTANCE == null) {
            synchronized(AppDatabase::class) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "budget_tracker_db"
                ).build()
            }
        }
        return INSTANCE!!
    }
}

