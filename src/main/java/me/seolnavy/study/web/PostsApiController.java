package me.seolnavy.study.web;

import com.amazonaws.services.s3.model.PutObjectResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.seolnavy.study.service.aws.AwsS3Service;
import me.seolnavy.study.service.posts.PostsService;
import me.seolnavy.study.web.dto.PostsResponseDto;
import me.seolnavy.study.web.dto.PostsSaveRequestDto;
import me.seolnavy.study.web.dto.PostsUpdateRequestDto;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@RestController
public class PostsApiController {

    private final PostsService postsService;
    private final AwsS3Service awsS3Service;

    @PostMapping("/api/v1/posts")
    public Long save(@RequestBody PostsSaveRequestDto requestDto) {
        return postsService.save(requestDto);
    }

    @PostMapping("/api/v2/posts")
    public Long saveV2(PostsSaveRequestDto requestDto) throws ParseException, IOException {
        log.debug("requestDto : " + requestDto);
        log.debug("file : " + requestDto.getPicture());
        PutObjectResult putResult = awsS3Service.uploadObject(requestDto.getPicture());
        return postsService.save(requestDto);
    }

    @PutMapping("/api/v1/posts/{id}")
    public Long update(@PathVariable Long id, @RequestBody PostsUpdateRequestDto requestDto){
        return postsService.update(id, requestDto);
    }

    @GetMapping("/api/v1/posts/{id}")
    public PostsResponseDto findById(@PathVariable Long id) {
        return postsService.findById(id);
    }

    @DeleteMapping("/api/v1/posts/{id}")
    public Long delete(@PathVariable Long id) {
        postsService.delete(id);
        return id;
    }
}
