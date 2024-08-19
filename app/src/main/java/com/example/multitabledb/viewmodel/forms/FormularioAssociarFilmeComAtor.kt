@file:OptIn(ExperimentalMaterial3Api::class)

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.multitabledb.data.Actor
import com.example.multitabledb.data.Movie
import com.example.multitabledb.data.MovieActor
import com.example.multitabledb.viewmodel.AppViewModel

@Composable
fun FormularioAssociarFilmeComAtor(navController: NavController, viewModel: AppViewModel) {
    // Collecting state for movies and actors from the ViewModel
    val movies by viewModel.movies.collectAsState()
    val actors by viewModel.actors.collectAsState()

    // State management for selected movie and actor
    val selectedMovie = remember { mutableStateOf<Movie?>(null) }
    val selectedActor = remember { mutableStateOf<Actor?>(null) }

    // State for managing dropdown visibility
    val movieDropdownExpanded = remember { mutableStateOf(false) }
    val actorDropdownExpanded = remember { mutableStateOf(false) }

    // Modern UI Layout
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .background(Color(0xFFF5F5F5)),
        shape = RoundedCornerShape(16.dp),
        shadowElevation = 8.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Associar Filme com Ator",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF3F51B5),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Dropdown for selecting a movie
            MovieDropdown(
                selectedMovie = selectedMovie.value,
                onMovieSelected = { selectedMovie.value = it },
                movieDropdownExpanded = movieDropdownExpanded.value,
                onDropdownExpandChange = { movieDropdownExpanded.value = it },
                movies = movies
            )

            // Dropdown for selecting an actor
            ActorDropdown(
                selectedActor = selectedActor.value,
                onActorSelected = { selectedActor.value = it },
                actorDropdownExpanded = actorDropdownExpanded.value,
                onDropdownExpandChange = { actorDropdownExpanded.value = it },
                actors = actors
            )

            // Button to associate the selected movie and actor
            Button(
                onClick = {
                    val selectedMovieId = selectedMovie.value?.id
                    val selectedActorId = selectedActor.value?.id

                    if (selectedMovieId != null && selectedActorId != null) {
                        viewModel.insertMovieActor(MovieActor(movieId = selectedMovieId, actorId = selectedActorId))
                        navController.popBackStack() // Navigate back after association
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3F51B5),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(50) // Rounded button shape
            ) {
                Text(text = "Associar", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDropdown(
    selectedMovie: Movie?,
    onMovieSelected: (Movie) -> Unit,
    movieDropdownExpanded: Boolean,
    onDropdownExpandChange: (Boolean) -> Unit,
    movies: List<Movie>
) {
    // Movie selection field
    OutlinedTextField(
        value = selectedMovie?.name ?: "Selecione um filme",
        onValueChange = {},
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth()
            .clickable { onDropdownExpandChange(true) },
        readOnly = true,
        label = { Text("Filme") },
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color(0xFF3F51B5),
            cursorColor = Color(0xFF3F51B5),
            focusedLabelColor = Color(0xFF3F51B5)
        )
    )

    // Movie dropdown menu
    DropdownMenu(
        expanded = movieDropdownExpanded,
        onDismissRequest = { onDropdownExpandChange(false) },
        modifier = Modifier.fillMaxWidth()
    ) {
        movies.forEach { movie ->
            DropdownMenuItem(
                text = { Text(movie.name) },
                onClick = {
                    onMovieSelected(movie)
                    onDropdownExpandChange(false)
                }
            )
        }
    }
}

@Composable
fun ActorDropdown(
    selectedActor: Actor?,
    onActorSelected: (Actor) -> Unit,
    actorDropdownExpanded: Boolean,
    onDropdownExpandChange: (Boolean) -> Unit,
    actors: List<Actor>
) {
    // Actor selection field
    OutlinedTextField(
        value = selectedActor?.name ?: "Selecione um ator",
        onValueChange = {},
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth()
            .clickable { onDropdownExpandChange(true) },
        readOnly = true,
        label = { Text("Ator") },
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color(0xFF3F51B5),
            cursorColor = Color(0xFF3F51B5),
            focusedLabelColor = Color(0xFF3F51B5)
        )
    )

    // Actor dropdown menu
    DropdownMenu(
        expanded = actorDropdownExpanded,
        onDismissRequest = { onDropdownExpandChange(false) },
        modifier = Modifier.fillMaxWidth()
    ) {
        actors.forEach { actor ->
            DropdownMenuItem(
                text = { Text(actor.name) },
                onClick = {
                    onActorSelected(actor)
                    onDropdownExpandChange(false)
                }
            )
        }
    }
}
