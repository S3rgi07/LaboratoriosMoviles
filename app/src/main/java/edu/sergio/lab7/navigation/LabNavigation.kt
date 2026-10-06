package edu.sergio.lab7.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.sergio.lab7.R
import edu.sergio.lab7.ui.screens.*
import edu.sergio.lab7.ui.viewmodel.*

private enum class BottomSection { CHARACTERS, LOCATIONS, PROFILE }

@Composable
fun LabNavigation(
    onExit: () -> Unit,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(navController = navController, startDestination = Login) {
        composable<Login> {
            LoginScreen(onStart = {
                navController.navigate(CharactersGraph) {
                    popUpTo<Login> { inclusive = true }
                    launchSingleTop = true
                }
            })
        }

        navigation<CharactersGraph>(startDestination = Characters) {
            composable<Characters> {
                val charactersViewModel: CharactersViewModel = viewModel()
                val state by charactersViewModel.uiState.collectAsStateWithLifecycle()
                BackHandler(onBack = onExit)
                MainScaffold(
                    selected = BottomSection.CHARACTERS,
                    onSelect = { section -> navigateBottom(navController, section) },
                ) {
                    CharactersScreen(
                        state = state,
                        onLoadingClick = charactersViewModel::showError,
                        onRetry = charactersViewModel::loadCharacters,
                        onCharacterClick = { id -> navController.navigate(CharacterDetails(id)) },
                    )
                }
            }
            composable<CharacterDetails> {
                val characterDetailsViewModel: CharacterDetailsViewModel = viewModel()
                val state by characterDetailsViewModel.uiState.collectAsStateWithLifecycle()
                CharacterDetailsScreen(
                    state = state,
                    onLoadingClick = characterDetailsViewModel::showError,
                    onRetry = characterDetailsViewModel::loadCharacter,
                    onBack = { navController.popBackStack() },
                )
            }
        }

        navigation<LocationsGraph>(startDestination = Locations) {
            composable<Locations> {
                val locationsViewModel: LocationsViewModel = viewModel()
                val state by locationsViewModel.uiState.collectAsStateWithLifecycle()
                MainScaffold(
                    selected = BottomSection.LOCATIONS,
                    onSelect = { section -> navigateBottom(navController, section) },
                ) {
                    LocationsScreen(
                        state = state,
                        onLoadingClick = locationsViewModel::showError,
                        onRetry = locationsViewModel::loadLocations,
                        onLocationClick = { id -> navController.navigate(LocationDetails(id)) },
                    )
                }
            }
            composable<LocationDetails> {
                val locationDetailsViewModel: LocationDetailsViewModel = viewModel()
                val state by locationDetailsViewModel.uiState.collectAsStateWithLifecycle()
                LocationDetailsScreen(
                    state = state,
                    onLoadingClick = locationDetailsViewModel::showError,
                    onRetry = locationDetailsViewModel::loadLocation,
                    onBack = { navController.popBackStack() },
                )
            }
        }

        navigation<ProfileGraph>(startDestination = Profile) {
            composable<Profile> {
                MainScaffold(
                    selected = BottomSection.PROFILE,
                    onSelect = { section -> navigateBottom(navController, section) },
                ) {
                    ProfileScreen(onLogout = {
                        navController.navigate(Login) {
                            // Clear the complete authenticated flow so Android Back cannot reopen it.
                            popUpTo(navController.graph.id) { inclusive = true }
                            launchSingleTop = true
                        }
                    })
                }
            }
        }
    }
}

private fun navigateBottom(navController: NavHostController, section: BottomSection) {
    val destination: Any = when (section) {
        BottomSection.CHARACTERS -> CharactersGraph
        BottomSection.LOCATIONS -> LocationsGraph
        BottomSection.PROFILE -> ProfileGraph
    }
    navController.navigate(destination) {
        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}

@Composable
private fun MainScaffold(
    selected: BottomSection,
    onSelect: (BottomSection) -> Unit,
    content: @Composable () -> Unit,
) {
    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = selected == BottomSection.CHARACTERS,
                    onClick = { onSelect(BottomSection.CHARACTERS) },
                    icon = { Icon(
                        painter = painterResource(R.drawable.ic_group),
                        contentDescription = stringResource(R.string.characters),
                        modifier = Modifier.size(24.dp),
                    ) },
                    label = { Text(stringResource(R.string.characters)) },
                )
                NavigationBarItem(
                    selected = selected == BottomSection.LOCATIONS,
                    onClick = { onSelect(BottomSection.LOCATIONS) },
                    icon = { Icon(
                        painter = painterResource(R.drawable.ic_portal),
                        contentDescription = stringResource(R.string.locations),
                        modifier = Modifier.size(24.dp),
                    ) },
                    label = { Text(stringResource(R.string.locations)) },
                )
                NavigationBarItem(
                    selected = selected == BottomSection.PROFILE,
                    onClick = { onSelect(BottomSection.PROFILE) },
                    icon = { Icon(
                        painter = painterResource(R.drawable.ic_person),
                        contentDescription = stringResource(R.string.profile),
                        modifier = Modifier.size(24.dp),
                    ) },
                    label = { Text(stringResource(R.string.profile)) },
                )
            }
        },
    ) { padding ->
        androidx.compose.foundation.layout.Box(Modifier.padding(padding)) { content() }
    }
}
