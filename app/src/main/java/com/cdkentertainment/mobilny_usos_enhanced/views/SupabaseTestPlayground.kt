package com.cdkentertainment.mobilny_usos_enhanced.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cdkentertainment.mobilny_usos_enhanced.DatabaseSingleton
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest

@Preview
@Composable
fun SupabaseTestPlayground() {
    var outputText: String by remember { mutableStateOf("Output text") }
    var functionText: String by remember { mutableStateOf("Function text") }
    LaunchedEffect(Unit) {
        outputText = DatabaseSingleton.client.from("universities").select().data
        functionText = DatabaseSingleton.client.postgrest.rpc("universities_list").data
        // .rpc to fukcje
        // .data to wynik
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize().padding(12.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = outputText
            )
            Text(
                text = functionText
            )
        }
    }
}