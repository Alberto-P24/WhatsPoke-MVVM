package com.example.whatspoke.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.whatspoke.R
import com.example.whatspoke.ui.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    val userName by profileViewModel.userName.collectAsStateWithLifecycle()
    val themeMode by profileViewModel.themeMode.collectAsStateWithLifecycle()
    var nameInput by remember { mutableStateOf(userName) }
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Surface(
            modifier = Modifier.size(100.dp),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primaryContainer,
            shadowElevation = 4.dp
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = stringResource(R.string.nav_profile),
                modifier = Modifier.padding(24.dp),
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = userName,
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = nameInput,
            onValueChange = { nameInput = it },
            label = { Text(stringResource(R.string.user_name_label)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                profileViewModel.saveUserName(nameInput)
                Toast.makeText(context, R.string.user_name_saved, Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(stringResource(R.string.save))
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.theme_label),
            style = MaterialTheme.typography.titleMedium
        )

        val themeOptions = listOf(
            stringResource(R.string.theme_light) to "light",
            stringResource(R.string.theme_dark) to "dark",
            stringResource(R.string.theme_system) to "system"
        )

        themeOptions.forEach { (label, value) ->
            Column(modifier = Modifier.fillMaxWidth()) {
                androidx.compose.foundation.layout.Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = themeMode == value,
                        onClick = {
                            profileViewModel.saveThemeMode(value)
                            Toast.makeText(context, R.string.theme_saved, Toast.LENGTH_SHORT).show()
                        }
                    )
                    Text(text = label, style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}
