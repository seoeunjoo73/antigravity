package com.example.board.controller;

import com.example.board.entity.AppUser;
import com.example.board.entity.Folder;
import com.example.board.service.CabinetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/cabinet")
@RequiredArgsConstructor
public class CabinetController {

    private final CabinetService cabinetService;

    // TODO: 로그인 기능이 완성될 때까지 임시로 사용할 사용자 ID와 업체 ID
    private final Long TEMP_USER_ID = 1L;
    private final Long TEMP_COMPANY_ID = 1L;

    @GetMapping
    public String list(Model model) {
        List<Folder> folders = cabinetService.getFoldersByCompany(TEMP_COMPANY_ID);
        model.addAttribute("folders", folders);
        return "cabinet/list";
    }

    @PostMapping("/folder/create")
    public String createFolder(@RequestParam String name, @RequestParam Long maxFileSize,
            RedirectAttributes redirectAttributes) {
        try {
            cabinetService.createFolder(TEMP_USER_ID, name, maxFileSize);
            redirectAttributes.addFlashAttribute("message", "폴더가 생성되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "폴더 생성 실패: " + e.getMessage());
        }
        return "redirect:/cabinet";
    }

    @GetMapping("/{folderId}")
    public String detail(@PathVariable Long folderId, Model model) {
        Folder folder = cabinetService.getFolder(folderId);
        List<AppUser> companyUsers = cabinetService.getUsersByCompany(TEMP_COMPANY_ID);

        model.addAttribute("folder", folder);
        model.addAttribute("companyUsers", companyUsers);
        model.addAttribute("permissions", folder.getPermissions());
        model.addAttribute("files", folder.getFiles());

        return "cabinet/detail";
    }

    @PostMapping("/{folderId}/upload")
    public String uploadFile(@PathVariable Long folderId, @RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes) {
        try {
            cabinetService.uploadFile(TEMP_USER_ID, folderId, file);
            redirectAttributes.addFlashAttribute("message", "파일이 업로드되었습니다.");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "파일 저장 중 오류가 발생했습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "업로드 실패: " + e.getMessage());
        }
        return "redirect:/cabinet/" + folderId;
    }

    @PostMapping("/{folderId}/permission/grant")
    public String grantPermission(@PathVariable Long folderId, @RequestParam Long userId,
            RedirectAttributes redirectAttributes) {
        try {
            cabinetService.grantPermission(TEMP_USER_ID, folderId, userId);
            redirectAttributes.addFlashAttribute("message", "권한이 부여되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "권한 부여 실패: " + e.getMessage());
        }
        return "redirect:/cabinet/" + folderId;
    }

    @PostMapping("/{folderId}/permission/revoke")
    public String revokePermission(@PathVariable Long folderId, @RequestParam Long userId,
            RedirectAttributes redirectAttributes) {
        try {
            cabinetService.revokePermission(TEMP_USER_ID, folderId, userId);
            redirectAttributes.addFlashAttribute("message", "권한이 취소되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "권한 취소 실패: " + e.getMessage());
        }
        return "redirect:/cabinet/" + folderId;
    }
}
