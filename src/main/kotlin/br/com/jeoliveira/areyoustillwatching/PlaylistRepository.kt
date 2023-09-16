package br.com.jeoliveira.areyoustillwatching

import org.springframework.data.repository.CrudRepository

interface PlaylistRepository : CrudRepository<Playlist, Long>