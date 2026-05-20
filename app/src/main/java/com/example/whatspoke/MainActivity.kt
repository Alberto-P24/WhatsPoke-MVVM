package com.example.whatspoke

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.whatspoke.ui.navigation.Routes
import com.example.whatspoke.ui.navigation.WhatsPokeNavGraph
import com.example.whatspoke.ui.theme.WhatsPokeTheme
import com.example.whatspoke.ui.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val profileViewModel: ProfileViewModel by viewModels()

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeMode by profileViewModel.themeMode.collectAsStateWithLifecycle()
            WhatsPokeTheme(themeMode = themeMode) {
                val windowSize = calculateWindowSizeClass(this)
                val navController = rememberNavController()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        val currentDestination = navBackStackEntry?.destination

                        NavigationBar {
                            val items = listOf(
                                Triple(Routes.ElemList, stringResource(R.string.nav_home), Icons.Default.Home),
                                Triple(Routes.FavList, stringResource(R.string.nav_favorites), Icons.Default.Star),
                                Triple(Routes.Profile, stringResource(R.string.nav_profile), Icons.Default.Person),
                                Triple(Routes.About, stringResource(R.string.nav_about), Icons.Default.Info)
                            )

                            items.forEach { (route, label, icon) ->
                                NavigationBarItem(
                                    icon = { Icon(icon, contentDescription = label) },
                                    label = { Text(label) },
                                    selected = currentDestination?.hierarchy?.any { it.route == route } == true,
                                    onClick = {
                                        navController.navigate(route) {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    WhatsPokeNavGraph(
                        windowSize = windowSize.widthSizeClass,
                        navController = navController,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
