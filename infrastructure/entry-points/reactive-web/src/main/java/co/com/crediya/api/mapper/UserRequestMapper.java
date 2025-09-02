package co.com.crediya.api.mapper;

import co.com.crediya.api.request.UserRequest;
import co.com.crediya.model.user.User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class UserRequestMapper {

    private UserRequestMapper() {}

    public static User toDomain(UserRequest userRequest) {

        LocalDate birthDate = LocalDate.parse(userRequest.getBirthDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return User.builder()
                .id(null)
                .names(userRequest.getNames())
                .lastNames(userRequest.getLastNames())
                .birthDate(birthDate)
                .documentNumber(userRequest.getDocumentNumber())
                .email(userRequest.getEmail())
                .address(userRequest.getAddress())
                .phone(userRequest.getPhone())
                .idRole(null)
                .salaryBase(userRequest.getSalaryBase())
                .build();
    }

}
