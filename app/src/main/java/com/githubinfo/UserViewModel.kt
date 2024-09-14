package com.githubinfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.HttpException

class UserViewModel:ViewModel() {

      private val _users = MutableLiveData(User(loading = true))
    val users: LiveData<User> get()  = _users

      private val _repos = MutableLiveData<List<RepoData>?>(null)
      val repos: LiveData<List<RepoData>?> = _repos

      private var _username: String = ""

     private var _ownerName: String =""
    private var _repoName: String = ""

    private val _repoData = MutableLiveData(Repo(loading = true))
    val repoData: LiveData<Repo?> get() = _repoData

      fun getRepoDetails(ownerName:String, repoName:String){
          _ownerName = ownerName
          _repoName = repoName
          fetchRepoDetails()
      }

    fun setUserName(userName: String){
        _username = userName
        fetchUserAndRepoDetails()
    }

    private fun fetchUserAndRepoDetails(){
        viewModelScope.launch {
            try {

                // Setting the loading state before making my API calls

                _users.value = User(loading = true)
                _repos.value = null

                // Making both the API calls simultaneously
                val userDeferred = async { userService.getUserDetails(_username) }
                val repoDeferred = async { userService.getUserRepoDetails(_username) }

                // Await response from the above calls
                val userResponse = userDeferred.await()
                val repoResponse = repoDeferred.await()

                //Update LiveData with successful responses

                _users.value = User(
                    user = UserData(
                        userResponse.avatar_url,
                        userResponse.login
                    ),
                    loading = false,
                    error = null
                )

                _repos.value = repoResponse

            }

            catch (e: HttpException) {
                if (e.code() == 404) {
                    // Handle 404 error specifically
                    _users.value = User(user = null, loading = false, error = "User not found, check the username and try again")
                    _repos.value = emptyList()
                } else {
                    // Handle other HTTP errors
                    _users.value = User(user = null, loading = false, error = "Error in fetching user information: ${e.message}")
                    _repos.value = emptyList()
                }
            } catch (e: Exception) {
                // General exception handling
                e.printStackTrace()

                _users.value = User(user = null, loading = false, error = "An unexpected error occurred: ${e.message}")
                _repos.value = emptyList()
            }
        }
    }

    private fun fetchRepoDetails(){
        viewModelScope.launch {
            try {
                // Setting the loading state before making any API calls
                _repoData.value = Repo(loading = true)
                // Making the API call
                val result = userService.getRepoDetails(_ownerName,_repoName)
                _repoData.value = Repo(
                    loading = false,
                    error = null,
                    repoData = RepoDetailData(
                        result.updated_at,
                        result.forks,
                        result.stargazers_count
                    )
                )

            } catch (e: HttpException){
                _repoData.value = Repo(
                    loading = false,
                    repoData = null,
                    error = "Http Error ${e.message}"
                )
            }
            catch (e: Exception){
                _repoData.value = Repo(
                    repoData = null,
                    loading = false,
                    error = "An unexpected error occurred {${e.message}}"
                )
            }

        }
    }

    data class User(
        var loading:Boolean,
        val user: UserData? = null,
        val error:String? = null
    )

    data class Repo(
        val loading: Boolean,
        val repoData: RepoDetailData? = null,
        val error: String? = null
    )
}

