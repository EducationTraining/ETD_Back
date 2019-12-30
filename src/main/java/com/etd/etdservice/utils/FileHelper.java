package com.etd.etdservice.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Slf4j
public class FileHelper {
	/**
	 * 上传一张图片并返回它的Url
	 * @param file
	 * @param imageRootPath 图片默认存放根路径
	 * @param type 存放图片的种类(为其单独建立文件夹)
	 * @param urlStarter
	 * @return
	 * @throws Exception
	 */
	public static String uploadPic(MultipartFile file, String imageRootPath, String type, String urlStarter) throws Exception {
        if (file.isEmpty()) {
	        throw new RuntimeException("文件为空");
        }
        String fileName = file.getOriginalFilename();
		// 图片在服务器上实际存放的路径
        String filePath = imageRootPath + "/" + type + "/" + fileName;
        String picUrl = urlStarter + "/images/" + type + "/" + fileName;
        log.info("文件上传路径：" + filePath);
        log.info("文件获取路径：" + picUrl);
        // 保存文件
        File dest = new File(filePath);
        // 检测是否存在目录
        if (!dest.getParentFile().exists()) {
            // 假如文件不存在即重新创建新的文件已防止异常发生
            dest.getParentFile().mkdirs();
        }
        file.transferTo(dest);
        
        return picUrl;
	}

	public static String uploadVideo(MultipartFile file, String videoRootPath, Integer subcourseId, String urlStarter) throws Exception{
		if (file.isEmpty()) {
			throw new RuntimeException("文件为空");
		}
		String fileName = file.getOriginalFilename();
		fileName = subcourseId + "_" + fileName;
		// 图片在服务器上实际存放的路径
		String filePath = videoRootPath + "/" + fileName;
		String videoUrl = urlStarter + "/video/" + fileName;
		log.info("文件上传路径：" + filePath);
		log.info("文件获取路径：" + videoUrl);
		// 保存文件
		File dest = new File(filePath);
		// 检测是否存在目录
		if (!dest.getParentFile().exists()) {
			// 假如文件不存在即重新创建新的文件已防止异常发生
			if (!dest.getParentFile().mkdirs()) {
				throw new RuntimeException("Cannot create dir" + dest.getAbsolutePath());
			}
		}
		file.transferTo(dest);

		return videoUrl;
	}
}
