package com.etd.etdservice.trial;

import com.etd.etdservice.dao.StudentDAO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static junit.framework.TestCase.assertNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserTrial {
	@Autowired
	private StudentDAO dao;

	@Test
	public void testGetNonExistUser() {
		assertNull(dao.queryByUserName("asdsadasdasdasd"));
	}
}
