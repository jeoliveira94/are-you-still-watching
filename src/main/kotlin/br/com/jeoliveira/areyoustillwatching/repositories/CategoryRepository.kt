package br.com.jeoliveira.areyoustillwatching

import org.springframework.data.jpa.repository.JpaRepository

interface CategoryRepository : JpaRepository<Category, Long>
