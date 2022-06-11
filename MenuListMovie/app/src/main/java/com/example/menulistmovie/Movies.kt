package com.example.menulistmovie

data class Movies(
    val id: String,
    val title: String,
    val poster: String,
    val plot: String,
    val year: String
){
    constructor() : this("", "", "", "", "") {
    }
}