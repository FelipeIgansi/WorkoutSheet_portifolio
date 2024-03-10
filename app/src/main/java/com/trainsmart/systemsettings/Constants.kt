package com.trainsmart.systemsettings

class Constants {
    companion object{
        const val AUTHKEY = "AuthenticationStage"
        const val LOGIN = "Login"
        const val ENTER = "Logar"
        const val REGISTRATION = "Cadastrar"
        const val REGISTER = "Cadastro"
        const val CONTINUE = "Prosseguir"
        const val CREATEACCOUNT = "Criar conta"
        const val EMAIL = "E-mail"
        const val NAME = "Nome"
        const val FULLNAME = "Nome Completo"
        const val PHONE = "Telefone"
        const val PASSWORD = "Senha"
        const val RE_PASS = "Repita a Senha"
        const val USERTABLENAME = "Usuarios"
        const val ROLE = "papel"
        const val PERSONAL = "Personal"
        const val STUDENT = "Aluno"
        const val SELECTED_NAME_STUDENT = "AlunoSelecionado"
        const val SELECTED_EMAIL_STUDENT = "EmailAlunoSelecionado"
    }

    object Database{
        const val WORKOUTSHEETCOLLECTION = "planilhasdetreino"
        const val SELECTED_ID_FROM_EXERCISE_TO_EDIT = "IDExerciseToEdit"
        const val FKID_USER = "fk_id"
    }
    /**
     * TODO( há um bug no Android Studio, que coloca como se as contants não estivessem em uso, mas estão! Dentro de um .filter .map)
     * */
    object ExercisesOptions {
        const val SELECT_WORKOUT = "Selecione o treino *"
        const val BACK = "Costas"
        const val ARM = "Braço"
        const val LEG = "Pernas"
        const val SHOULDER = "Ombros"
        const val CHEST = "Peito"
        const val Other = "Outro"
    }
}
