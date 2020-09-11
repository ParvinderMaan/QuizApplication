package com.app.armygyan.signin.model

data class SignInRequest(var name: String?, var email: String?, var photoUrl: String?="", var deviceId: String?){
    constructor() : this("","","","")
}