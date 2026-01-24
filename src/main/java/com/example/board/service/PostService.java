package com.example.board.service;

import com.example.board.entity.Attachment;
import com.example.board.entity.Board;
import com.example.board.entity.Post;
import com.example.board.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    @Value("${app.upload.dir}")
    private String uploadDir;

    public Page<Post> getPostsByBoard(Long boardId, Pageable pageable) {
        return postRepository.findByBoardIdOrderByCreatedAtDesc(boardId, pageable);
    }

    public Post getPost(Long id) {
        return postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid post Id:" + id));
    }

    public Post createPost(Board board, String title, String content, String author, MultipartFile file) throws IOException {
        Post post = Post.builder()
                .board(board)
                .title(title)
                .content(content)
                .author(author)
                .build();

        if (file != null && !file.isEmpty()) {
            Attachment attachment = saveFile(file);
            attachment.setPost(post);
            post.setAttachment(attachment);
        }

        return postRepository.save(post);
    }

    public Post updatePost(Long id, String title, String content, MultipartFile file) throws IOException {
        Post post = getPost(id);
        post.setTitle(title);
        post.setContent(content);

        if (file != null && !file.isEmpty()) {
            // Delete old file if exists (optional, for simplicity we just add/replace)
            Attachment attachment = saveFile(file);
            attachment.setPost(post);
            post.setAttachment(attachment);
        }

        return postRepository.save(post);
    }

    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

    private Attachment saveFile(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String storedFileName = UUID.randomUUID().toString() + "_" + originalFilename;
        
        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs();
        
        File dest = new File(dir.getAbsolutePath() + File.separator + storedFileName);
        file.transferTo(dest);

        return Attachment.builder()
                .originalFileName(originalFilename)
                .storedFileName(storedFileName)
                .filePath("/uploads/" + storedFileName)
                .build();
    }
}
