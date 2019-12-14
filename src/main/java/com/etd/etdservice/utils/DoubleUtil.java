package com.etd.etdservice.utils;

import java.util.Random;

public class DoubleUtil {
	/**
	 * 生成max到min范围的浮点数
	 * */
	public static double nextDouble(final double min, final double max) {
		return min + ((max - min) * new Random().nextDouble());
	}
}
