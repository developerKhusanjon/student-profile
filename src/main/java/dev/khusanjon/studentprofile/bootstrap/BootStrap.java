package dev.khusanjon.studentprofile.bootstrap;

import dev.khusanjon.studentprofile.model.Student;
import dev.khusanjon.studentprofile.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class BootStrap implements CommandLineRunner {

    @Autowired
    private StudentService studentService;

    @Override
    public void run(String... strings) throws Exception {
        Student student = new Student();
        student.setDob(LocalDateTime.now().toLocalDate());
        student.setEmail("dev@mail.ru");
        student.setFirstName("Husanjon");
        student.setLastName("Tuychiyev");
        student.setGender("Male");
        student.setPassword("1111");
        student.setRole("User");

        Student student1 = new Student();
        student1.setDob(LocalDateTime.now().toLocalDate());
        student1.setEmail("devops@mail.ru");
        student1.setFirstName("Ahmadjon");
        student1.setLastName("Aliyev");
        student1.setGender("Male");
        student1.setPassword("1111");
        student1.setRole("User");

        studentService.save(student);
        studentService.save(student1);
    }
}
