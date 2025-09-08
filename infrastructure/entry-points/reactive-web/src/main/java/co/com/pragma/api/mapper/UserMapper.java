package co.com.pragma.api.mapper;

import co.com.pragma.api.dto.request.UserDTORequest;
import co.com.pragma.api.dto.response.UserDTOResponse;
import co.com.pragma.model.user.User;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import reactor.core.publisher.Mono;

public class UserMapper {

    public static Mono<User> toUserDomain(UserDTORequest userDTO) {
        return Mono.just(User.builder()
                .name(userDTO.getName())
                .lastName(userDTO.getLastName())
                .birthdate(userDTO.getBirthdate())
                .address(userDTO.getAddress())
                .phoneNumber(userDTO.getPhoneNumber())
                .email(userDTO.getEmail())
                .baseSalary(userDTO.getBaseSalary())
                .idNumber(userDTO.getIdNumber())
                .idRole(userDTO.getIdRole())
                .password(userDTO.getPassword())
                .build());
    }

    public static Mono<UserDTOResponse> toUserDTOResponse(User user) {
        return Mono.just(UserDTOResponse.builder()
                .name(user.getName())
                .lastName(user.getLastName())
                .birthdate(user.getBirthdate())
                .address(user.getAddress())
                .phoneNumber(user.getPhoneNumber())
                .email(user.getEmail())
                .baseSalary(user.getBaseSalary())
                .idNumber(user.getIdNumber())
                .idRole(user.getIdRole())
                .password(user.getPassword())
                .build());
    }
}
