package com.app.quiz.signin.model

data class SignInRequest(var name: String?, var email: String?, var photoUrl: String?="", var deviceId: String?){
    constructor() : this("","","","")
}