package br.com.jeoliveira.areyoustillwatching

import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/playlists")
class PlaylistController(private val playlistRepository: PlaylistRepository) {

    @GetMapping("/")
    fun getAllPlaylists(): Iterable<Playlist> {
        return playlistRepository.findAll()
    }

    @GetMapping("/{id}")
    fun getPlaylistById(@PathVariable id: Long): Playlist {
        return playlistRepository.findById(id).orElseThrow { NoSuchElementException("Playlist not found") }
    }

    @PostMapping("/")
    fun createPlaylist(@Valid @RequestBody playlist: Playlist): Playlist {
        return playlistRepository.save(playlist)
    }

    @PutMapping("/{id}")
    fun updatePlaylist(@PathVariable id: Long, @Valid @RequestBody updatedPlaylist: Playlist): Playlist {
        val existingPlaylist = playlistRepository.findById(id)
            .orElseThrow { NoSuchElementException("Playlist not found") }

        existingPlaylist.name = updatedPlaylist.name
        existingPlaylist.description = updatedPlaylist.description
        existingPlaylist.url = updatedPlaylist.url

        return playlistRepository.save(existingPlaylist)
    }

    @DeleteMapping("/{id}")
    fun deletePlaylist(@PathVariable id: Long) {
        playlistRepository.deleteById(id)
    }
}
