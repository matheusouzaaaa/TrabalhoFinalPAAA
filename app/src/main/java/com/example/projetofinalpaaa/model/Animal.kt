package com.example.projetofinalpaaa.model

import com.google.firebase.firestore.Exclude
import java.io.Serializable

data class Animal(val nome: String?=null, val idade: Int?=null, val tipo: String?=null, @Exclude val key: String? = null):Serializable