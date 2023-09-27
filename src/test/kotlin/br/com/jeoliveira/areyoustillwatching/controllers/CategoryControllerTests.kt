package br.com.jeoliveira.areyoustillwatching.controllers

import br.com.jeoliveira.areyoustillwatching.Category
import br.com.jeoliveira.areyoustillwatching.CategoryController
import br.com.jeoliveira.areyoustillwatching.models.Playlist
import br.com.jeoliveira.areyoustillwatching.services.CategoryService
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


@WebMvcTest(CategoryController::class)
class CategoryControllerTests {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var categoryService: CategoryService

    @MockkBean
    private lateinit var playlistService: PlaylistService

    companion object {
        fun categoriesFromResponse(response: String): List<Category> {
            val gson = Gson()
            val categoriesType = object : TypeToken<List<Category>>() {}.type
            return gson.fromJson(response, categoriesType)
        }
    }

    @Test
    fun `should return a list of categories`() {
        // Arrange
        val expectedCategory = listOf(
            Category(1L, "title 1", "color 1"),
            Category(2L, "title 2", "color 2")
        )

        // Mock the behavior of the playlistService
        every { categoryService.getAllCategories(any()) } returns expectedCategory

        // Act and Assert
        val response = mockMvc.perform(get("/v1/categories/"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn().response.contentAsString

        val resultCategories = categoriesFromResponse(response)

        assertContentEquals(expectedCategory, resultCategories)
        verify(exactly = 1) { categoryService.getAllCategories(any()) }
    }


    @Test
    fun `Should return category by id`() {
        // Arrange
        val categoryId = 1L
        val category = Category(1L, "title 1", "color 1");

        // Mock the behavior of your service method
        every { categoryService.getCategoryById(categoryId) } returns Optional.of(category)

        // Act and Assert
        mockMvc.perform(get("/v1/categories/$categoryId")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(categoryId.toInt()))
            .andExpect(jsonPath("$.title").value(category.title))
            .andExpect(jsonPath("$.color").value(category.color))

        verify(exactly = 1) { categoryService.getCategoryById(categoryId) }
    }

    @Test
    fun `Should return playlists by category id`() {
        // Arrange

        val categoryId = 1L
        val playlists = listOf(
            Playlist(id = 1L, name = "Test Playlist 1", description = "Test Description 2", url = "https://example.com1", categoryId = categoryId),
            Playlist(id = 2L, name = "Test Playlist 2", description = "Test Description 1", url = "https://example.com2", categoryId = categoryId),
        )

        // Mock the behavior of your service method
        every { playlistService.getPlaylistsByCategoryId(any(), any()) } returns playlists

        // Act and Assert
        val response = mockMvc.perform(get("/v1/categories/$categoryId/playlists")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn().response.contentAsString

        val objectMapper = jacksonObjectMapper()
        val resultPlaylist: List<Playlist> = objectMapper.readValue(response)

        assertContentEquals(playlists, resultPlaylist)
        verify(exactly = 1) { playlistService.getPlaylistsByCategoryId(categoryId, any()) }
    }

    @Test
    fun `Should return a new category`() {
        // Arrange
        val newCategory = Category(id = 1L, title = "New Category", color = "New Color")

        // Mock the behavior of your service method
        every { categoryService.createCategory(any()) } returns newCategory

        // Act and Assert
        mockMvc.perform(post("/v1/categories/")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"title\":\"New Category\",\"color\":\"New Color\"}")
        )
            .andExpect(status().isCreated)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.title").value(newCategory.title))
            .andExpect(jsonPath("$.color").value(newCategory.color))

        verify(exactly = 1) { categoryService.createCategory(any()) }
    }

    @Test
    fun `Should return updated category`() {
        // Arrange
        val categoryId = 1L
        val updatedPlaylist = Category(id = categoryId, title = "Update Category", color = "Update Color")

        // Mock the behavior of your service method
        every { categoryService.updateCategory(any(), any()) } returns Optional.of(updatedPlaylist)

        // Act and Assert
        mockMvc.perform(put("/v1/categories/$categoryId")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"title\":\"New Category\",\"color\":\"New Color\"}")
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.title").value(updatedPlaylist.title))
            .andExpect(jsonPath("$.color").value(updatedPlaylist.color))

        verify(exactly = 1) { categoryService.updateCategory(any(), any()) }
    }

    @Test
    fun `Should delete a category`() {
        // Arrange
        val categoryId = 1L

        // Mock the behavior of your service method
        every { categoryService.deleteCategoryById(categoryId) } returns Unit

        // Act and Assert
        mockMvc.perform(delete("/v1/categories/$categoryId"))
            .andExpect(status().isOk)

        verify(exactly = 1) { categoryService.deleteCategoryById(any()) }
    }
}