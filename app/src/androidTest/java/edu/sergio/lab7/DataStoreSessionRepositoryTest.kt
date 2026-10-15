package edu.sergio.lab7

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import edu.sergio.lab7.data.DataStoreSessionRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DataStoreSessionRepositoryTest {
    private val context = ApplicationProvider.getApplicationContext<Context>()
    private val repository = DataStoreSessionRepository(context)

    @Before
    fun clearBeforeTest() = runBlocking { repository.clearUserName() }

    @After
    fun clearAfterTest() = runBlocking { repository.clearUserName() }

    @Test
    fun namePersistsAcrossRepositoryInstancesAndCanBeDeleted() = runBlocking {
        repository.saveUserName("  Summer Smith  ")

        val repositoryAfterRestart = DataStoreSessionRepository(context)
        assertEquals("Summer Smith", repositoryAfterRestart.userName.first())

        repositoryAfterRestart.clearUserName()
        assertNull(repository.userName.first())
    }
}
