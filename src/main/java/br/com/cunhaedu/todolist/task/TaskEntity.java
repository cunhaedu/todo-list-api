package br.com.cunhaedu.todolist.task;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity(name = "tb_tasks")
public class TaskEntity {
  @Id
  @GeneratedValue(generator = "UUID")
  private UUID id;

  @Column(length = 50)
  private String title;
  private String description;
  private Boolean done;
  private LocalDateTime startAt;
  private LocalDateTime endAt;

  private UUID userId;

  @CreationTimestamp
  private LocalDateTime createdAt;
}