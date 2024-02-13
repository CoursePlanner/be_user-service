package org.course_planner.user_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.course_planner.user_service.document.UserDocument;
import org.springframework.data.domain.Page;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetAllUsersResponse {
    List<UserDTO> usersList;
    private Pagination pagination;

    public GetAllUsersResponse(Page<UserDocument> pageableResponse) {
        this.usersList = pageableResponse.getContent().stream().map(UserDTO::new).collect(Collectors.toList());
        this.pagination = new Pagination(pageableResponse);
    }

    public GetAllUsersResponse addUser(UserDocument userDocument) {
        if (this.usersList == null) {
            this.usersList = new LinkedList<>();
        }
        this.usersList.add(new UserDTO(userDocument));
        return this;
    }
}
