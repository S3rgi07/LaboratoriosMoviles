package edu.sergio.lab7

import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.testing.TestNavHostController
import androidx.navigation.toRoute
import androidx.test.ext.junit.runners.AndroidJUnit4
import edu.sergio.lab7.navigation.*
import edu.sergio.lab7.ui.theme.LabTheme
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NavigationTest {
    @get:Rule val compose = createAndroidComposeRule<ComponentActivity>()
    private lateinit var navController: TestNavHostController
    private var exited = false

    @Before fun setup() {
        compose.setContent {
            navController = TestNavHostController(LocalContext.current).apply {
                navigatorProvider.addNavigator(ComposeNavigator())
            }
            LabTheme { LabNavigation(onExit = { exited = true }, navController = navController) }
        }
    }

    @Test fun startsWithStudentNameAndId() {
        compose.onNodeWithText("Sergio Daniel López Barrientos").assertIsDisplayed()
        compose.onNodeWithText("Carné 25848").assertIsDisplayed()
        compose.onNodeWithText("Empezar").assertIsDisplayed()
    }

    @Test fun startRemovesLoginAndSystemBackExits() {
        compose.onNodeWithText("Empezar").performClick()
        compose.onNodeWithText("Characters").assertIsDisplayed()
        compose.runOnIdle {
            assertTrue(navController.currentDestination!!.hasRoute<Characters>())
            assertNull(navController.previousBackStackEntry)
            compose.activity.onBackPressedDispatcher.onBackPressed()
        }
        compose.runOnIdle { assertTrue(exited) }
    }

    @Test fun opensMortyByIdAndBothBackActionsReturnToList() {
        compose.onNodeWithText("Empezar").performClick()
        compose.onNodeWithText("Morty Smith").performClick()
        compose.onNodeWithText("Characters details").assertIsDisplayed()
        compose.onNodeWithText("Morty Smith").assertIsDisplayed()
        compose.onNodeWithText("Human").assertIsDisplayed()
        compose.onNodeWithText("Alive").assertIsDisplayed()
        compose.onNodeWithText("Male").assertIsDisplayed()
        compose.runOnIdle {
            assertEquals(2, navController.currentBackStackEntry!!.toRoute<CharacterDetails>().id)
        }
        compose.onNodeWithContentDescription("Volver").performClick()
        compose.onNodeWithText("Characters").assertIsDisplayed()
        compose.onNodeWithText("Morty Smith").performClick()
        compose.runOnIdle { compose.activity.onBackPressedDispatcher.onBackPressed() }
        compose.onNodeWithText("Characters").assertIsDisplayed()
    }

    @Test fun invalidIdShowsFallbackInsteadOfCrashing() {
        compose.onNodeWithText("Empezar").performClick()
        compose.runOnIdle { navController.navigate(CharacterDetails(-1)) }
        compose.onNodeWithText("No se encontró este personaje.").assertIsDisplayed()
        compose.onNodeWithContentDescription("Volver").performClick()
        compose.onNodeWithText("Characters").assertIsDisplayed()
    }
}
