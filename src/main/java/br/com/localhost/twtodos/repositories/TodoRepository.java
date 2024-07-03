package br.com.localhost.twtodos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.localhost.twtodos.models.Todo;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    
}
