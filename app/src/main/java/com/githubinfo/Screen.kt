package com.githubinfo

sealed class Screen(val route:String) {

    object UserSearchScreen: Screen("usersearchscreen")
    object RepoDetailsScreen: Screen("repodetailsscreen")

}