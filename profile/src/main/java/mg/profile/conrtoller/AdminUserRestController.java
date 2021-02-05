package mg.profile.conrtoller;

import lombok.AllArgsConstructor;
import mg.profile.dto.UserDto;
import mg.profile.dto.UserResponseDto;
import mg.profile.mapper.UserMapper;
import mg.profile.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/admin/profile")
public class AdminUserRestController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/{uuid}")
    public UserResponseDto getUserProfileByUuid(@PathVariable String uuid) {
        return userMapper.mapToResponseDto(userService.findUserByUuid(UUID.fromString(uuid)));
    }

    @PostMapping
    public UserResponseDto createUserProfile(@RequestBody UserDto userDto) {
        return userMapper.mapToResponseDto(userService.createUser(userDto));
    }

    @PutMapping
    public UserResponseDto updateUserProfile(@RequestBody UserDto userDto) {
        return userMapper.mapToResponseDto(userService.updateUser(userDto));
    }

    @DeleteMapping("/{uuid}")
    public void deleteProfileByUuid(@PathVariable String uuid) {
        userService.deleteUserByUuid(UUID.fromString(uuid));
    }
}
