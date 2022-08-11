package com.eight.blogserver8.repository;

import com.eight.blogserver8.domain.HeartPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface HeartPostRepository extends JpaRepository<HeartPost, Long> {

    Long countAllByPostId(Long postId);

    @Transactional
    void deleteByPostId(Long postId);

}
