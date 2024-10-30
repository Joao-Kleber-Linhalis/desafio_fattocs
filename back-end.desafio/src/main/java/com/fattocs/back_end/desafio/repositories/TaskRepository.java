package com.fattocs.back_end.desafio.repositories;

import com.fattocs.back_end.desafio.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task,Long> {
}
