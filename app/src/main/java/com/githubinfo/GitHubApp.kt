package com.githubinfo

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

@Composable
fun AppNavigation(navController: NavHostController){
    val userViewModel: UserViewModel = viewModel()


    NavHost(navController = navController, startDestination ="usersearchscreen" ) {
        composable("usersearchscreen"){
            UserSearchScreen(
                userViewModel = userViewModel,
                onRepoClick = {ownerName, repoName ->
                    userViewModel.getRepoDetails(ownerName, repoName)
                    navController.navigate("repodetailsscreen/$ownerName/$repoName")
                }
            )
        }
        composable("repodetailsscreen/{ownerName}/{repoName}",
            arguments = listOf(
                    navArgument("ownerName"){type= NavType.StringType},
                    navArgument("repoName"){type = NavType.StringType}

            )
        ){backStackEntry ->
            val ownerName = backStackEntry.arguments?.getString("ownerName")
            val repoName = backStackEntry.arguments?.getString("repoName")
            if(ownerName!=null && repoName!=null){
                userViewModel.getRepoDetails(ownerName, repoName)
                RepoDetailScreen(userViewModel = userViewModel, ownerName = ownerName, repoName = repoName)
            }

        }
    }
}
