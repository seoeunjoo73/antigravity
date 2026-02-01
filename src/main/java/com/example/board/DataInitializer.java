package com.example.board;

import com.example.board.entity.AppUser;
import com.example.board.entity.Company;
import com.example.board.repository.AppUserRepository;
import com.example.board.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final CompanyRepository companyRepository;
    private final AppUserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        if (companyRepository.count() == 0) {
            Company company = Company.builder()
                    .name("Test Company")
                    .build();
            companyRepository.save(company);

            AppUser representative = AppUser.builder()
                    .username("admin")
                    .password("1234")
                    .role(AppUser.UserRole.REPRESENTATIVE)
                    .company(company)
                    .build();
            userRepository.save(representative);

            AppUser user = AppUser.builder()
                    .username("user1")
                    .password("1234")
                    .role(AppUser.UserRole.USER)
                    .company(company)
                    .build();
            userRepository.save(user);

            System.out.println("Initial data seeded: Company ID 1, Admin ID 1, User ID 2");
        }
    }
}
