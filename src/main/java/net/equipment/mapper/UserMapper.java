
package net.equipment.mapper;

import net.equipment.dto.UserDto;
import net.equipment.models.User;

public class UserMapper {
    public UserMapper() {
    }

    public static UserDto mapToUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getPassword(),
                user.getEmail(),
                user.getRole(),
                user.getCompany(),
                user.getEquipment(),
                user.getCreatedAt(),
                user.getUpdatedAt());
    }

    public static User mapToUser(UserDto userDto) {
        return new User(
                userDto.getId(),
                userDto.getFirstName(),
                userDto.getLastName(),
                userDto.getPassword(),
                userDto.getEmail(),
                userDto.getRole(),
                userDto.getCompany(),
                userDto.getEquipment(),
                userDto.getCreatedAt(),
                userDto.getUpdatedAt());
    }
}
