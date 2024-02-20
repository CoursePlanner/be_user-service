package org.course_planner.user_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.course_planner.user_service.document.UserDocument;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String userId;
    private String fullName;
    private String emailId;
    private String username;
    private String password;
    private Boolean enabled;
    private List<String> authorities;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;

    public UserDTO(UserDocument entity) {
        this.userId = entity.getUserId();
        this.fullName = entity.getFullName();
        this.emailId = entity.getEmailId();
        this.username = entity.getUsername();
        this.password = entity.getPassword();
        this.enabled = entity.getIsEnabled();
        this.createdOn = entity.getCreatedOn();
        this.updatedOn = entity.getUpdatedOn();
        this.authorities = entity.getAuthoritiesStrList();
    }

    public UserDTO(String fullName, String emailId, String username, String password) {
        this.fullName = fullName;
        this.emailId = emailId;
        this.username = username;
        this.password = password;
    }
}
