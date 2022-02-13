package com.geekbrains.myweather_kotlin1.model

data class PhoneContact(
    var ID: String,
    var name: String,
    var numbers: MutableList<String>) {
}