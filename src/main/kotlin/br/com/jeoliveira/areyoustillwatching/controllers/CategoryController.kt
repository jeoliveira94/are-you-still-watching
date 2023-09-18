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

    @GetMapping("/{id}")
    fun getCategoryById(@PathVariable id: Long): ResponseEntity<Any> {
        val category = categoryService.getCategoryById(id)
        if (category.isPresent) {
            return ResponseEntity(category.get(), HttpStatus.OK)
        }
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body("Category with ID $id not found")
    }

    @PostMapping("/")
    fun createCategory(@Valid @RequestBody category: Category): ResponseEntity<Category> {
        val createdCategory = categoryService.createCategory(category)
        return ResponseEntity(createdCategory, HttpStatus.CREATED)
    }

    @PutMapping("/{id}")
    fun updateCategory(@PathVariable id: Long, @Valid @RequestBody updatedCategory: Category): ResponseEntity<Any> {
        val updatedCategoryOptional = categoryService.updateCategory(id, updatedCategory)

        if (updatedCategoryOptional.isPresent) {
            return ResponseEntity(updatedCategoryOptional.get(), HttpStatus.OK)
        }

        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body("Category with ID $id not found")
    }

    }
}