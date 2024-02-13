package org.course_planner.user_service.service;

import org.course_planner.user_service.dto.GetAllUsersRequest;
import org.course_planner.user_service.dto.GetAllUsersResponse;
import org.course_planner.user_service.dto.UserDTO;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<UserDTO> createUser(UserDTO user);

    Mono<UserDTO> updateUser(UserDTO user);

    Mono<UserDTO> getUserByEmailId(String emailId);

    Mono<UserDTO> getUserByUserId(String userId);

    Mono<UserDTO> getUserByUsername(String username);

    Mono<Boolean> delete(String userId);

    Mono<GetAllUsersResponse> getAllUsers(GetAllUsersRequest request);
}
