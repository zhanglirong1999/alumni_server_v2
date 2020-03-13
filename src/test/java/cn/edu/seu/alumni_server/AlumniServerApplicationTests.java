package cn.edu.seu.alumni_server;

import cn.edu.seu.alumni_server.common.Utils;
import cn.edu.seu.alumni_server.controller.CommonController;
import cn.edu.seu.alumni_server.service.AccountService;
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
	AccountService accountService;

	@Autowired
	QCloudFileManager fileManager;
	@Autowired
	ActivityService activityService;
	@Autowired
	CommonController commonController;
	@Autowired
	AlumniCircleService alumniCircleService;

	@Test
	public void testCommonController() {
		// 获取图片
		File file = new File(
			"F:\\JavaProjects\\AlumniServer\\test.jpg"
		);
		try {
			MultipartFile multipartFile = Utils.fileToMultipartFile(file);
			Object ansUrl = this.commonController.uploadFile(
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

	@Test
	public void testUploadFile() {
		try {
			String s = fileManager.saveAccountAvatar(
				"https://wx.qlogo.cn/mmopen/vi_32/O6qftWBakkgESo8qNUDjTMa5FQbAzC0nibIQxJTjxnXickWE7iaVm1tgCuQz198FTC8k2zeHicOlYv9tmld9v9o55Q/132",
				Utils.generateId() + ".png"
			);
			System.out.println(s);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testTransAvatar() throws IOException {
		this.accountService.transAccountAvatarFromURLToURL();
	}

}
