package com.examly.springapp.controller;

import com.examly.springapp.dto.TimetableRequestDTO;
import com.examly.springapp.entity.Teacher;
import com.examly.springapp.entity.Timetable;
import com.examly.springapp.service.TimetableService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "Timetable", description = "Timetable management and clash detection")
@RestController
@RequestMapping("/api/timetable")
public class TimetableController {

    private final TimetableService timetableService;

    public TimetableController(TimetableService timetableService) {
        this.timetableService = timetableService;
    }

    @Operation(summary = "Create a new timetable slot")
    @PostMapping("/generate")
    public ResponseEntity<Timetable> createSlot(@Valid @RequestBody TimetableRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(timetableService.createSlot(dto));
    }

    @Operation(summary = "Get class timetable")
    @GetMapping("/class/{classSection}")
    public ResponseEntity<List<Timetable>> getClassTimetable(@PathVariable String classSection) {
        return ResponseEntity.ok(timetableService.getClassTimetable(classSection));
    }

    @Operation(summary = "Get teacher timetable")
    @GetMapping("/teacher/{id}")
    public ResponseEntity<List<Timetable>> getTeacherTimetable(@PathVariable Long id) {
        return ResponseEntity.ok(timetableService.getTeacherTimetable(id));
    }

    @Operation(summary = "Detect all scheduling clashes")
    @GetMapping("/clashes")
    public ResponseEntity<List<Timetable>> getClashes() {
        return ResponseEntity.ok(timetableService.detectClashes());
    }

    @Operation(summary = "Update a timetable slot")
    @PutMapping("/{id}")
    public ResponseEntity<Timetable> updateSlot(@PathVariable Long id,
                                                @Valid @RequestBody TimetableRequestDTO dto) {
        return ResponseEntity.ok(timetableService.updateSlot(id, dto));
    }

    @Operation(summary = "Suggest substitute teachers for an absent teacher on a given day and period")
    @GetMapping("/substitutes")
    public ResponseEntity<List<Teacher>> suggestSubstitutes(
            @RequestParam Long absentTeacherId,
            @RequestParam String day,
            @RequestParam int period) {
        return ResponseEntity.ok(timetableService.suggestSubstitutes(absentTeacherId, day, period));
    }
}
