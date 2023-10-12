package br.com.marceloandre.todolist.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

// generator são atributos dinamicos
public interface UserRepository extends JpaRepository<UserModel, UUID> {
   UserModel findByUserName(String userName);
}
