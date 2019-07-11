package com.license.tester.db.repo;

import com.license.tester.db.model.Teacher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "tester.db.recreate", havingValue = "true")
public class DbSeeder implements CommandLineRunner {

    private final TeacherRepository teacherRepository;

    public DbSeeder(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Teacher admin = new Teacher("admin", "admin@admin.com", "$2a$10$AjHGc4x3Nez/p4ZpvFDWeO6FGxee/cVqj5KHHnHfuLnIOzC5ag4fm");
        teacherRepository.save(admin);
    }
}
