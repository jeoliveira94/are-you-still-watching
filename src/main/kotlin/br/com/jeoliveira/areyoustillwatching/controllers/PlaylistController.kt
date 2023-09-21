package br.com.jeoliveira.areyoustillwatching.controllers

import br.com.jeoliveira.areyoustillwatching.models.Playlist
import br.com.jeoliveira.areyoustillwatching.repositories.PlaylistRepository
import br.com.jeoliveira.areyoustillwatching.services.PlaylistService
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/playlists")
class PlaylistController(
    @Autowired private val playlistService: PlaylistService
) {

    @GetMapping("/")
    fun getAllPlaylists(@RequestParam(required = false) name: String?): ResponseEntity<Iterable<Playlist>> {
        val playlists =  if (name.isNullOrEmpty()) {
            playlistService.getAllPlaylists()
        } else {
            playlistService.getPlaylistsByName(name)
        }
        return ResponseEntity(playlists, HttpStatus.OK)
    }

    @GetMapping("/{id}")
    fun getPlaylistById(@PathVariable id: Long): ResponseEntity<Any> {
        val playlist = this.playlistService.getPlaylistById(id)
        if(playlist.isPresent) {
            return ResponseEntity.ok(playlist.get())
        }

        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body("Playlist with ID $id not found")
    }

    @PostMapping("/")
    fun createPlaylist(@Valid @RequestBody playlist: Playlist): ResponseEntity<Playlist> {
        return ResponseEntity(playlist, HttpStatus.CREATED)
    }

    @PutMapping("/{id}")
    fun updatePlaylist(@PathVariable id: Long, @Valid @RequestBody updatedPlaylist: Playlist): ResponseEntity<Any> {
        val updatedPlaylistOptional = this.playlistService.updatePlaylist(id, updatedPlaylist)

        if(updatedPlaylistOptional.isPresent) {
            return ResponseEntity.ok(updatedPlaylistOptional.get())
        }

        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body("Playlist with ID $id not found")
    }

    @DeleteMapping("/{id}")
    fun deletePlaylist(@PathVariable id: Long): ResponseEntity<Any> {
        this.playlistService.deletePlaylistById(id)
        return ResponseEntity.ok(null)
    }
}
