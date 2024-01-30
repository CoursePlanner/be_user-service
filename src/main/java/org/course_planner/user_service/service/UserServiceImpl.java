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

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDTO createUser(UserDTO user) {
        if (!passesPrelimChecks(user)) {
            throw new UserException("Duplicate emailId or username provided!", HttpStatus.BAD_REQUEST);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreatedOn(LocalDateTime.now());
        user.setUpdatedOn(LocalDateTime.now());
        UserDocument userDocument = userRepository.save(new UserDocument(user));
        return new UserDTO(userDocument);
    }

    @Override
    public UserDTO updateUser(UserDTO user) {
        UserDocument fromDB = userRepository.findById(user.getUserId()).orElse(null);
        if (fromDB == null) {
            throw new UserException("No changes made!", HttpStatus.BAD_REQUEST);
        }

        if (!passesPrelimChecks(user)) {
            throw new UserException("Duplicate emailId or username provided!", HttpStatus.BAD_REQUEST);
        }

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

        if (isUpdated) {
            user.setUpdatedOn(LocalDateTime.now());
            return new UserDTO(userRepository.save(fromDB));
        }

        throw new UserException("No changes made!", HttpStatus.BAD_REQUEST);
    }

    @Override
    public UserDTO getUserByUserId(String userId) {
        UserDocument userDocument = userRepository.findById(userId).orElse(null);
        if (userDocument == null) {
            throw new UserException("User does not exist!", HttpStatus.OK);
        }
        return new UserDTO(userDocument);
    }

    @Override
    public UserDTO getUserByEmailId(String emailId) {
        UserDocument userDocument = userRepository.findByEmailId(emailId).orElse(null);
        if (userDocument == null) {
            throw new UserException("User does not exist!", HttpStatus.OK);
        }
        return new UserDTO(userDocument);
    }

    @Override
    public UserDTO getUserByUsername(String username) {
        UserDocument userDocument = userRepository.findByUsername(username).orElse(null);
        if (userDocument == null) {
            throw new UserException("User does not exist!", HttpStatus.OK);
        }
        return new UserDTO(userDocument);
    }

    @Override
    public Boolean delete(String userId) {
        UserDocument userDocument = userRepository.findById(userId).orElse(null);
        if (userDocument != null) {
            userRepository.delete(userDocument);
            return true;
        }
        return false;
    }

    @Override
    public GetAllUsersResponse getAllUsers(GetAllUsersRequest request) {
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "updatedOn");
        if (Pagination.isValidPaginationObj(request.getPagination())) {
            pageable = PageRequest.of(request.getPagination().getPage(), request.getPagination().getSize(),
                    Sort.Direction.DESC, "updatedOn");
        }
        return new GetAllUsersResponse(userRepository.findAll(pageable));
    }

    private boolean passesPrelimChecks(UserDTO userDTO) {
        List<UserDocument> userDocuments = userRepository.findByUsernameOrEmailId(userDTO.getUsername(), userDTO.getEmailId());
        long countOfRecords = userDocuments.stream().filter(document ->
                !document.getUserId().equals(userDTO.getUserId())
                        && (document.getUsername().equals(userDTO.getUsername())
                        || document.getEmailId().equals(userDTO.getEmailId()))).count();
        return countOfRecords == 0;
    }
}
