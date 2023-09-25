package br.com.jeoliveira.areyoustillwatching.repositories

import br.com.jeoliveira.areyoustillwatching.models.Playlist
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.CrudRepository

interface PlaylistRepository : JpaRepository<Playlist, Long> {
    fun findByCategoryId(categoryId: Long, pageable: Pageable): List<Playlist>
    fun findByName(name: String, pageable: Pageable): List<Playlist>
}
