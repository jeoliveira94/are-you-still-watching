package br.com.jeoliveira.areyoustillwatching.models

import jakarta.persistence.*
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

@Entity
@Table(name = "playlist")
class Playlist(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @field:NotBlank(message = "name is mandatory")
    @field:Size(max = 100)
    var name: String,

    @field:NotNull(message = "description can't be null")
    @field:Size(max = 250)
    var description: String,

    @field:NotBlank(message = "url is mandatory")
    @field:Size(max = 150)
    var url: String,

    @field:NotNull(message = "category id is mandatory")
    @field:Min(1, message = "category id should be greater than 1")
    var category_id: Long
)
