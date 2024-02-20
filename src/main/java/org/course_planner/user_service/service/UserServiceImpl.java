package org.course_planner.user_service.service;

import org.course_planner.user_service.document.UserDocument;
import org.course_planner.user_service.dto.GetAllUsersRequest;
import org.course_planner.user_service.dto.GetAllUsersResponse;
import org.course_planner.user_service.dto.Pagination;
import org.course_planner.user_service.dto.UserDTO;
import org.course_planner.user_service.repository.UserRepository;
import org.course_planner.utils.exceptions.UserException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Mono<UserDTO> createUser(UserDTO user) {
        return passesPrelimChecks(user).filter(isValid -> {
            if (!isValid) {
                Mono.error(new UserException("Duplicate emailId or username provided!", HttpStatus.BAD_REQUEST));
            }
            return true;
        }).map((filtered) -> {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setCreatedOn(LocalDateTime.now());
            user.setUpdatedOn(LocalDateTime.now());
            return user;
        }).flatMap((profile) -> userRepository.save(new UserDocument(profile)).map(UserDTO::new));
    }

    @Override
    public Mono<UserDTO> updateUser(UserDTO user) {
        return passesPrelimChecks(user).filter(isValid -> {
                    if (!isValid) {
                        Mono.error(new UserException("Duplicate emailId or username provided!", HttpStatus.BAD_REQUEST));
                    }
                    return true;
                }).then(userRepository.findById(user.getUserId())
                .flatMap(fromDB -> {
                    boolean isUpdated = false;
                    if (user.getPassword() != null && !passwordEncoder.matches(user.getPassword(), fromDB.getPassword())) {
                        fromDB.setPassword(passwordEncoder.encode(user.getPassword()));
                        isUpdated = true;
                    }
                    if (user.getEmailId() != null && !user.getEmailId().equalsIgnoreCase(fromDB.getEmailId())) {
                        fromDB.setEmailId(user.getEmailId());
                        isUpdated = true;
                    }
                    if (user.getFullName() != null && !user.getFullName().equalsIgnoreCase(fromDB.getFullName())) {
                        fromDB.setFullName(user.getFullName());
                        isUpdated = true;
                    }
                    if (user.getEnabled() != null && !user.getEnabled().equals(fromDB.getIsEnabled())) {
                        fromDB.setIsEnabled(user.getEnabled());
                        isUpdated = true;
                    }

                    if (!isUpdated) {
                        Mono.error(new UserException("No changes made!", HttpStatus.BAD_REQUEST));
                    }

                    user.setUpdatedOn(LocalDateTime.now());
                    return userRepository.save(fromDB).map(UserDTO::new);
                }));
    }

    @Override
    public Mono<UserDTO> getUserByUserId(String userId) {
        return userRepository.findById(userId)
                .switchIfEmpty(Mono.error(new UserException("User does not exist!", HttpStatus.OK)))
                .map(UserDTO::new);
    }

    @Override
    public Mono<UserDTO> getUserByEmailId(String emailId) {
        return userRepository.findByEmailId(emailId)
                .switchIfEmpty(Mono.error(new UserException("User does not exist!", HttpStatus.OK)))
                .map(UserDTO::new);
    }

    @Override
    public Mono<UserDTO> getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .switchIfEmpty(Mono.error(new UserException("User does not exist!", HttpStatus.OK)))
                .map(UserDTO::new);
    }

    @Override
    public Mono<Boolean> delete(String userId) {
        return userRepository.findById(userId)
                .switchIfEmpty(Mono.error(new UserException("User does not exist!", HttpStatus.OK)))
                .flatMap(userRepository::delete)
                .thenReturn(true);
    }

    @Override
    public Mono<GetAllUsersResponse> getAllUsers(GetAllUsersRequest request) {
        if (!Pagination.isValidPaginationObj(request.getPagination())) {
            request.setPagination(new Pagination());
            request.getPagination().setPage(0);
            request.getPagination().setSize(10);
        }

        return userRepository.findAll()
                .buffer(request.getPagination().getSize(), request.getPagination().getPage() + 1)
                .elementAt(request.getPagination().getPage(), new ArrayList<>())
                .flatMap(userDocumentList -> {
                    GetAllUsersResponse getAllUsersResponse = new GetAllUsersResponse();
                    userDocumentList.stream().sorted(Comparator.comparing(UserDocument::getUpdatedOn).reversed())
                            .forEach(getAllUsersResponse::addUser);
                    return Mono.just(getAllUsersResponse);
                });
    }
//
    private Mono<Boolean> passesPrelimChecks(UserDTO userDTO) {
        Flux<UserDocument> userDocuments = userRepository.findByUsernameOrEmailId(userDTO.getUsername(), userDTO.getEmailId());
        return userDocuments.any(document -> !(document.getUserId().equals(userDTO.getUserId())
                    && (document.getUsername().equals(userDTO.getUsername())
                    || document.getEmailId().equals(userDTO.getEmailId()))));
//        return userDocuments.any(document ->
//            !document.getUserId().equals(userDTO.getUserId())
//                        && (document.getUsername().equals(userDTO.getUsername())
//                        || document.getEmailId().equals(userDTO.getEmailId())));
    }
}
