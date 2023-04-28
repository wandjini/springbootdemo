package com.dirusptivetech.demo.student;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public  class StudentService {
    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getStudents() {
        return this.studentRepository.findAll();
    }

    public void addNewStudent(Student student) {
        Optional<Student> studentByEmail = this.studentRepository.findStudentByEmail(student.getEmail());
        if (studentByEmail.isPresent()){
            throw new IllegalStateException( "Email already taken");

        }

        this.studentRepository.save(student);
    }

    public void deleteStudent(Long studentId) {
        Student existingStudent = this.studentRepository.getReferenceById(studentId);
        if (!this.studentRepository.existsById(studentId) )
            throw new IllegalStateException("Student not exists");
        this.studentRepository.delete(existingStudent);
    }

    @Transactional
    public void updateStudent(Long studentId, String name, String email) {

            Optional<Student> studentOptional = this.studentRepository.findById(studentId);
            if (studentOptional.isPresent()){
                if (email!=null && email.length()> 0){
                    Optional stOpt = this.studentRepository.findStudentByEmail(email);
                    if (stOpt.isPresent()){
                        throw new IllegalStateException("Email already taken");
                    }
                    studentOptional.get().setEmail(email);
                }
                if (name != null && name.length() > 0)
                    studentOptional.get().setName(name);
            }
            else {throw new IllegalStateException("Student doesn't exist");}

    }
}
