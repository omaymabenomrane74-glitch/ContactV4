package com.example.contactv4.data

data class Contact(
    val id: Int = 0,
    val nom: String,
    val prenom: String,
    val telephone: String,
    val email: String = ""
)