package com.license.tester.service;

import com.license.tester.db.model.Student;
import com.license.tester.db.model.Teacher;
import com.license.tester.db.repo.StudentRepository;
import com.license.tester.db.repo.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class AuthUserDetailsService implements UserDetailsService {

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private StudentRepository studentRepository;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Student student = studentRepository.findByEmail(email);
        if (student == null) {
            Teacher teacher = teacherRepository.findByEmail(email);
            if (teacher == null) {
                throw new UsernameNotFoundException("User not found");
            }
            List<SimpleGrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("user"));
            return new User(teacher.getEmail(), teacher.getPassword(), authorities);
        }

        List<SimpleGrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("user"));
        return new User(student.getEmail(), student.getPassword(), authorities);
    }
}
