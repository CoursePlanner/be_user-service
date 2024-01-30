package org.course_planner.user_service.repository;

import org.course_planner.user_service.document.UserDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<UserDocument, String> {
    Page<UserDocument> findAll(Pageable pageable);

    Optional<UserDocument> findByEmailId(String emailId);

    Optional<UserDocument> findByUsername(String username);

    List<UserDocument> findByUsernameOrEmailId(String username, String emailId);
}
