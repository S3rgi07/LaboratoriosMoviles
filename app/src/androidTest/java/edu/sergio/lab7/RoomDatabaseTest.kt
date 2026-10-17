package edu.sergio.lab7

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import edu.sergio.lab7.data.local.AppDatabase
import edu.sergio.lab7.data.local.CharacterEntity
import edu.sergio.lab7.data.local.LocationEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RoomDatabaseTest {
    private lateinit var database: AppDatabase

    @Before
    fun createDatabase() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    @After
    fun closeDatabase() = database.close()

    @Test
    fun storesBothTablesAndUpdatesFavoriteById() = runBlocking {
        database.characterDao().insertAll(
            listOf(CharacterEntity(1, "Rick", "Alive", "Human", "Male", "image")),
        )
        database.locationDao().insertAll(
            listOf(LocationEntity(1, "Earth", "Planet", "C-137")),
        )

        database.characterDao().updateFavorite(1, true)

        assertTrue(database.characterDao().observeById(1).first()!!.isFavorite)
        assertEquals(1, database.locationDao().observeAll().first().size)
    }
}
