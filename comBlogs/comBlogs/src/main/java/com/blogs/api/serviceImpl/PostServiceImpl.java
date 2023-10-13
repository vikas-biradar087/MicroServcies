package com.blogs.api.serviceImpl;

import com.blogs.api.entity.Post;
import com.blogs.api.exception.ResourceNotFoundException;
import com.blogs.api.payload.PostDto;
import com.blogs.api.payload.PostResponse;
import com.blogs.api.repositories.PostRepository;
import com.blogs.api.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {


    private PostRepository postRepository;

    private ModelMapper mapper;

    public PostServiceImpl(PostRepository postRepository,ModelMapper mapper) {

        this.postRepository = postRepository;
        this.mapper=mapper;
    }

    @Override
    public PostDto createPost(PostDto postDto) {

        // convert DTO to entity
        Post post = mapToEntity(postDto);
        Post newPost = postRepository.save(post);

        // convert entity to DTO
        PostDto postResponse = mapToDTO(newPost);
        return postResponse;
    }


    @Override
    public PostResponse getAllPost(int pageNo,int pageSize,String sortBy,String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();


        // create Pageable instance
        Pageable pageable= PageRequest.of(pageNo,pageSize,Sort.by(sortBy));

        Page<Post> posts = postRepository.findAll(pageable);

        // get content for page object
        List<Post> content = posts.getContent();


        List<PostDto>  contents= content.stream().map(x -> mapToDTO(x)).collect(Collectors.toList());

        PostResponse postResponse=new PostResponse();
        postResponse.setContent(contents);
        postResponse.setPageNo(posts.getNumber());
        postResponse.setPageSize(posts.getSize());
        postResponse.setTotalPages(posts.getTotalPages());
        postResponse.setTotalElement(posts.getTotalElements());
        postResponse.setLast(posts.isLast());

        return postResponse;
    }


    @Override
    public PostDto getPostById(long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        return mapToDTO(post);
    }

    @Override
    public PostDto updatePost(PostDto postDto, long id) {
        Post updatePost = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));

        updatePost.setTitle(postDto.getTitle());
        updatePost.setDescription(postDto.getDescription());
        updatePost.setContent(postDto.getContent());
        Post newPost = postRepository.save(updatePost);
        return mapToDTO(newPost);
    }

    @Override
    public void deletePostById(long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        postRepository.delete(post);
    }


    // convert Entity into DTO
    private PostDto mapToDTO(Post post){

        PostDto postDto = mapper.map(post, PostDto.class);

//        PostDto postDto = new PostDto();
//        postDto.setId(post.getId());
//        postDto.setTitle(post.getTitle());
//        postDto.setDescription(post.getDescription());
//        postDto.setContent(post.getContent());
        return postDto;
    }

    // convert DTO to entity
    private Post mapToEntity(PostDto postDto){

        Post post = mapper.map(postDto, Post.class);
//        Post post = new Post();
//        post.setTitle(postDto.getTitle());
//        post.setDescription(postDto.getDescription());
//        post.setContent(postDto.getContent());

        return post;

    }

}
