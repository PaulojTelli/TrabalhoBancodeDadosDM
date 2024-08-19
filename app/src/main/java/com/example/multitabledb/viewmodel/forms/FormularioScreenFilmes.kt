package com.example.multitabledb.viewmodel.forms

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.multitabledb.data.Movie
import com.example.multitabledb.viewmodel.AppViewModel

@Composable
fun FormularioScreenFilmes(navController: NavController, viewModel: AppViewModel) {
    // Remember state to store movie name input
    val movieNameState = remember { mutableStateOf("") }

    // Layout for the form screen
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .border(2.dp, Color.Blue, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFFAFAFA)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Display the form title
            Text(
                text = "Adicionar Novo Filme",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF3F51B5),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Input field for movie name
            OutlinedTextField(
                value = movieNameState.value,
                onValueChange = { movieNameState.value = it },
                label = { Text("Nome do Filme") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
                    .border(1.dp, Color.Gray, CircleShape),
                shape = CircleShape
            )

            // Button to save the movie
            Button(
                onClick = {
                    if (movieNameState.value.isNotEmpty()) {
                        viewModel.insertMovie(Movie(name = movieNameState.value))
                        navController.popBackStack() // Return to the previous screen
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                shape = CircleShape
            ) {
                Text(text = "Salvar", fontSize = 18.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}
