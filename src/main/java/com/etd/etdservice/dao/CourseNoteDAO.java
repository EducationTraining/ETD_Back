package com.etd.etdservice.dao;

import com.etd.etdservice.bean.course.CourseNote;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CourseNoteDAO {
    /**
     * 发布 note
     * @param note
     * @return
     */
    boolean publishNote(CourseNote note);

    /**
     * 根据 CourseNote 更新该 CourseNote
     * @param note
     * @return
     */
    boolean updateNote(CourseNote note);

    /**
     * 删除指定的 note
     * @param id note的id
     * @return
     */
    boolean deleteNote(int id);

    /**
     * 根据公告id返回该课程信息
     * @param id 公告的id
     * @return 该公告
     */
    CourseNote getNoteById(int id);

    /**
     * 根据课程 id 返回所有的 note.
     * @param courseId
     * @return 符合要求的 note
     */
    List<CourseNote> getAllNotes(int courseId);

    /**
     * 根据课程 id 和 note的类型返回所有的 note
     * @param courseId 课程 id
     * @param type 课程类型
     * @return 符合要求的 note
     */
    List<CourseNote> getAllNotesWithSpecificType(@Param("courseId") int courseId, @Param("type") int type);

}
