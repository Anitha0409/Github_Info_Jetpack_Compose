package com.githubinfo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun RepoDetailScreen( userViewModel: UserViewModel = viewModel(), ownerName: String?,repoName:String?){
    val repoDetailState by userViewModel.repoData.observeAsState()
        Column (modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally)
        {

            when{
                repoDetailState?.loading  == true ->{
                    Text("Loading...")
                }

                repoDetailState?.repoData!=null -> {
                    repoDetailState?.let { repoDetailData->
                        Text("Ownername: ${ownerName}", style = TextStyle(fontWeight = FontWeight.Bold), fontSize = 20.sp)
                        Text("Reponame: ${repoName}", style = TextStyle(fontWeight = FontWeight.Bold), fontSize = 20.sp)
                        Text("Updated at: ${repoDetailData.repoData?.updated_at}",
                            color = Color.Black,
                            style = androidx.compose.ui.text.TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            ),
                            modifier = Modifier.padding(top = 4.dp))
                        Text("Forks: ${repoDetailData?.repoData?.forks}",
                            color = Color.Black,
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            ),
                            modifier = Modifier.padding(top = 4.dp)
                        )
                        Text("Stargazers count: ${repoDetailData?.repoData?.stargazers_count}",
                            color = Color.Black,
                            style = TextStyle(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
                repoDetailState?.error!=null ->{
                    Text("Error: ${repoDetailState?.error}")
                }
            }
        }
    }




