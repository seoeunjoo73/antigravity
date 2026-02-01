package com.example.board.service;

import com.example.board.entity.*;
import com.example.board.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CabinetServiceTest {

    @Mock
    private FolderRepository folderRepository;
    @Mock
    private FolderPermissionRepository permissionRepository;
    @Mock
    private CabinetFileRepository fileRepository;
    @Mock
    private AppUserRepository userRepository;

    @InjectMocks
    private CabinetService cabinetService;

    private Company company;
    private AppUser representative;
    private AppUser normalUser;
    private Folder folder;

    @BeforeEach
    void setUp() {
        company = Company.builder().id(1L).name("Test Company").build();
        representative = AppUser.builder()
                .id(1L)
                .username("rep")
                .role(AppUser.UserRole.REPRESENTATIVE)
                .company(company)
                .build();
        normalUser = AppUser.builder()
                .id(2L)
                .username("user")
                .role(AppUser.UserRole.USER)
                .company(company)
                .build();
        folder = Folder.builder()
                .id(1L)
                .name("Test Folder")
                .maxFileSize(1024L) // 1KB limit
                .company(company)
                .build();
    }

    @Test
    void testCreateFolder_ByRepresentative() {
        // given
        when(userRepository.findById(1L)).thenReturn(Optional.of(representative));
        when(folderRepository.save(any(Folder.class))).thenReturn(folder);

        // when
        Folder created = cabinetService.createFolder(1L, "New Folder", 5000L);

        // then
        assertNotNull(created);
        verify(folderRepository, times(1)).save(any(Folder.class));
    }

    @Test
    void testCreateFolder_ByNormalUser_ShouldFail() {
        // given
        when(userRepository.findById(2L)).thenReturn(Optional.of(normalUser));

        // when & then
        assertThrows(RuntimeException.class, () -> {
            cabinetService.createFolder(2L, "New Folder", 5000L);
        });
    }

    @Test
    void testGrantPermission() {
        // given
        when(userRepository.findById(1L)).thenReturn(Optional.of(representative));
        when(folderRepository.findById(1L)).thenReturn(Optional.of(folder));
        when(userRepository.findById(2L)).thenReturn(Optional.of(normalUser));
        when(permissionRepository.existsByFolderAndUser(folder, normalUser)).thenReturn(false);

        // when
        cabinetService.grantPermission(1L, 1L, 2L);

        // then
        verify(permissionRepository, times(1)).save(any(FolderPermission.class));
    }

    @Test
    void testUploadFile_WithinSizeLimit() throws IOException {
        // given
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", new byte[512]); // 512B
        when(userRepository.findById(1L)).thenReturn(Optional.of(representative));
        when(folderRepository.findById(1L)).thenReturn(Optional.of(folder));
        when(fileRepository.save(any(CabinetFile.class))).thenReturn(new CabinetFile());

        // when
        CabinetFile uploaded = cabinetService.uploadFile(1L, 1L, file);

        // then
        assertNotNull(uploaded);
    }

    @Test
    void testUploadFile_ExceedSizeLimit_ShouldFail() {
        // given
        MockMultipartFile file = new MockMultipartFile("file", "large.txt", "text/plain", new byte[2048]); // 2KB
        when(userRepository.findById(1L)).thenReturn(Optional.of(representative));
        when(folderRepository.findById(1L)).thenReturn(Optional.of(folder));

        // when & then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            cabinetService.uploadFile(1L, 1L, file);
        });
        assertTrue(exception.getMessage().contains("File size exceeds the limit"));
    }
}
