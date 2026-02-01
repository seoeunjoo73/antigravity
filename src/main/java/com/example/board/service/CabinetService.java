package com.example.board.service;

import com.example.board.entity.*;
import com.example.board.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CabinetService {

    private final FolderRepository folderRepository;
    private final FolderPermissionRepository permissionRepository;
    private final CabinetFileRepository fileRepository;
    private final AppUserRepository userRepository;
    private final CompanyRepository companyRepository;

    private final String uploadDir = "uploads/cabinet/";

    public Folder getFolder(Long folderId) {
        return folderRepository.findById(folderId)
                .orElseThrow(() -> new RuntimeException("Folder not found"));
    }

    public List<Folder> getFoldersByCompany(Long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));
        return company.getFolders();
    }

    public List<AppUser> getUsersByCompany(Long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));
        return company.getUsers();
    }

    public List<FolderPermission> getPermissionsByFolder(Long folderId) {
        Folder folder = getFolder(folderId);
        return folder.getPermissions();
    }

    /**
     * 업체 대표자가 업체 폴더 생성
     */
    public Folder createFolder(Long representativeId, String folderName, Long maxFileSize) {
        AppUser representative = userRepository.findById(representativeId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (representative.getRole() != AppUser.UserRole.REPRESENTATIVE) {
            throw new RuntimeException("Only representatives can create folders");
        }

        Folder folder = Folder.builder()
                .name(folderName)
                .maxFileSize(maxFileSize)
                .company(representative.getCompany())
                .build();

        return folderRepository.save(folder);
    }

    /**
     * 업체 대표자가 업체 사용자에게 폴더 접근 권한 제어
     */
    public void grantPermission(Long representativeId, Long folderId, Long targetUserId) {
        AppUser representative = userRepository.findById(representativeId)
                .orElseThrow(() -> new RuntimeException("Representative not found"));

        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new RuntimeException("Folder not found"));

        if (representative.getRole() != AppUser.UserRole.REPRESENTATIVE ||
                !folder.getCompany().getId().equals(representative.getCompany().getId())) {
            throw new RuntimeException("No authority to grant permission");
        }

        AppUser targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new RuntimeException("Target user not found"));

        if (!targetUser.getCompany().getId().equals(representative.getCompany().getId())) {
            throw new RuntimeException("User belongs to a different company");
        }

        if (!permissionRepository.existsByFolderAndUser(folder, targetUser)) {
            FolderPermission permission = FolderPermission.builder()
                    .folder(folder)
                    .user(targetUser)
                    .build();
            permissionRepository.save(permission);
        }
    }

    public void revokePermission(Long representativeId, Long folderId, Long targetUserId) {
        AppUser representative = userRepository.findById(representativeId)
                .orElseThrow(() -> new RuntimeException("Representative not found"));

        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new RuntimeException("Folder not found"));

        if (representative.getRole() != AppUser.UserRole.REPRESENTATIVE ||
                !folder.getCompany().getId().equals(representative.getCompany().getId())) {
            throw new RuntimeException("No authority to revoke permission");
        }

        AppUser targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new RuntimeException("Target user not found"));

        permissionRepository.deleteByFolderAndUser(folder, targetUser);
    }

    /**
     * 폴더에 파일 업로드 (사이즈 제한 포함)
     */
    public CabinetFile uploadFile(Long userId, Long folderId, MultipartFile file) throws IOException {
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new RuntimeException("Folder not found"));

        // 권한 체크 (대표자는 본인 업체 폴더 모두 가능, 사용자는 권한이 있어야 함)
        boolean hasPermission = user.getRole() == AppUser.UserRole.REPRESENTATIVE &&
                user.getCompany().getId().equals(folder.getCompany().getId());

        if (!hasPermission) {
            hasPermission = permissionRepository.existsByFolderAndUser(folder, user);
        }

        if (!hasPermission) {
            throw new RuntimeException("No permission to upload file to this folder");
        }

        // 사이즈 제한 체크
        if (folder.getMaxFileSize() != null && folder.getMaxFileSize() > 0) {
            if (file.getSize() > folder.getMaxFileSize()) {
                throw new RuntimeException("File size exceeds the limit: " + folder.getMaxFileSize());
            }
        }

        // 파일 저장 로직
        String originalFileName = file.getOriginalFilename();
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String storedFileName = UUID.randomUUID().toString() + extension;

        Path directoryPath = Paths.get(uploadDir);
        if (!Files.exists(directoryPath)) {
            Files.createDirectories(directoryPath);
        }

        Path filePath = directoryPath.resolve(storedFileName);
        Files.copy(file.getInputStream(), filePath);

        CabinetFile cabinetFile = CabinetFile.builder()
                .originalFileName(originalFileName)
                .storedFileName(storedFileName)
                .filePath(filePath.toString())
                .fileSize(file.getSize())
                .folder(folder)
                .uploader(user)
                .build();

        return fileRepository.save(cabinetFile);
    }
}
