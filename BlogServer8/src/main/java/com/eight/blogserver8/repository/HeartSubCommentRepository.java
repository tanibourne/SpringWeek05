package com.eight.blogserver8.repository;

import com.eight.blogserver8.domain.HeartSubComment;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

public interface HeartSubCommentRepository extends JpaRepository<HeartSubComment, Long> {
    Long countAllBySubCommentId(Long commentId);

    @Transactional
    void deleteBySubCommentId(Long commentId);
}
