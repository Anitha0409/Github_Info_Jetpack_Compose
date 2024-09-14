package com.githubinfo

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter


@Composable
fun UserSearchScreen(userViewModel: UserViewModel = viewModel(), onRepoClick: (ownerName: String, repoName: String) -> Unit){

    var username by remember{ mutableStateOf("") }
    val userState by userViewModel.users.observeAsState()
    val repoState by userViewModel.repos.observeAsState()
    val keyBoardController = LocalSoftwareKeyboardController.current

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
        )
    {
        TextField(
            value = username,
            onValueChange = {
            username = it
        },
            label = { Text(text = "Enter your username")},
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions (
                onDone = {
                    if(username.isNotEmpty()) {
                        userViewModel.setUserName(username)
                    }
                    keyBoardController?.hide()
                }
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            if(username.isNotEmpty()) {
                userViewModel.setUserName(username)
            }
        }
        )
        {
            Text("Search")
        }
        Spacer(modifier = Modifier.height(16.dp))

        when{
            userState?.loading == true -> {
                Box(modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Loading...")
                }

            }
            userState?.user!=null ->{
                Box(Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center)
                {
                    Column (modifier = Modifier.padding(8.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    )
                    {
                        userState?.let { userData ->
                            Image(painter = rememberAsyncImagePainter(userData.user?.avatar_url), contentDescription ="Profile Picture")
                            Text(text = "Username: ${userData.user?.login}",
                                color = Color.Black,
                                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 24.sp),
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                        if(repoState.isNullOrEmpty()){
                            Text("No repository found or error in fetching repo info", color = Color.Red)
                        }else {
                            Text(text = "Repositories:",
                                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp),
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                                items(repoState ?: emptyList()) { repo ->
                                    repoItem(repo, userState?.user?.login ?:"",onRepoClick)
                                }
                            }
                        }
                    }
                }

            }
          userState?.error!=null->{
              Text("Error: ${userState?.error}", color = Color.Red)
          }

        }

    }

}

@Composable
fun repoItem(repo:RepoData, ownerName:String, onRepoClick: (ownerName: String,repoName:String) ->Unit){
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
        .clickable {
            onRepoClick(ownerName, repo.name)
        },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally)
    {
        Text(text = repo.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Text(text = repo.description ?: "No description available", fontSize = 14.sp)
    }
}