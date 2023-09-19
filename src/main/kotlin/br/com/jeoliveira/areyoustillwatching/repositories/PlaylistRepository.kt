package br.com.jeoliveira.areyoustillwatching.repositories

import br.com.jeoliveira.areyoustillwatching.models.Playlist
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.CrudRepository

interface PlaylistRepository : JpaRepository<Playlist, Long> {
    fun findByCategoryId(categoryId: Long): List<Playlist>
}
