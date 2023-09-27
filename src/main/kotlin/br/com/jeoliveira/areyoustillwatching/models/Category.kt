package br.com.jeoliveira.areyoustillwatching

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size


@Entity
@Table(name = "category")
class Category(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @field:NotBlank(message = "url is mandatory")
    @field:Size(max = 150)
    var title: String,

    @field:NotBlank(message = "url is mandatory")
    @field:Size(max = 10)
    var color: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Category

        if (id != other.id) return false
        if (title != other.title) return false
        if (color != other.color) return false

        return true
    }
}
