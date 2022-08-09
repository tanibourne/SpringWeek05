package com.eight.blogserver8.repository;

import com.eight.blogserver8.domain.HeartComment;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

public interface HeartCommentRepository extends JpaRepository<HeartComment, Long> {
    Long countAllByCommentId(Long commentId);

    @Transactional
    void deleteByCommentId(Long commentId);
}
