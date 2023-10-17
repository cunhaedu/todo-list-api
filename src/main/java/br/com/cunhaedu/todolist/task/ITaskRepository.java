package br.com.cunhaedu.todolist.task;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ITaskRepository extends JpaRepository<TaskEntity, UUID> {
  List<TaskEntity> findByUserId(UUID userId);
}
