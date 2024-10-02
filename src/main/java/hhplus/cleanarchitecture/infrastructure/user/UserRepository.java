package hhplus.cleanarchitecture.infrastructure.user;

import org.springframework.data.jpa.repository.JpaRepository;

import hhplus.cleanarchitecture.domain.user.User;

public interface UserRepository extends JpaRepository<User, Long> { }
