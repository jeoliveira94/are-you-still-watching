package br.com.jeoliveira.areyoustillwatching.repositories

import br.com.jeoliveira.areyoustillwatching.models.Playlist
import org.springframework.data.repository.CrudRepository

interface PlaylistRepository : CrudRepository<Playlist, Long>
