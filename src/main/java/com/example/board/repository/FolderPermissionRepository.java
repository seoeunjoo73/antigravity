package com.example.board.repository;

import com.example.board.entity.FolderPermission;
import com.example.board.entity.Folder;
import com.example.board.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface FolderPermissionRepository extends JpaRepository<FolderPermission, Long> {
    Optional<FolderPermission> findByFolderAndUser(Folder folder, AppUser user);

    void deleteByFolderAndUser(Folder folder, AppUser user);

    boolean existsByFolderAndUser(Folder folder, AppUser user);
}
