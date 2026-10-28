package com.examly.springapp.service;

import com.examly.springapp.dto.TimetableRequestDTO;
import com.examly.springapp.entity.Teacher;
import com.examly.springapp.entity.Timetable;
import com.examly.springapp.entity.Timetable.Day;
import com.examly.springapp.exception.ResourceNotFoundException;
import com.examly.springapp.exception.TimetableClashException;
import com.examly.springapp.repository.TeacherRepository;
import com.examly.springapp.repository.TimetableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class TimetableService {

    private final TimetableRepository timetableRepository;
    private final TeacherRepository teacherRepository;

    public TimetableService(TimetableRepository timetableRepository, TeacherRepository teacherRepository) {
        this.timetableRepository = timetableRepository;
        this.teacherRepository = teacherRepository;
    }

    @Transactional
    public Timetable createSlot(TimetableRequestDTO dto) {
        Teacher teacher = teacherRepository.findById(dto.getTeacherId())
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with ID: " + dto.getTeacherId()));

        Day day = Day.valueOf(dto.getDay().toUpperCase());

        if (timetableRepository.existsByTeacherIdAndDayAndPeriodAndActiveTrue(dto.getTeacherId(), day, dto.getPeriod())) {
            throw new TimetableClashException("Teacher already assigned at this time slot — cannot double-book");
        }

        Timetable slot = new Timetable(dto.getClassSection(), day, dto.getPeriod(), dto.getSubject(), dto.getClassroom(), teacher);
        return timetableRepository.save(slot);
    }

    public List<Timetable> getClassTimetable(String classSection) {
        return timetableRepository.findByClassSectionAndActiveTrue(classSection);
    }

    public List<Timetable> getTeacherTimetable(Long teacherId) {
        return timetableRepository.findByTeacherIdAndActiveTrue(teacherId);
    }

    public List<Timetable> detectClashes() {
        return timetableRepository.findAllClashes();
    }

    @Transactional
    public Timetable updateSlot(Long id, TimetableRequestDTO dto) {
        Timetable slot = timetableRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Timetable slot not found with ID: " + id));

        Day day = Day.valueOf(dto.getDay().toUpperCase());
        boolean clashExists = timetableRepository
                .existsByTeacherIdAndDayAndPeriodAndActiveTrue(dto.getTeacherId(), day, dto.getPeriod());
        if (clashExists && !slot.getTeacher().getId().equals(dto.getTeacherId())) {
            throw new TimetableClashException("Teacher already assigned at this time slot — cannot double-book");
        }

        Teacher teacher = teacherRepository.findById(dto.getTeacherId())
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with ID: " + dto.getTeacherId()));

        slot.setClassSection(dto.getClassSection());
        slot.setDay(day);
        slot.setPeriod(dto.getPeriod());
        slot.setSubject(dto.getSubject());
        slot.setClassroom(dto.getClassroom());
        slot.setTeacher(teacher);
        return timetableRepository.save(slot);
    }

    // FR4/FR6: Substitute auto-suggestion — teachers with no active slot on given day+period
    public List<Teacher> suggestSubstitutes(Long absentTeacherId, String day, int period) {
        Day dayEnum = Day.valueOf(day.toUpperCase());
        List<Timetable> absentSlots = timetableRepository.findByTeacherIdAndDayAndActiveTrue(absentTeacherId, dayEnum);
        List<Long> busyTeacherIds = timetableRepository
                .findByDayAndPeriodAndActiveTrue(dayEnum, period)
                .stream()
                .map(t -> t.getTeacher().getId())
                .toList();
        return teacherRepository.findAll().stream()
                .filter(t -> !t.getId().equals(absentTeacherId))
                .filter(t -> !busyTeacherIds.contains(t.getId()))
                .toList();
    }
}
