package edu.sergio.lab7.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import edu.sergio.lab7.data.CharacterDb
import edu.sergio.lab7.ui.screens.CharacterDetailsScreen
import edu.sergio.lab7.ui.screens.CharactersScreen
import edu.sergio.lab7.ui.screens.LoginScreen

@Composable
fun LabNavigation(
    onExit: () -> Unit,
    navController: NavHostController = rememberNavController(),
) {
    val characterDb = remember { CharacterDb() }
    NavHost(navController = navController, startDestination = Login) {
        composable<Login> {
            LoginScreen(onStart = {
                navController.navigate(Characters) {
                    // El login deja de existir en el back stack.
                    popUpTo<Login> { inclusive = true }
                    launchSingleTop = true
                }
            })
        }
        composable<Characters> {
            // Finaliza la tarea: volver a abrir la app comienza en Login.
            BackHandler(onBack = onExit)
            CharactersScreen(
                characters = characterDb.getAllCharacters(),
                onCharacterClick = { id -> navController.navigate(CharacterDetails(id)) },
            )
        }
        composable<CharacterDetails> { entry ->
            val destination = entry.toRoute<CharacterDetails>()
            CharacterDetailsScreen(
                character = runCatching { characterDb.getCharacterById(destination.id) }.getOrNull(),
                onBack = { navController.popBackStack() },
            )
        }
    }
}
