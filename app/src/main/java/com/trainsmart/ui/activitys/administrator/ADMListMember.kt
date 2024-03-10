package com.trainsmart.ui.activitys.administrator

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.trainsmart.ui.viewmodel.ListMemberViewModel

@Composable
fun ADMListMember(
    padding: PaddingValues,
    listMemberViewModel: ListMemberViewModel
) {

    val listUsers by listMemberViewModel.listUsers.collectAsState()

    LaunchedEffect(key1 = Unit) {
        listMemberViewModel.loadUsers()
    }

    LazyColumn(
        modifier = Modifier
            .padding(padding)
            .padding(10.dp)
    ) {
        item {
            Text(text = "Selecione o aluno:")

            listUsers.forEach { user ->

                OutlinedButton(
                    onClick = {
                        listMemberViewModel.onUserSelected(user)
                    },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(user.nome)
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}