package br.com.jeoliveira.areyoustillwatching.controllers

import br.com.jeoliveira.areyoustillwatching.models.Playlist
import br.com.jeoliveira.areyoustillwatching.services.PlaylistService
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
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

    companion object {
        fun playlistsFromResponse(response: String): List<Playlist> {
            val gson = Gson()
            val playlistListType = object : TypeToken<List<Playlist>>() {}.type
            return gson.fromJson(response, playlistListType)
        }
    }

    @Test
    fun `should return a list of playlists`() {
        // Arrange
        val expectedPlaylists = listOf(
            Playlist(1L, "Playlist 1", "Description 1", "URL 1", 1L),
            Playlist(2L, "Playlist 2", "Description 2", "URL 2", 2L)
            // Add more playlists as needed
        )

        // Mock the behavior of the playlistService
        every { playlistService.getAllPlaylists(any()) } returns expectedPlaylists

        // Act and Assert
        val response = mockMvc.perform(get("/v1/playlists/"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn().response.contentAsString

        val resultPlaylist = playlistsFromResponse(response)

        assertContentEquals(expectedPlaylists, resultPlaylist)
        verify(exactly = 1) { playlistService.getAllPlaylists(any()) }
        verify(exactly = 0) { playlistService.getPlaylistsByName(any(), any()) }
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
        every { playlistService.getPlaylistsByName(any(), any()) } returns expectedPlaylists

        // Act and Assert
        val response = mockMvc.perform(get("/v1/playlists/?name=$name"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn().response.contentAsString

        val objectMapper = jacksonObjectMapper()
        val resultPlaylist: List<Playlist> = objectMapper.readValue(response)

        assertContentEquals(expectedPlaylists, resultPlaylist)
        verify(exactly = 1) { playlistService.getPlaylistsByName(name, any()) }
        verify(exactly = 0) { playlistService.getAllPlaylists(any()) }

    }

    @Test
    fun `Should return playlist by id`() {
        // Arrange
        val playlistId = 1L
        val playlist = Playlist(id = playlistId, name = "Test Playlist", description = "Test Description", url = "https://example.com", categoryId = 2L)

        // Mock the behavior of your service method
        every { playlistService.getPlaylistById(playlistId) } returns Optional.of(playlist)

        // Act and Assert
        mockMvc.perform(get("/v1/playlists/$playlistId")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(playlistId.toInt()))
            .andExpect(jsonPath("$.name").value(playlist.name))
            .andExpect(jsonPath("$.description").value(playlist.description))
            .andExpect(jsonPath("$.url").value(playlist.url))
            .andExpect(jsonPath("$.categoryId").value(playlist.categoryId.toInt()))

        verify(exactly = 1) { playlistService.getPlaylistById(any<Long>()) }
    }

    @Test
    fun `Should return a new playlist`() {
        // Arrange
        val newPlaylist = Playlist(name = "New Playlist", description = "New Description", url = "https://example.com", categoryId = 3L)

        // Mock the behavior of your service method
        every { playlistService.createPlaylist(any<Playlist>()) } returns newPlaylist

        // Act and Assert
        mockMvc.perform(post("/v1/playlists/")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"name\":\"New Playlist\",\"description\":\"New Description\",\"url\":\"https://example.com\",\"categoryId\":3}")
        )
            .andExpect(status().isCreated)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.name").value(newPlaylist.name))
            .andExpect(jsonPath("$.description").value(newPlaylist.description))
            .andExpect(jsonPath("$.url").value(newPlaylist.url))
            .andExpect(jsonPath("$.categoryId").value(newPlaylist.categoryId))

        verify(exactly = 1) { playlistService.createPlaylist(any<Playlist>()) }
    }

    @Test
    fun `Should return updated playlists`() {
        // Arrange
        val playlistId = 1L
        val updatedPlaylist = Playlist(id = playlistId, name = "Updated Playlist", description = "Updated Description", url = "https://example.com/updated", categoryId = 4L)

        // Mock the behavior of your service method
        every { playlistService.updatePlaylist(any<Long>(), any<Playlist>()) } returns Optional.of(updatedPlaylist)

        // Act and Assert
        mockMvc.perform(put("/v1/playlists/$playlistId")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"name\":\"Updated Playlist\",\"description\":\"Updated Description\",\"url\":\"https://example.com/updated\",\"categoryId\":4}")
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(playlistId))
            .andExpect(jsonPath("$.name").value(updatedPlaylist.name))
            .andExpect(jsonPath("$.description").value(updatedPlaylist.description))
            .andExpect(jsonPath("$.url").value(updatedPlaylist.url))
            .andExpect(jsonPath("$.categoryId").value(updatedPlaylist.categoryId))

        verify(exactly = 1) { playlistService.updatePlaylist(any<Long>(), any<Playlist>()) }
    }

    @Test
    fun `Should delete a playlist`() {
        // Arrange
        val playlistId = 1L

        // Mock the behavior of your service method
        every { playlistService.deletePlaylistById(playlistId) } returns Unit

        // Act and Assert
        mockMvc.perform(delete("/v1/playlists/$playlistId"))
            .andExpect(status().isOk)

        verify(exactly = 1) { playlistService.deletePlaylistById(any<Long>()) }
    }

}
