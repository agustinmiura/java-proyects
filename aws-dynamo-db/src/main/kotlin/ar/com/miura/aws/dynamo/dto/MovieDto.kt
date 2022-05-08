package ar.com.miura.aws.dynamo.dto

data class MovieDto(
    val id:String,
    val genre:String,
    val name:String,
    val year:Int
) {
}