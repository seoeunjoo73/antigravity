package com.example.board.controller;

import com.example.board.entity.Board;
import com.example.board.entity.Post;
import com.example.board.service.BoardService;
import com.example.board.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;
    private final PostService postService;

    @GetMapping("/{boardId}")
    public String list(@PathVariable Long boardId, Model model, @PageableDefault(size = 10) Pageable pageable) {
        Board board = boardService.getBoard(boardId);
        model.addAttribute("board", board);
        model.addAttribute("posts", postService.getPostsByBoard(boardId, pageable));
        return "board/list";
    }

    @GetMapping("/{boardId}/write")
    public String writeForm(@PathVariable Long boardId, Model model) {
        model.addAttribute("board", boardService.getBoard(boardId));
        return "board/write";
    }

    @PostMapping("/{boardId}/write")
    public String write(@PathVariable Long boardId,
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam String author,
            @RequestParam(required = false) MultipartFile file) throws IOException {
        Board board = boardService.getBoard(boardId);
        postService.createPost(board, title, content, author, file);
        return "redirect:/board/" + boardId;
    }

    @GetMapping("/view/{postId}")
    public String view(@PathVariable Long postId, Model model) {
        Post post = postService.getPost(postId);
        model.addAttribute("post", post);
        return "board/view";
    }

    @GetMapping("/edit/{postId}")
    public String editForm(@PathVariable Long postId, Model model) {
        Post post = postService.getPost(postId);
        model.addAttribute("post", post);
        model.addAttribute("board", post.getBoard());
        return "board/write";
    }

    @PostMapping("/edit/{postId}")
    public String edit(@PathVariable Long postId,
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam(required = false) MultipartFile file) throws IOException {
        Post post = postService.updatePost(postId, title, content, file);
        return "redirect:/board/view/" + post.getId();
    }

    @PostMapping("/delete/{postId}")
    public String delete(@PathVariable Long postId) {
        Post post = postService.getPost(postId);
        Long boardId = post.getBoard().getId();
        postService.deletePost(postId);
        return "redirect:/board/" + boardId;
    }
}
