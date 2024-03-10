package com.trainsmart.repository.clouddatabase.model

import com.trainsmart.repository.localdatabase.entity.Exercise

data class WorkoutSheetModel ( // Todo (se alterar aqui terá que alterar tbm os campos já salvos na
// base, pois caso contrario não serão identificados, e portanto as variaveis não serão iniciadas)
    val nome: String = "",
    val objetivo: String = "",
    val data: String = "",
    val personal: String = "",
    val exercises: List<Exercise> = listOf()
)