package com.example.dogbreeds.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.dogbreeds.domain.model.Breed
import com.example.dogbreeds.ui.viewmodel.BreedListViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BreedListScreen(
    viewModel: BreedListViewModel,
    onBreedClick: (Int) -> Unit,
    onFavoritesClick: () -> Unit
) {

    println("DEBUG: BreedListScreen composed")   // ← ЭТА СТРОКА
    val breeds by viewModel.breeds.collectAsState()
    val query by viewModel.searchQuery.collectAsState()
    println("DEBUG: breeds size = ${breeds.size}")   // ← И ЭТА
    LaunchedEffect(Unit) {
        println("DEBUG: LaunchedEffect fired, calling refresh()")
        viewModel.refresh()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dog Breeds") },
                actions = {
                    TextButton(onClick = onFavoritesClick) { Text("★ Избранное") }
                }
            )
        }
    ) { padding ->
        Column(Modifier.padding(padding)) {
            OutlinedTextField(
                value = query,
                onValueChange = viewModel::onSearchQueryChange,
                label = { Text("Поиск по названию") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )

            LazyColumn {
                items(breeds, key = { it.id }) { breed ->
                    BreedCard(breed, onClick = { onBreedClick(breed.id) })
                }
            }
        }
    }
}

@Composable
fun BreedCard(breed: Breed, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = breed.imageUrl,
                contentDescription = breed.name,
                modifier = Modifier.size(80.dp)
            )
            Column(Modifier.padding(8.dp)) {
                Text(breed.name, style = MaterialTheme.typography.titleMedium)
                Text(breed.temperament, maxLines = 2)
            }
        }
    }
}

@Composable
fun BreedDetailScreen(
    breed: Breed?,
    onToggleFavorite: () -> Unit,
    onBack: () -> Unit
) {
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Button(onClick = onBack) { Text("← Назад") }
        if (breed == null) {
            Text("Загрузка...")
            return
        }
        AsyncImage(
            model = breed.imageUrl,
            contentDescription = breed.name,
            modifier = Modifier.fillMaxWidth().height(250.dp)
        )
        Text(breed.name, style = MaterialTheme.typography.headlineMedium)
        Text("Темперамент: ${breed.temperament}")
        Text("Происхождение: ${breed.origin ?: "—"}")
        Text("Живёт: ${breed.lifeSpan}")
        Text(breed.description ?: "Описание отсутствует")
        Spacer(Modifier.height(16.dp))
        Button(onClick = onToggleFavorite) {
            Text(if (breed.isFavorite) "Убрать из избранного" else "В избранное")
        }
    }
}

@Composable
fun FavoritesScreen(
    viewModel: FavoritesViewModel,
    onBreedClick: (Int) -> Unit,
    onBack: () -> Unit
) {
    val favorites by viewModel.favorites.collectAsState()

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Button(onClick = onBack) { Text("← Назад") }
        Text("Избранное", style = MaterialTheme.typography.headlineMedium)
        if (favorites.isEmpty()) {
            Text("Пока пусто")
        } else {
            LazyColumn {
                items(favorites, key = { it.id }) { breed ->
                    BreedCard(breed, onClick = { onBreedClick(breed.id) })
                }
            }
        }
    }
}