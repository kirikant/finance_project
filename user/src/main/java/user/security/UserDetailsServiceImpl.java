package user.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import user.entity.UserEntity;
import user.repository.UserRepo;

import javax.persistence.EntityNotFoundException;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepo usersRepo;

    public UserDetailsServiceImpl(UserRepo usersRepo) {
        this.usersRepo = usersRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws EntityNotFoundException {
        UserEntity user= usersRepo.findByLogin(username)
                .orElseThrow(() -> new EntityNotFoundException("there is no such user"));
        return SecurityUser.transform(user);
    }
}