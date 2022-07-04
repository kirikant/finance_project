package user.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import user.entity.UserEntity;

import java.util.Optional;
import java.util.UUID;

public interface UserRepo extends JpaRepository<UserEntity, UUID> {
      Optional<UserEntity> findByLogin(String login);
}
