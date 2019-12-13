package com.etd.etdservice.dao;

import com.etd.etdservice.bean.users.Admin;

public interface AdminDAO {
	/**
	 * query admin by id
	 * @param id
	 * @return a Admin object
	 */
	Admin queryById(int id);

	/**
	 * query admin by userName
	 * @param userName
	 * @return a Admin object
	 */
	Admin queryByUserName(String userName);

	/**
	 * query admin by sessionKey
	 * @param sessionKey
	 * @return a Admin object
	 */
	Admin queryBySessionKey(String sessionKey);
	
	/**
	 * update admin
	 * @param admin a Admin Object
	 * @return update success or not
	 */
	boolean update(Admin admin);

	/**
	 * insert a admin
	 * @param admin
	 * @return insert success or not
	 */
	boolean create(Admin admin);
}
