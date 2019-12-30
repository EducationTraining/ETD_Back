package com.etd.etdservice.trial;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class JSONTrial {
	public static void main(String[] args) {
		JSONArray exampleArray = new JSONArray();
		for (int i=1; i<=5; i++) {
			JSONObject firstObj = new JSONObject();
			firstObj.put("title", "title" + i);
			JSONArray subcoursesArray = new JSONArray();
			for (int j=1; j<=2; j++) {
				JSONObject subcourseObj = new JSONObject();
				subcourseObj.put("title", "title" + i + "." + j);
				subcoursesArray.add(subcourseObj);
			}
			firstObj.put("subcourses", subcoursesArray);
			exampleArray.add(firstObj);
		}
		String exampleArrayStr = exampleArray.toJSONString();
		System.out.println(exampleArrayStr);
		processArray(exampleArrayStr);
	}

	private static void processArray(String arrayStr) {
		JSONArray exampleArray = JSON.parseArray(arrayStr);
		int count = 0;
		// 遍历每一个一级子课程
		for (int i=0; i<exampleArray.size(); i++) {
			JSONObject firstSubcourseObj = exampleArray.getJSONObject(i);
			// 为每个一级子课程查询子课程信息
			firstSubcourseObj.put("id", count++);
			// 遍历二级子课程
			JSONArray secondSubcourses = firstSubcourseObj.getJSONArray("subcourses");
			for (int j=0; j<secondSubcourses.size(); j++) {
				// 为每个二级子课程查询子课程信息
				JSONObject secondSubcourseObj = secondSubcourses.getJSONObject(j);
				secondSubcourseObj.put("id", count++);
			}
		}
		System.out.println(exampleArray.toJSONString());
	}
}
