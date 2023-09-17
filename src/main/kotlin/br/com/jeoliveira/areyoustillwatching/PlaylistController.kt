package br.com.jeoliveira.areyoustillwatching

import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/playlists")
class PlaylistController(private val playlistRepository: PlaylistRepository) {

    @GetMapping("/")
    fun getAllPlaylists(): ResponseEntity<Iterable<Playlist>> {
        return ResponseEntity.ok(playlistRepository.findAll())
    }

    @GetMapping("/{id}")
    fun getPlaylistById(@PathVariable id: Long): ResponseEntity<Any> {
        val playlist = playlistRepository.findById(id)
        if(playlist.isPresent) {
            return ResponseEntity.ok(playlist.get())
        }

        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body("Playlist with ID $id not found")
    }

    @PostMapping("/")
    fun createPlaylist(@Valid @RequestBody playlist: Playlist): ResponseEntity<Playlist> {
        return ResponseEntity(playlist, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    fun updatePlaylist(@PathVariable id: Long, @Valid @RequestBody updatedPlaylist: Playlist): ResponseEntity<Any> {
        val existingPlaylistOptional = playlistRepository.findById(id)

        if(existingPlaylistOptional.isPresent) {
            val existingPlaylist = existingPlaylistOptional.get();
            existingPlaylist.name = updatedPlaylist.name
            existingPlaylist.description = updatedPlaylist.description
            existingPlaylist.url = updatedPlaylist.url
            return ResponseEntity.ok(this.playlistRepository.save(existingPlaylist))
        }

        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body("Playlist with ID $id not found")
    }

    @DeleteMapping("/{id}")
    fun deletePlaylist(@PathVariable id: Long): ResponseEntity<Any> {
        playlistRepository.deleteById(id)
        return ResponseEntity.ok(null);
    }
}
