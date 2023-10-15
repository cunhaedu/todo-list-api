package br.com.cunhaedu.todolist.user;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserRepository extends JpaRepository<UserEntity, UUID> {
  UserEntity findByUsername(String username);
}
