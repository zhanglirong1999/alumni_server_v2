package cn.edu.seu.alumni_server;

import cn.edu.seu.alumni_server.common.Utils;
import cn.edu.seu.alumni_server.common.exceptions.ActivityServiceException;
import cn.edu.seu.alumni_server.controller.dto.ActivityWithMultipartFileDTO;
import cn.edu.seu.alumni_server.dao.entity.Activity;
import cn.edu.seu.alumni_server.service.ActivityService;
import cn.edu.seu.alumni_server.service.QCloudFileManager;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
	AlumniServerApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AlumniServerApplicationTests {

	@Autowired
	QCloudFileManager fileManager;
	@Autowired
	ActivityService activityService;

	@Test
	public void testPostImgs() {
		// 获取图片
		File file = new File(
			"F:\\71118123\\照片\\coma.jpg"
		);
		try {
			MultipartFile multipartFile = Utils.fileToMultipartFile(file);
			ActivityWithMultipartFileDTO x = new ActivityWithMultipartFileDTO(
				7647332456448L, null, null, null, null, null, null,
				null, multipartFile, null, null, null, null, null, null
			);
			// 首先判断是否有效, 并且发送不为 null 的图片.
			Activity ans = this.activityService.updateActivityDAO(x);
			// 然后选择性更新.
			this.activityService.updateActivity(ans);
		} catch (IOException | IllegalAccessException | ActivityServiceException | InvocationTargetException e) {
			e.printStackTrace();
		}
//		System.out.println("1123");
	}

}
