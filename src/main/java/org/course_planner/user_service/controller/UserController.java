package org.course_planner.user_service.controller;

import org.course_planner.user_service.dto.GetAllUsersRequest;
import org.course_planner.user_service.dto.GetAllUsersResponse;
import org.course_planner.user_service.dto.UserDTO;
import org.course_planner.user_service.service.UserService;
import org.course_planner.utils.exceptions.UserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Mono<UserDTO>> createUser(@RequestBody UserDTO user) {
        if (user.getUserId() != null) {
            throw new UserException("User ID is not acceptable!", HttpStatus.NOT_ACCEPTABLE);
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new UserException("User password is not provided!", HttpStatus.BAD_REQUEST);
        }
        if (user.getEmailId() == null || user.getEmailId().trim().isEmpty()) {
            throw new UserException("Email ID is required!", HttpStatus.BAD_REQUEST);
        }
        if (user.getFullName() == null || user.getFullName().trim().isEmpty()) {
            throw new UserException("Full name is required!", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(userService.createUser(user), HttpStatus.CREATED);
    }

    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Mono<UserDTO>> updateUser(@RequestBody UserDTO user) {
        if (user.getUserId() == null || user.getUserId().trim().equalsIgnoreCase("")) {
            throw new UserException("User ID to modify is not provided!", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(userService.updateUser(user), HttpStatus.ACCEPTED);
    }

    @GetMapping(value = "/retrieve", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Mono<UserDTO>> getUser(@RequestHeader(value = "x-user-id", required = false) String userId,
                                                 @RequestHeader(value = "x-email-id", required = false) String emailId,
                                                 @RequestHeader(value = "x-username", required = false) String username) {
        if (userId != null && !userId.trim().equalsIgnoreCase("")) {
            return new ResponseEntity<>(userService.getUserByUserId(userId), HttpStatus.OK);
        } else if (emailId != null && !emailId.isBlank()) {
            return new ResponseEntity<>(userService.getUserByEmailId(emailId), HttpStatus.OK);
        } else if (username != null && !username.isBlank()) {
            return new ResponseEntity<>(userService.getUserByUsername(username), HttpStatus.OK);
        } else {
            throw new UserException("Missing all required parameters!", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = "/delete/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Mono<Boolean>> deleteUser(@PathVariable String userId) {
        if (userId == null || userId.trim().equalsIgnoreCase("")) {
            throw new UserException("User ID to modify is not provided!", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(userService.delete(userId), HttpStatus.OK);
    }

    @PostMapping(value = "/users")
    public ResponseEntity<Mono<GetAllUsersResponse>> createUser(@RequestBody GetAllUsersRequest request) {
        return new ResponseEntity<>(userService.getAllUsers(request), HttpStatus.OK);
    }
}
