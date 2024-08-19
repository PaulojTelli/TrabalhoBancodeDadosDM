package com.example.multitabledb

import FormularioAssociarFilmeComAtor
import com.example.multitabledb.viewmodel.forms.FormularioScreenFilmes

import com.example.multitabledb.viewmodel.forms.FormularioScreenFilmes
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.multitabledb.data.Actor
import com.example.multitabledb.data.ActorWithMovies
import com.example.multitabledb.data.Movie
import com.example.multitabledb.data.MovieWithActors
import com.example.multitabledb.ui.theme.MultiTableDBTheme
import com.example.multitabledb.viewmodel.AppViewModel
import com.example.multitabledb.viewmodel.forms.FormularioScreenAtor

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MultiTableDBTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFFAFAFA) // Light gray background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen() {
    val viewModel: AppViewModel = viewModel(factory = AppViewModel.Factory)
    val movies by viewModel.movies.collectAsState()
    val actors by viewModel.actors.collectAsState()
    val navController = rememberNavController()

    Scaffold(
        floatingActionButton = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                FloatingActionButton(
                    onClick = { navController.navigate("form_filmes") },
                    shape = CircleShape,
                    containerColor = Color(0xFF00796B) // Teal color
                ) {
                    Text(text = "+ Filme", color = Color.White, fontWeight = FontWeight.Bold)
                }
                FloatingActionButton(
                    onClick = { navController.navigate("form_atores") },
                    shape = CircleShape,
                    containerColor = Color(0xFF1976D2) // Blue color
                ) {
                    Text(text = "+ Ator", color = Color.White, fontWeight = FontWeight.Bold)
                }
                FloatingActionButton(
                    onClick = { navController.navigate("form_associar") },
                    shape = CircleShape,
                    containerColor = Color(0xFFD32F2F) // Red color
                ) {
                    Text(text = "Associar", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        },
        bottomBar = {
            BottomAppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                containerColor = Color(0xFF004D40) // Dark teal color
            ) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Gerenciador de Filmes", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = "movies_actors",
            modifier = Modifier.padding(padding)
        ) {
            composable("movies_actors") {
                MoviesActorsScreen(
                    movies = movies,
                    actors = actors,
                    navController = navController,
                    onMovieSelection = viewModel::selectMovie,
                    onActorSelection = viewModel::selectActor
                )
            }
            composable("movie") {
                MovieDetailsScreen(
                    navController = navController,
                    viewModel = viewModel,
                    movieWithActors = viewModel.movieWithActors.collectAsState().value
                )
            }
            composable("actor") {
                ActorDetailsScreen(
                    navController = navController,
                    viewModel = viewModel,
                    actorWithMovies = viewModel.actorWithMovies.collectAsState().value
                )
            }
            composable("form_filmes") {
                FormularioScreenFilmes(navController = navController, viewModel = viewModel)
            }
            composable("form_atores") {
                FormularioScreenAtor(navController = navController, viewModel = viewModel)
            }
            composable("form_associar") {
                FormularioAssociarFilmeComAtor(navController = navController , viewModel =  viewModel)
            }
        }
    }
}

@Composable
fun MoviesActorsScreen(
    movies: List<Movie>,
    actors: List<Actor>,
    navController: NavController,
    onMovieSelection: (Movie) -> Unit,
    onActorSelection: (Actor) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF4CAF50),
                        Color(0xFF8BC34A)
                    )
                )
            )
            .padding(16.dp)
    ) {
        Text(
            text = "Associando Filmes",
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 16.dp)
        )
        Row(modifier = Modifier.weight(1f)) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            ) {
                Text(
                    text = "Filmes",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                LazyColumn {
                    items(movies) { movie ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable {
                                    onMovieSelection(movie)
                                    navController.navigate("movie")
                                },
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(2.dp, Color.White),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFCDDC39)) // Lime color
                        ) {
                            Text(
                                text = movie.name,
                                color = Color.Black,
                                fontSize = 18.sp,
                                modifier = Modifier.padding(16.dp),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            ) {
                Text(
                    text = "Atores",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                LazyColumn {
                    items(actors) { actor ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable {
                                    onActorSelection(actor)
                                    navController.navigate("actor")
                                },
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(2.dp, Color.White),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEB3B)) // Yellow color
                        ) {
                            Text(
                                text = actor.name,
                                color = Color.Black,
                                fontSize = 18.sp,
                                modifier = Modifier.padding(16.dp),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MovieDetailsScreen(
    navController: NavController,
    movieWithActors: MovieWithActors,
    viewModel: AppViewModel
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFEEEEEE) // Light gray background
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = movieWithActors.movie.name,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF3E2723) // Dark brown
            )
            Spacer(modifier = Modifier.height(10.dp))
            LazyColumn {
                items(movieWithActors.actors) { actor ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF8D6E63)) // Brown color
                    ) {
                        Text(
                            text = actor.name,
                            fontSize = 18.sp,
                            color = Color.White,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }

            Button(
                onClick = {
                    viewModel.deleteMovie(movieWithActors.movie) // Chama a função deleteMovie com o ID do filme

                    navController.popBackStack() // Retorna para a tela anterior
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFB71C1C), // Dark red
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .padding(top = 16.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(text = "Excluir ${movieWithActors.movie.name}")
            }
        }
    }
}


@Composable
fun ActorDetailsScreen(
    actorWithMovies: ActorWithMovies,
    viewModel: AppViewModel,
    navController: NavController
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFEEEEEE) // Light gray background
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = actorWithMovies.actor.name,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF3E2723) // Dark brown
            )
            Spacer(modifier = Modifier.height(10.dp))
            LazyColumn {
                items(actorWithMovies.movies) { movie ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF6D4C41)) // Brown color
                    ) {
                        Text(
                            text = movie.name,
                            fontSize = 18.sp,
                            color = Color.White,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }

            Button(
                onClick = {
                    viewModel.deleteActor(actorWithMovies.actor)
                    navController.popBackStack()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFB71C1C), // Dark red
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .padding(top = 16.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(text = "Excluir ${actorWithMovies.actor.name}")
            }
        }
    }
}
