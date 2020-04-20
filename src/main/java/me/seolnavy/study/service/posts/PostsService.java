package me.seolnavy.study.service.posts;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.seolnavy.study.domain.posts.Posts;
import me.seolnavy.study.domain.posts.PostsRepository;
import me.seolnavy.study.web.dto.PostsResponseDto;
import me.seolnavy.study.web.dto.PostsSaveRequestDto;
import me.seolnavy.study.web.dto.PostsUpdateRequestDto;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class PostsService {
    private final PostsRepository postsRepository;

    @Transactional
    @CacheEvict(value = "posts", allEntries = true)
    public Long save(PostsSaveRequestDto requestDto) {
        log.debug("save");
        return postsRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    @CacheEvict(value = "posts", key = "#id")
    public Long update(Long id, PostsUpdateRequestDto requestDto) {
        log.debug("update");
        Posts posts = postsRepository.findById(id)
                                        .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));
        posts.update(requestDto.getTitle(), requestDto.getContent());
        return id;
    }

    @Cacheable(value = "posts", key = "#id")
    public PostsResponseDto findById(Long id) {
        log.debug("findById");
        Posts entity = postsRepository.findById(id)
                                        .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));
        return new PostsResponseDto(entity);
    }

//    @Cacheable(value = "posts")
    @CacheEvict(value = "posts", allEntries = true)
    @Transactional(readOnly = true)
    public List<PostsResponseDto> findAllDesc() {
        log.debug("findAllDesc");
        return postsRepository.findAllDesc().stream()
                                                .map(PostsResponseDto::new)
                                                .collect(Collectors.toList());
    }

    @Transactional
    @CacheEvict(value = "posts", key = "#id")
    public void delete(Long id) {
        log.debug("delete");
        Posts posts = postsRepository.findById(id)
                                       .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));
        postsRepository.delete(posts);
    }

}
