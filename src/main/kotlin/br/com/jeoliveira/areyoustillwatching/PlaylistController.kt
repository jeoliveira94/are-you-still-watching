package br.com.jeoliveira.areyoustillwatching

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/playlists")
class PlaylistController(private val playlistRepository: PlaylistRepository) {

    @GetMapping("/")
    fun getAllPlaylists(): Iterable<Playlist> {
        return playlistRepository.findAll()
    }
}