package dev.khusanjon.studentprofile.service;

import dev.khusanjon.studentprofile.generic.GenericService;
import dev.khusanjon.studentprofile.model.Student;


public interface StudentService extends GenericService<Student> {

	boolean authenticate(String email, String password);
	
	Student findByEmail(String email);
	
}
