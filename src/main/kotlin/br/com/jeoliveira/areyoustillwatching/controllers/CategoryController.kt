package br.com.jeoliveira.areyoustillwatching

import br.com.jeoliveira.areyoustillwatching.services.CategoryService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/categories")
class CategoryController(
    private val categoryService: CategoryService
) {

    @GetMapping("/")
    fun getAllCategories(): ResponseEntity<Iterable<Category>> {
        val categories = categoryService.getAllCategories();
        return ResponseEntity(categories, HttpStatus.OK)
    }

}