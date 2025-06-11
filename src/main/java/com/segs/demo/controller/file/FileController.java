package com.segs.demo.controller.file;

import com.segs.demo.model.StudentGradeDTO;
import com.segs.demo.repository.TermCourseRepository;
import com.segs.demo.model.ResponseMessage;
import com.segs.demo.service.FileService;
import com.segs.demo.service.GradeService;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/grades")
public class FileController {

    @Autowired
    private FileService fileService;

    @Autowired
    private GradeService gradeService; // <-- Use your existing grade saving service

    @Autowired
    private TermCourseRepository termCourseRepository;

    @PostMapping("/uploadcsv")
public ResponseEntity<ResponseMessage> uploadFile(
        @RequestParam("file") MultipartFile file,
        HttpSession session) {

    Long trmid = (Long) session.getAttribute("TRMID");
    Long crsid = (Long) session.getAttribute("CRSID");
    Long examTypeId = (Long) session.getAttribute("examTypeId");

    if (trmid == null || crsid == null || examTypeId == null) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseMessage("Session expired or context missing (TRMID/CRSID/examTypeId)."));
    }

    // Compute tcrid from crsid and trmid
    Long tcrid = termCourseRepository.findTcridByCrsidAndTrmid(crsid, trmid);
    if (tcrid == null) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseMessage("Invalid Term-Course combination."));
    }

    if (!fileService.hasCsvFormat(file)) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseMessage("Invalid file format. Please upload a CSV file."));
    }

    try {
        List<StudentGradeDTO> gradesList = fileService.csvToStudentGradeDTOs(file.getInputStream());

        if (gradesList == null || gradesList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(new ResponseMessage("CSV file is empty or improperly formatted."));
        }

        gradeService.saveOrUpdateGrades(gradesList, tcrid, examTypeId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseMessage("Grades uploaded successfully: " + file.getOriginalFilename()));
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ResponseMessage("Upload failed: " + e.getMessage()));
    }
}

}
