package com.example.dogbreeds

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.dogbreeds.di.AppDependencies
import com.example.dogbreeds.navigation.*
import com.example.dogbreeds.ui.*
import com.example.dogbreeds.ui.viewmodel.BreedListViewModel
import com.example.dogbreeds.ui.viewmodel.FavoritesViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppDependencies.init(applicationContext)
        setContent {
            Scaffold(modifier = Modifier.fillMaxSize()) { padding ->
                MainScreen(modifier = Modifier.padding(padding))
            }
        }
    }
}

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val repo = AppDependencies.repository
    val backStack = rememberNavBackStack(BreedListRoute)
    val onNavigate: (androidx.navigation3.runtime.NavKey) -> Unit = { backStack.add(it) }

    val listVm: BreedListViewModel = viewModel(factory = vmFactory { BreedListViewModel(repo) })
    val favVm: FavoritesViewModel = viewModel(factory = vmFactory { FavoritesViewModel(repo) })

    NavDisplay(
        modifier = modifier,
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<BreedListRoute> {
                BreedListScreen(
                    viewModel = listVm,
                    onBreedClick = { onNavigate(BreedDetailRoute(it)) },
                    onFavoritesClick = { onNavigate(FavoritesRoute) }
                )
            }
            entry<BreedDetailRoute> { key ->
                val breeds by listVm.breeds.collectAsState()
                val breed = breeds.find { it.id == key.breedId }
                BreedDetailScreen(
                    breed = breed,
                    onToggleFavorite = { breed?.let { listVm.toggleFavorite(it) } },
                    onBack = { backStack.removeLastOrNull() }
                )
            }
            entry<FavoritesRoute> {
                FavoritesScreen(
                    viewModel = favVm,
                    onBreedClick = { onNavigate(BreedDetailRoute(it)) },
                    onBack = { backStack.removeLastOrNull() }
                )
            }
        }
    )
}


fun <T : ViewModel> vmFactory(create: () -> T): ViewModelProvider.Factory =
    object : ViewModelProvider.Factory {
        override fun <VM : ViewModel> create(modelClass: Class<VM>): VM = create() as VM
    }