package com.ec2.main.service;

import com.ec2.main.model.Courses;
import com.ec2.main.repository.CoursesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CourseService {

    @Autowired
    private CoursesRepository courseRepository;

    // Get all courses
    public List<Courses> getAllCourses() {
        return courseRepository.findAll();
    }

    // Get a single course by ID
    public Courses getCourseById(Long id) {
        return courseRepository.findById(id).orElse(null);
    }

    // Add a new course
    public Courses addCourse(Courses course) {
        if (course.getCrsid() == null) {
            // Manually generate a new crsid
            Long maxId = courseRepository.findMaxCrsid();
            course.setCrsid(maxId + 1);
        }

        // Set default values for NOT NULL columns
        if (course.getCrslongdesc() == null) course.setCrslongdesc("");
        if (course.getCrstitle() == null) course.setCrstitle("");
        if (course.getCrsurl() == null) course.setCrsurl("");
        if (course.getCrsprerequisites() == null) course.setCrsprerequisites("");
        if (course.getCrsequivalents() == null) course.setCrsequivalents("");
        if (course.getCrsrowstate() == null) course.setCrsrowstate(1L); // active by default
        if (course.getCrscreatedat() == null) course.setCrscreatedat(LocalDateTime.now());
        if (course.getCrslastupdatedat() == null) course.setCrslastupdatedat(LocalDateTime.now());
        if (course.getCrslectures() == null) course.setCrslectures(BigDecimal.ZERO);
        if (course.getCrstutorials() == null) course.setCrstutorials(BigDecimal.ZERO);
        if (course.getCrspracticals() == null) course.setCrspracticals(BigDecimal.ZERO);
        if (course.getCrscreditpoints() == null) course.setCrscreditpoints(BigDecimal.ZERO);
        if (course.getCrsmarks() == null) course.setCrsmarks(0L);
        if (course.getCrscreatedby() == null) course.setCrscreatedby(0L);
        if (course.getCrslastupdatedby() == null) course.setCrslastupdatedby(0L);

        return courseRepository.save(course);
    }

    // Update an existing course (full update)
    public Courses updateCourse(Long id, Courses updatedCourse) {
        Courses existing = getCourseById(id);
        if (existing != null) {
            existing.setCrsname(updatedCourse.getCrsname());
            existing.setCrstitle(updatedCourse.getCrstitle());
            existing.setCrscode(updatedCourse.getCrscode());
            existing.setCrsdiscipline(updatedCourse.getCrsdiscipline());
            existing.setCrsassessmenttype(updatedCourse.getCrsassessmenttype());
            existing.setCrslectures(updatedCourse.getCrslectures());
            existing.setCrstutorials(updatedCourse.getCrstutorials());
            existing.setCrspracticals(updatedCourse.getCrspracticals());
            existing.setCrscreditpoints(updatedCourse.getCrscreditpoints());
            existing.setCrsmarks(updatedCourse.getCrsmarks());
            existing.setCrslastupdatedat(LocalDateTime.now());
            return courseRepository.save(existing);
        }
        return null;
    }

    // Delete course
    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }

    // ✅ Archive course (rowstate +900)
    public void archiveCourse(Long id) {
        Courses course = courseRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Course not found"));

        if (course.getCrsrowstate() == null) {
            course.setCrsrowstate(0L);
        }

        // Add 900 to move it into archived range
        course.setCrsrowstate(course.getCrsrowstate() + 900);
        course.setCrslastupdatedat(LocalDateTime.now());
        courseRepository.save(course);
    }

    // ✅ Restore archived course (rowstate -900)
    public void restoreCourse(Long id) {
        Courses course = courseRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Course not found"));

        Long currentState = course.getCrsrowstate();
        if (currentState != null && currentState >= 900) {
            course.setCrsrowstate(currentState - 900);
        }

        course.setCrslastupdatedat(LocalDateTime.now());
        courseRepository.save(course);
    }

    public List<Courses> getArchivedCourses() {
        return courseRepository.findArchivedCourses();
    }

    // ✅ Search active (non-archived) courses
    public Page<Courses> searchCourses(String keyword, Pageable pageable) {
        if (keyword == null || keyword.trim().isEmpty()) {
            // Only show active (not archived) courses
            return courseRepository.findByCrsrowstateGreaterThan(0, pageable);
        }
        return courseRepository.searchCourses(keyword, pageable);
    }
}
