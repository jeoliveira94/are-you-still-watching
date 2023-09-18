package br.com.jeoliveira.areyoustillwatching

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size


@Entity
@Table(name = "category")
class Category(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @field:NotBlank
    @field:Size(max = 150)
    var title: String,

    @field:NotBlank
    @field:Size(max = 10)
    var color: String
)
