package cn.edu.seu.alumni_server;

import cn.edu.seu.alumni_server.common.Utils;
import cn.edu.seu.alumni_server.common.dto.WebResponse;
import cn.edu.seu.alumni_server.controller.CommonController;
import cn.edu.seu.alumni_server.service.ActivityService;
import cn.edu.seu.alumni_server.service.AlumniCircleService;
import cn.edu.seu.alumni_server.service.QCloudFileManager;
import java.io.File;
import java.io.IOException;
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
	@Autowired
	CommonController commonController;
	@Autowired
	AlumniCircleService alumniCircleService;

	@Test
	public void testUploadOneFile() {
		// 获取图片
		File file = new File(
			"F:\\JavaProjects\\AlumniServer\\doc\\demo.png"
		);
		try {
			MultipartFile multipartFile = Utils.fileToMultipartFile(file);
			String ansUrl = this.fileManager.uploadOneFile(
				multipartFile, "test-img-02",
				"test", "imgs"
			);
			System.out.println(ansUrl);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testCommonController() {
		// 获取图片
		File file = new File(
			"F:\\JavaProjects\\AlumniServer\\doc\\demo.png"
		);
		try {
			MultipartFile multipartFile = Utils.fileToMultipartFile(file);
			WebResponse ansUrl = this.commonController.uploadFile(
				multipartFile
			);
			System.out.println(ansUrl);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testDeleteFile() {
		System.out.println(this.commonController.deleteFile(
			"https://root-test-bucket-1258701411.cos.ap-shanghai.myqcloud.com/11813611470848.jpg"));
	}

	@Test
	public void testAlumniCircleRecommand() {
		System.out.println(alumniCircleService.alumniCirclesRecommend());
	}

}
