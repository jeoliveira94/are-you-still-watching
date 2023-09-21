package br.com.jeoliveira.areyoustillwatching.controllers

import br.com.jeoliveira.areyoustillwatching.models.Playlist
import br.com.jeoliveira.areyoustillwatching.services.PlaylistService
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.util.*
import kotlin.test.assertContentEquals


@WebMvcTest(PlaylistController::class)
class PlaylistControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var playlistService: PlaylistService

    @Test
    fun `should return a list of playlists`() {
        // Arrange
        val expectedPlaylists = listOf(
            Playlist(1L, "Playlist 1", "Description 1", "URL 1", 1L),
            Playlist(2L, "Playlist 2", "Description 2", "URL 2", 2L)
            // Add more playlists as needed
        )

        // Mock the behavior of the playlistService
        every { playlistService.getAllPlaylists() } returns expectedPlaylists

        // Act and Assert
        val response = mockMvc.perform(get("/v1/playlists/"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn().response.contentAsString

        // Deserialize the JSON response into a list of Playlist objects
        val objectMapper = jacksonObjectMapper()
        val resultPlaylist: List<Playlist> = objectMapper.readValue(response)

        assertContentEquals(expectedPlaylists, resultPlaylist);
        verify(exactly = 1) { playlistService.getAllPlaylists() }
        verify(exactly = 0) { playlistService.getPlaylistsByName(any<String>()) }
    }

    @Test
    fun `should return a list of playlists by name`() {
        // Arrange
        val name = "workout"
        val expectedPlaylists = listOf(
            Playlist(1L, "Hip Hop Workout", "Description 1", "URL 1", 1L),
            Playlist(1L, "Rock Workout", "Description 2", "URL 2", 2L)
        )

        // Mock the behavior of the playlistService
        every { playlistService.getPlaylistsByName(any<String>()) } returns expectedPlaylists

        // Act and Assert
        val response = mockMvc.perform(get("/v1/playlists/?name=$name"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn().response.contentAsString

        // Deserialize the JSON response into a list of Playlist objects
        val objectMapper = jacksonObjectMapper()
        val resultPlaylist: List<Playlist> = objectMapper.readValue(response)

        assertContentEquals(expectedPlaylists, resultPlaylist)
        verify(exactly = 1) { playlistService.getPlaylistsByName(name) }
        verify(exactly = 0) { playlistService.getAllPlaylists() }

    }

}
