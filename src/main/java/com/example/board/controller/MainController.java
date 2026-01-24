package com.example.board.controller;

import com.example.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final BoardService boardService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("boards", boardService.getAllBoards());
        return "index";
    }

    @PostMapping("/board/create")
    public String createBoard(@RequestParam String name, @RequestParam String description) {
        boardService.createBoard(name, description);
        return "redirect:/";
    }

    @PostMapping("/board/delete")
    public String deleteBoard(@RequestParam Long id) {
        boardService.deleteBoard(id);
        return "redirect:/";
    }
}
