package org.course_planner.user_service.service;

import org.course_planner.user_service.dto.GetAllUsersRequest;
import org.course_planner.user_service.dto.GetAllUsersResponse;
import org.course_planner.user_service.dto.UserDTO;

public interface UserService {
    UserDTO createUser(UserDTO user);

    UserDTO updateUser(UserDTO user);

    UserDTO getUserByEmailId(String emailId);

    UserDTO getUserByUserId(String userId);

    UserDTO getUserByUsername(String username);

    Boolean delete(String userId);

    GetAllUsersResponse getAllUsers(GetAllUsersRequest request);
}
