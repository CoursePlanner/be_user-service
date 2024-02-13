package org.course_planner.user_service.repository;

import org.course_planner.user_service.document.UserDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends ReactiveMongoRepository<UserDocument, String> {
//    Mono<Page<UserDocument>> findAll(Pageable pageable);

    Mono<UserDocument> findByEmailId(String emailId);

    Mono<UserDocument> findByUsername(String username);

    Flux<UserDocument> findByUsernameOrEmailId(String username, String emailId);
}
