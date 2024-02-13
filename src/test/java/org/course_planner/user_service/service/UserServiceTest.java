//package org.course_planner.user_service.service;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import org.course_planner.user_service.dto.UserDTO;
//import org.course_planner.user_service.document.UserDocument;
//import org.course_planner.user_service.repository.UserRepository;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.doAnswer;
//
//@SpringBootTest
//@ExtendWith(MockitoExtension.class)
//public class UserServiceTest {
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private PasswordEncoder passwordEncoder;
//
//    @InjectMocks
//    private UserServiceImpl userService;
//
//    private UserDocument loadMockData() {
//        try {
//            ObjectMapper mapper = new ObjectMapper();
//            mapper.registerModule(new JavaTimeModule());
//            String mockData = "{\"userId\":\"65b1cfea23e6eb7e39bbee12\",\"fullName\":\"Test User\",\"emailId\":\"testuser@example.com\",\"username\":\"testuser\",\"password\":\"$2a$10$rlOpjs4FQ8Ps0CiHwWhK.uroIKKBoVzmIYZ1e0bH6O57qCBAIYtMa\",\"enabled\":true,\"authorities\":null,\"createdOn\":\"2024-01-25T08:35:14.3937021\",\"updatedOn\":\"2024-01-25T08:35:14.3937021\"}";
//            UserDTO userDTO = mapper.readValue(mockData, UserDTO.class);
//            return new UserDocument(userDTO);
//        } catch (Exception ex) {
//            return new UserDocument();
//        }
//    }
//
//    @Test
//    void createUser() {
//        UserDocument mockData = loadMockData();
//        doAnswer(invocationOnMock -> mockData)
//                .when(userRepository).save(any(UserDocument.class));
//        UserDTO response = userService.createUser(new UserDTO(mockData));
//        Assertions.assertNotNull(response);
//        Assertions.assertEquals(response.getUserId(), mockData.getUserId());
//    }
//}
