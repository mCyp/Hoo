package com.example.composehoo.ui.page.login

enum class LoginType {

    WelCome,
    Login,
    Register;

    companion object {
        fun fromRoute(route: String?): LoginType{
            return when(route?.substringBefore("/")){
                WelCome.name -> WelCome
                Login.name -> Login
                Register.name -> Register
                else -> WelCome
            }
        }
    }
}