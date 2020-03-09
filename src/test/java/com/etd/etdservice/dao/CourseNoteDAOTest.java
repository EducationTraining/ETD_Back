package com.etd.etdservice.dao;

import com.etd.etdservice.bean.course.Course;
import com.etd.etdservice.bean.course.CourseNote;
import com.etd.etdservice.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class CourseNoteDAOTest {
    @Autowired
    private static CourseDAO courseDAO;

    @Autowired
    private CourseNoteDAO courseNoteDAO;

    private Course course;

    private CourseNote mockCourseNote() {
        return mockCourseNote(null, null);
    }

    private CourseNote mockCourseNote(Integer courseId) {
        return mockCourseNote(courseId, null);
    }

    /**
     * 返回模拟的课程公告
     * @param courseId 课程id
     * @param type 公告类型
     * @return
     */
    private CourseNote mockCourseNote(Integer courseId, Integer type) {
        CourseNote note = new CourseNote();
        if (courseId == null) {
            courseId = new Random().nextInt(100000) + 999;
        }
        note.setCourseId(courseId);
        note.setNote(StringUtil.generateRandomString("note: "));
        note.setCreateTime(new Date());
        note.setPublishTime(new Date());
        if (type == null) {
            type = new Random().nextInt(3) + 1;
        }
        note.setType(type);
        log.debug(note.toString());
        return note;
    }

    @Test
    public void publishNote() {
        CourseNote note = mockCourseNote();
        assertNotNull(note);
        assertTrue(courseNoteDAO.publishNote(note));
    }

    @Test
    public void updateNote() {
        CourseNote note = mockCourseNote();
        courseNoteDAO.publishNote(note);
        note.setNote(StringUtil.generateRandomString("note: "));
        assertTrue(courseNoteDAO.updateNote(note));
        CourseNote newOne = courseNoteDAO.getNoteById(note.getId());
        assertEquals(note.getNote(), newOne.getNote());
    }

    @Test
    public void deleteNote() {
        CourseNote note = mockCourseNote();
        courseNoteDAO.publishNote(note);
        assertTrue(courseNoteDAO.deleteNote(note.getId()));
        note = courseNoteDAO.getNoteById(note.getId());
        assertNull(note);
    }

    @Test
    public void getNoteById() {
        CourseNote note = mockCourseNote();
        courseNoteDAO.publishNote(note);
        log.debug(note.toString());
        System.out.println(note.toString());
        CourseNote newNote = courseNoteDAO.getNoteById(note.getId());
        assertEquals(note.getNote(), newNote.getNote());
    }

    @Test
    public void getAllNotes() {
        int courseId = new Random().nextInt(100000) + 999;
        for (int i  = 0; i < 5; ++i) {
            CourseNote note = mockCourseNote(courseId);
            courseNoteDAO.publishNote(note);
        }
        List<CourseNote> notes = courseNoteDAO.getAllNotes(courseId);
        assertTrue(notes.size() >= 5);
    }

    @Test
    public void getAllNotesWithSpecificType() {
        int courseId = new Random().nextInt(100000) + 999;
        int type = new Random().nextInt(3) + 1;
        for (int i  = 0; i < 5; ++i) {
            CourseNote note = mockCourseNote(courseId, type);
            courseNoteDAO.publishNote(note);
        }
        List<CourseNote> notes = courseNoteDAO.getAllNotesWithSpecificType(courseId, type);
        assertTrue(notes.size() >= 5);
        for (int i = 0; i < notes.size(); ++i) {
            assertEquals(type, notes.get(i).getType());
        }
    }
}
