package com.ec2.main.service;

import com.ec2.main.model.*;
import org.springframework.data.domain.Page;
import java.util.List;

public interface facultyService {

    List<Terms> getAllTerms();
    List<Egcrstt1> getAllExamTypes();
    List<AcademicYears> getAllAcademicYears();
    List<Courses> getAllCourses();



    // Non-paginated versions
    List<Users> getAllFaculties();
    List<Users> searchFaculties(String keyword);
}
