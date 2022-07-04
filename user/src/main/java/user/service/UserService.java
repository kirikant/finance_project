package user.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import user.dto.UserDto;
import user.entity.Role;
import user.entity.UserEntity;
import user.repository.UserRepo;
import user.security.JwtTokenProvider;
import user.utils.Mapper;

import javax.persistence.EntityNotFoundException;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepo userRepo;
    private final Mapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public UserService(UserRepo userRepo, Mapper mapper, PasswordEncoder passwordEncoder,
                       JwtTokenProvider jwtTokenProvider) {
        this.userRepo = userRepo;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Transactional
    public void save(UserDto userDto) {
        UserEntity userEntity = mapper.map(userDto, UserEntity.class);
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        userEntity.setRole(Role.USER);
        userRepo.saveAndFlush(userEntity);
    }

    public UserEntity findOrThrow(String login){
        return userRepo.findByLogin(login)
                .orElseThrow(() -> new EntityNotFoundException("There is no such user"));
    }

    public void find(String login){
        if (userRepo.findByLogin(login).isPresent()) throw
                new IllegalArgumentException("User with such login is already existed");
    }

    public String login(String login, String password){
        UserEntity userEntity = findOrThrow(login);
        if (!passwordEncoder.matches(password,userEntity.getPassword())){
            throw new IllegalArgumentException("wrong password");
        }
        return jwtTokenProvider.createToken(userEntity.getLogin(),userEntity.getRole(), userEntity.getEmail());
    }

    public Page<UserDto> getAll(Integer page, Integer size){
       return userRepo.findAll(PageRequest.of(page,size))
                .map(x->mapper.map(x,UserDto.class));
    }
}
