package br.com.jeoliveira.areyoustillwatching.services

import br.com.jeoliveira.areyoustillwatching.Category
import br.com.jeoliveira.areyoustillwatching.CategoryRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.Optional


@Service
class CategoryService(@Autowired private val categoryRepository: CategoryRepository) {

    fun getAllCategories(): Iterable<Category> {
        return categoryRepository.findAll()
    }

    fun getCategoryById(id: Long): Optional<Category> {
        return categoryRepository.findById(id)
    }

    fun createCategory(category: Category): Category {
        return categoryRepository.save(category)
    }

}
