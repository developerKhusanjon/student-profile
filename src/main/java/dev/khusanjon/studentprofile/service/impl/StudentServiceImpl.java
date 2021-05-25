package dev.khusanjon.studentprofile.service.impl;

import dev.khusanjon.studentprofile.model.Student;
import dev.khusanjon.studentprofile.repository.StudentRepository;
import dev.khusanjon.studentprofile.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {
	
	@Autowired
	private StudentRepository studentRepository;
	
	@Override
	public Student save(Student entity) {
		return studentRepository.save(entity);
	}

	@Override
	public Student update(Student entity) {
		return studentRepository.save(entity);
	}

	@Override
	public void delete(Student entity) {
		studentRepository.delete(entity);
	}

	@Override
	public void delete(Long id) {
		studentRepository.deleteById(id);
	}

	@Override
	public Student find(Long id) {
		return studentRepository.findById(id).get();
	}

	@Override
	public List<Student> findAll() {
		return studentRepository.findAll();
	}

	@Override
	public boolean authenticate(String username, String password){
		Student user = this.findByEmail(username);
		if(user == null){
			return false;
		}else{
			return password.equals(user.getPassword());
		}
	}

	@Override
	public Student findByEmail(String email) {
		return studentRepository.findByEmail(email);
	}

	@Override
	public void deleteInBatch(List<Student> users) {
		studentRepository.deleteInBatch(users);
	}
	
}
