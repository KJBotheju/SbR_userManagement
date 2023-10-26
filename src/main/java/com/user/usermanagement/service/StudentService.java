package com.user.usermanagement.service;

import com.user.usermanagement.exception.StudentAlreadyExistsException;
import com.user.usermanagement.exception.StudentNotFoundException;
import com.user.usermanagement.model.Student;
import com.user.usermanagement.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService implements IStudentService{
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public List<Student> getStudents() {
        return studentRepository.findAll();
    }

    @Override
    public Student addStudent(Student student) {
        if(studentAlreadyExists(student.getEmail())){
            throw new StudentAlreadyExistsException(student.getEmail() + " already exists");
        }
        return studentRepository.save(student);
    }

    @Override
    public Student updateStudent(Student student, Long id) {
        return studentRepository.findById(id).map(st ->{
            st.setFirstName(st.getFirstName());
            st.setLastName(st.getLastName());
            st.setEmail(st.getEmail());
            st.setFaculty(st.getFaculty());
            return studentRepository.save(st);
        }).orElseThrow(()->new StudentNotFoundException("Sorry, this student could not found"));
    }

    @Override
    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(()->new StudentNotFoundException("Sorry, no student find with this id : " + id));
    }

    @Override
    public void deleteStudent(Long id) {

    }

    private boolean studentAlreadyExists(String email) {
        return studentRepository.findByEmail(email).isPresent();
    }

}
