package com.eight.blogserver8.service;

import com.eight.blogserver8.controller.response.*;
import com.eight.blogserver8.domain.*;
import com.eight.blogserver8.jwt.TokenProvider;
import com.eight.blogserver8.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MypageService {



    //
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final SubCommentRepository subCommentRepository;
    //    private final LikeRepository likeRepository;
    private final TokenProvider tokenProvider;


    @Transactional
    public ResponseDto<?> viewMypage(HttpServletRequest request) {
        if (null == request.getHeader("Refresh-Token")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

//        String name = maybePerson.get().getName();
//        Member findMember = memberRepository.findByNickname(mypageRequestDto.getNickname()).get(); // optional. get을해서 넣어줌..

        // 내가 작성한글 모음 db에서 들고오기
        List<Post> postList = postRepository.findByMember(member);
        List<Comment> commentList = commentRepository.findByMember(member);
        List<SubComment> subCommentList = subCommentRepository.findByMember(member);


        // 내가 좋아요한 글 db에서 들고오기
//        Long postId = heartPostRepository.findByMember(member);
//        List<Post> heartPostList = postRepository.findByPostId(1L);

//        Long commentId = heartCommentRepository.findByMember(member);
//        List<Comment> heartCommentList = commentRepository.findByCommentId(1L);

//        Long subCommentId = heartSubCommentRepository.findByMember(member);
//        List<SubComment> heartSubCommentList = subCommentRepository.findBySubCommentId(1L);



        MypageResponseDto mypageResponseDto = new MypageResponseDto();

        // 내가 작성한 글 모음
        List<MypagePostResponseDto> postListDto = new ArrayList<>();
        List<MypageCommentResponseDto> commentListDto = new ArrayList<>();
        List<MypageSubCommentResponseDto> subcommentListDto = new ArrayList<>();
        // 내가 좋아요한 글 모음..
//        List<MypagePostResponseDto> heartPostListDto = new ArrayList<>();
//        List<MypageCommentResponseDto> heartCommentListDto = new ArrayList<>();
//        List<MypageSubCommentResponseDto> heartSubcommentListDto = new ArrayList<>();

        for( Post post : postList){
            postListDto.add(
                    MypagePostResponseDto.builder()
                            .postId(post.getId())
                            .title(post.getTitle())
                            .postContent(post.getContent())
                            .heart(post.getHeart())
                            .createdAt(post.getCreatedAt())
                            .modifiedAt(post.getModifiedAt())
                            .build()
            );
        }


        for (Comment comment : commentList){
            commentListDto.add(
                    MypageCommentResponseDto.builder()
                            .commentId(comment.getId())
                            .commentContent(comment.getContent())
                            .heart(comment.getHeart())
                            .createdAt(comment.getCreatedAt())
                            .modifiedAt(comment.getModifiedAt())
                            .build()
            );
        }

        for(SubComment subComment : subCommentList){
            subcommentListDto.add(
                    MypageSubCommentResponseDto.builder()
                            .subCommentId(subComment.getId())
                            .subCommentContent(subComment.getContent())
                            .heart(subComment.getHeart())
                            .createdAt(subComment.getCreatedAt())
                            .modifiedAt(subComment.getModifiedAt())
                            .build()
            );
        }

        //        for(Post post : heartPostList){
//            heartPostListDto.add(
//                    MypagePostResponseDto.builder()
//                            .postId(post.getId())
//                            .title(post.getTitle())
//                            .postContent(post.getContent())
//                            .heart(post.getHeart())
//                            .createdAt(post.getCreatedAt())
//                            .modifiedAt(post.getModifiedAt())
//                            .build()
//            );
//
//        }
//
//        for (Comment comment : heartCommentList){
//            heartCommentListDto.add(
//                    MypageCommentResponseDto.builder()
//                            .commentId(comment.getId())
//                            .commentContent(comment.getContent())
//                            .heart(comment.getHeart())
//                            .createdAt(comment.getCreatedAt())
//                            .modifiedAt(comment.getModifiedAt())
//                            .build()
//            );
//        }
//
//        for(SubComment subComment : heartSubCommentList){
//            heartSubcommentListDto.add(
//                    MypageSubCommentResponseDto.builder()
//                            .subCommentId(subComment.getId())
//                            .subCommentContent(subComment.getContent())
//                            .heart(subComment.getHeart())
//                            .createdAt(subComment.getCreatedAt())
//                            .modifiedAt(subComment.getModifiedAt())
//                            .build()
//            );
//        }



//        mypage.update(findMember,postList,commentList,subCommentList);// 마이페이지 객체에 업데이트
//        mypageRepository.save(mypage);   // 저장하는게 아니다.
        mypageResponseDto.update(postListDto,commentListDto,subcommentListDto ); // 빌더 안쓰고 리스폰 만듬.

         return ResponseDto.success( mypageResponseDto );

    }

    @Transactional
    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }


}
