package user.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import user.dto.UserDto;
import user.exception.ValidationChecker;
import user.exception.ValidationException;
import user.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final ValidationChecker validationChecker;

    public UserController(UserService userService, ValidationChecker validationChecker) {
        this.userService = userService;
        this.validationChecker = validationChecker;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDto userDto) throws ValidationException {
        validationChecker.checkUserDtoFields(userDto);
        userService.find(userDto.getLogin());
        userService.save(userDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDto userDto) throws ValidationException {
        validationChecker.checkLoginFields(userDto);
        String jwtToken = userService.login(userDto.getLogin(), userDto.getPassword());
        return ResponseEntity.ok().body(jwtToken);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAll(@RequestParam Integer page,@RequestParam Integer size) throws ValidationException {
        validationChecker.checkPageParams(page, size);
        return ResponseEntity.ok().body(userService.getAll(page, size));
    }

    @GetMapping("/validation")
    public ResponseEntity<?> validate(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok().build();
    }


}
