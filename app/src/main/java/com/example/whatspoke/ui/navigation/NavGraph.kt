package com.example.whatspoke.ui.navigation

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.whatspoke.R
import com.example.whatspoke.ui.screens.AboutScreen
import com.example.whatspoke.ui.screens.DetailFavScreen
import com.example.whatspoke.ui.screens.DetailItemScreen
import com.example.whatspoke.ui.screens.ElemListScreen
import com.example.whatspoke.ui.screens.FavListScreen
import com.example.whatspoke.ui.screens.ProfileScreen

// Routes
object Routes {
    const val ElemList = "elem_list"
    const val DetailItem = "detail_item/{pokemonId}"
    const val FavList = "fav_list"
    const val DetailFav = "detail_fav/{pokemonId}"
    const val Profile = "profile"
    const val About = "about"

    fun detailItem(id: Int) = "detail_item/$id"
    fun detailFav(id: Int) = "detail_fav/$id"
}

@Composable
fun WhatsPokeNavGraph(
    windowSize: androidx.compose.material3.windowsizeclass.WindowWidthSizeClass,
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Routes.ElemList,
        modifier = modifier
    ) {
        composable(Routes.ElemList) {
            if (windowSize == androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Medium ||
                windowSize == androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Expanded
            ) {
                MasterDetailElemScreen()
            } else {
                ElemListScreen(
                    onNavigateToDetail = { id -> navController.navigate(Routes.detailItem(id)) }
                )
            }
        }

        composable(
            route = Routes.DetailItem,
            arguments = listOf(navArgument("pokemonId") { type = NavType.IntType })
        ) { backStackEntry ->
            val pokemonId = backStackEntry.arguments?.getInt("pokemonId") ?: 0
            DetailItemScreen(
                pokemonId = pokemonId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Routes.FavList) {
            FavListScreen(
                onNavigateToDetail = { id -> navController.navigate(Routes.detailFav(id)) }
            )
        }

        composable(
            route = Routes.DetailFav,
            arguments = listOf(navArgument("pokemonId") { type = NavType.IntType })
        ) { backStackEntry ->
            val pokemonId = backStackEntry.arguments?.getInt("pokemonId") ?: 0
            DetailFavScreen(pokemonId = pokemonId)
        }

        composable(Routes.Profile) {
            ProfileScreen()
        }

        composable(Routes.About) {
            AboutScreen()
        }
    }
}

@Composable
fun MasterDetailElemScreen() {
    var selectedId by remember { mutableStateOf<Int?>(null) }

    Row(modifier = Modifier.fillMaxSize()) {
        ElemListScreen(
            onNavigateToDetail = { id -> selectedId = id },
            modifier = Modifier.weight(1f)
        )

        VerticalDivider(modifier = Modifier.width(1.dp))

        if (selectedId != null) {
            DetailItemScreen(
                pokemonId = selectedId!!,
                onNavigateBack = { selectedId = null },
                modifier = Modifier.weight(1.5f)
            )
        } else {
            androidx.compose.foundation.layout.Box(
                modifier = Modifier.weight(1.5f).fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(stringResource(R.string.select_placeholder))
            }
        }
    }
}
