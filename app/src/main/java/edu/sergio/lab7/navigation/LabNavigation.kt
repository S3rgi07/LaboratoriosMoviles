package edu.sergio.lab7.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.sergio.lab7.R
import edu.sergio.lab7.data.DataStoreSessionRepository
import edu.sergio.lab7.data.InitialSyncRepository
import edu.sergio.lab7.data.SessionRepository
import edu.sergio.lab7.data.local.AppDatabase
import edu.sergio.lab7.ui.screens.*
import edu.sergio.lab7.ui.viewmodel.*

private enum class BottomSection { CHARACTERS, LOCATIONS, PROFILE }

@Composable
fun LabNavigation(
    onExit: () -> Unit,
    navController: NavHostController = rememberNavController(),
    sessionRepository: SessionRepository? = null,
) {
    val context = LocalContext.current
    val repository = remember(context, sessionRepository) {
        sessionRepository ?: DataStoreSessionRepository(context.applicationContext)
    }
    val database = remember(context) { AppDatabase.getInstance(context.applicationContext) }
    val syncRepository = remember(database) { InitialSyncRepository(database) }
    val sessionViewModel: SessionViewModel = viewModel(
        factory = SessionViewModel.factory(repository, syncRepository),
    )
    val sessionState by sessionViewModel.uiState.collectAsStateWithLifecycle()
    val loginState by sessionViewModel.loginUiState.collectAsStateWithLifecycle()

    NavHost(navController = navController, startDestination = SessionGate) {
        composable<SessionGate> {
            SessionLoadingScreen()
            LaunchedEffect(sessionState) {
                when (sessionState) {
                    SessionUiState.Loading -> Unit
                    SessionUiState.LoggedOut -> navController.navigate(Login) {
                        popUpTo<SessionGate> { inclusive = true }
                    }
                    is SessionUiState.LoggedIn -> navigateToCharacters(navController, SessionGate)
                }
            }
        }

        composable<Login> {
            BackHandler(onBack = onExit)
            LaunchedEffect(sessionState) {
                if (sessionState is SessionUiState.LoggedIn) {
                    navigateToCharacters(navController, Login)
                }
            }
            LoginScreen(
                isSynchronizing = loginState.isSynchronizing,
                hasError = loginState.hasError,
                onStart = sessionViewModel::logIn,
            )
        }

        navigation<CharactersGraph>(startDestination = Characters) {
            composable<Characters> {
                val charactersViewModel: CharactersViewModel = viewModel(
                    factory = CharactersViewModel.factory(database.characterDao()),
                )
                val state by charactersViewModel.uiState.collectAsStateWithLifecycle()
                BackHandler(onBack = onExit)
                MainScaffold(
                    selected = BottomSection.CHARACTERS,
                    onSelect = { section -> navigateBottom(navController, section) },
                ) {
                    CharactersScreen(
                        state = state,
                        onLoadingClick = {},
                        onRetry = {},
                        onCharacterClick = { id -> navController.navigate(CharacterDetails(id)) },
                        onFavoriteClick = charactersViewModel::toggleFavorite,
                        onFavoritesFilterChange = charactersViewModel::setFavoritesOnly,
                    )
                }
            }
            composable<CharacterDetails> {
                val characterDetailsViewModel: CharacterDetailsViewModel = viewModel(
                    factory = CharacterDetailsViewModel.factory(database.characterDao()),
                )
                val state by characterDetailsViewModel.uiState.collectAsStateWithLifecycle()
                CharacterDetailsScreen(
                    state = state,
                    onLoadingClick = {},
                    onRetry = {},
                    onBack = { navController.popBackStack() },
                    onFavoriteClick = characterDetailsViewModel::toggleFavorite,
                )
            }
        }

        navigation<LocationsGraph>(startDestination = Locations) {
            composable<Locations> {
                val locationsViewModel: LocationsViewModel = viewModel(
                    factory = LocationsViewModel.factory(database.locationDao()),
                )
                val state by locationsViewModel.uiState.collectAsStateWithLifecycle()
                MainScaffold(
                    selected = BottomSection.LOCATIONS,
                    onSelect = { section -> navigateBottom(navController, section) },
                ) {
                    LocationsScreen(
                        state = state,
                        onLoadingClick = {},
                        onRetry = {},
                        onLocationClick = { id -> navController.navigate(LocationDetails(id)) },
                    )
                }
            }
            composable<LocationDetails> {
                val locationDetailsViewModel: LocationDetailsViewModel = viewModel(
                    factory = LocationDetailsViewModel.factory(database.locationDao()),
                )
                val state by locationDetailsViewModel.uiState.collectAsStateWithLifecycle()
                LocationDetailsScreen(
                    state = state,
                    onLoadingClick = {},
                    onRetry = {},
                    onBack = { navController.popBackStack() },
                )
            }
        }

        navigation<ProfileGraph>(startDestination = Profile) {
            composable<Profile> {
                LaunchedEffect(sessionState) {
                    if (sessionState is SessionUiState.LoggedOut) {
                        navController.navigate(Login) {
                            popUpTo(navController.graph.id) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                }
                MainScaffold(
                    selected = BottomSection.PROFILE,
                    onSelect = { section -> navigateBottom(navController, section) },
                ) {
                    val name = (sessionState as? SessionUiState.LoggedIn)?.userName.orEmpty()
                    ProfileScreen(userName = name, onLogout = sessionViewModel::logOut)
                }
            }
        }
    }
}

private fun navigateToCharacters(navController: NavHostController, source: Any) {
    navController.navigate(CharactersGraph) {
        when (source) {
            Login -> popUpTo<Login> { inclusive = true }
            SessionGate -> popUpTo<SessionGate> { inclusive = true }
        }
        launchSingleTop = true
    }
}

@Composable
private fun SessionLoadingScreen() {
    androidx.compose.foundation.layout.Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}

private fun navigateBottom(navController: NavHostController, section: BottomSection) {
    val destination: Any = when (section) {
        BottomSection.CHARACTERS -> CharactersGraph
        BottomSection.LOCATIONS -> LocationsGraph
        BottomSection.PROFILE -> ProfileGraph
    }
    navController.navigate(destination) {
        popUpTo<CharactersGraph> { saveState = true }
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
