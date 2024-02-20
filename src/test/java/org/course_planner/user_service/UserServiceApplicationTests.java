package org.course_planner.user_service;

import com.google.gson.Gson;
import org.course_planner.user_service.controller.UserController;
import org.course_planner.user_service.dto.UserDTO;
import org.course_planner.user_service.document.UserDocument;
import org.course_planner.user_service.repository.UserRepository;
import org.course_planner.user_service.service.UserServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class UserServiceApplicationTests {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceApplicationTests.class);
    private UserController userController;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepositoryStub;

    @InjectMocks
    private UserServiceImpl userService;

    private String userId;

    @BeforeEach
    @Test
    void createProfile() {
        LOGGER.info("createProfile: Executing test case!");
        UserDTO userDTO = new UserDTO("Full Name", "email@id.com",
                "username", "password");
        when(userRepositoryStub.save(new UserDocument(userDTO)))
                .thenReturn((new Gson().fromJson("{\n" +
                        "    \"userId\": \"65b1cfea23e6eb7e39bbee12\",\n" +
                        "    \"fullName\": \"Test User\",\n" +
                        "    \"emailId\": \"testuser@example.com\",\n" +
                        "    \"username\": \"testuser\",\n" +
                        "    \"password\": \"$2a$10$rlOpjs4FQ8Ps0CiHwWhK.uroIKKBoVzmIYZ1e0bH6O57qCBAIYtMa\",\n" +
                        "    \"enabled\": true,\n" +
                        "    \"authorities\": null,\n" +
                        "    \"createdOn\": \"2024-01-25T08:35:14.3937021\",\n" +
                        "    \"updatedOn\": \"2024-01-25T08:35:14.3937021\"\n" +
                        "}", UserDocument.class)));
        UserDTO response = userController.createUser(userDTO).getBody();
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getUserId());
        Assertions.assertNotEquals(response.getUserId(), "");
        userId = response.getUserId();
        LOGGER.info("createProfile: Test case succeeded!");
    }

    @Test
    void createProfileFailScenario() {
        LOGGER.info("createProfileFailScenario: Executing test case!");
        UserDTO userDTO = new UserDTO("Full Name", "email@id.com",
                "username", "password");
        userDTO.setUserId("helloWorldId");
        UserDTO response = userController.createUser(userDTO).getBody();
        Assertions.assertNull(response);
        LOGGER.info("createProfileFailScenario: Test case succeeded!");
    }

    @AfterEach
    @Test
    void deleteProfile() {
        LOGGER.info("deleteProfile: Executing test case!");
        if (userId != null) {
            Boolean response = userController.deleteUser(userId).getBody();
            Assertions.assertNotNull(response);
            Assertions.assertTrue(response);
        }
        userId = null;
        LOGGER.info("deleteProfile: Test case succeeded!");
    }
}
