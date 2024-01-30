package org.course_planner.user_service.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.course_planner.user_service.dto.UserDTO;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(value = "user_profile")
public class UserDocument implements UserDetails {
    @Id
    private String userId;
    private String fullName;
    @Indexed(unique = true)
    private String emailId;
    @Indexed(unique = true)
    private String username;
    private String password;
    private List<String> authorities = new ArrayList<>();
    private Boolean isEnabled;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;

    public UserDocument(UserDTO dto) {
        this.userId = dto.getUserId();
        this.fullName = dto.getFullName();
        this.emailId = dto.getEmailId();
        this.username = dto.getUsername();
        this.password = dto.getPassword();
        this.isEnabled = true;
        this.authorities = dto.getAuthorities();
        this.createdOn = dto.getCreatedOn();
        this.updatedOn = dto.getUpdatedOn();
    }

    public List<String> getAuthoritiesStrList() {
        return authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public String toString() {
        return MessageFormat.format("{userId\": \"{0}\"," +
                        "    \"fullName\": \"{1}\"," +
                        "    \"emailId\": \"{2}\"," +
                        "    \"username\": \"{3}\"," +
                        "    \"password\": \"{4}\"," +
                        "    \"enabled\": {5}," +
                        "    \"authorities\": {6}," +
                        "    \"createdOn\": \"{7}\"," +
                        "    \"updatedOn\": \"{8}\"" +
                        "}", userId, fullName, emailId, username, password, isEnabled,
                authorities, createdOn, updatedOn);
    }
}
