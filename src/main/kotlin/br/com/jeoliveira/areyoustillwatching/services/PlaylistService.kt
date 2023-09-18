package br.com.jeoliveira.areyoustillwatching.services

import br.com.jeoliveira.areyoustillwatching.models.Playlist
import br.com.jeoliveira.areyoustillwatching.repositories.PlaylistRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.Optional

@Service
class PlaylistService(
    @Autowired private val playlistRepository: PlaylistRepository
) {

    fun getAllPlaylists(): Iterable<Playlist> {
        return playlistRepository.findAll()
    }

    fun getPlaylistById(id: Long): Optional<Playlist> {
        return playlistRepository.findById(id);
    }

    fun createPlaylist(playlist: Playlist): Playlist {
        return playlistRepository.save(playlist)
    }

    fun updatePlaylist(id: Long, updatedPlaylist: Playlist): Optional<Playlist> {
        val existingPlaylistOptional = playlistRepository.findById(id)

        if(existingPlaylistOptional.isPresent) {
            val existingPlaylist = existingPlaylistOptional.get();
            existingPlaylist.name = updatedPlaylist.name
            existingPlaylist.description = updatedPlaylist.description
            existingPlaylist.url = updatedPlaylist.url
            return Optional.of(this.playlistRepository.save(existingPlaylist))
        }

        return Optional.empty()
    }

    fun deletePlaylistById(id: Long) {
        playlistRepository.deleteById(id)
    }
}