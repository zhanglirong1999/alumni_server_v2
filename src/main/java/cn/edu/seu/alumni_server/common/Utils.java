package cn.edu.seu.alumni_server.common;

import cn.edu.seu.alumni_server.common.config.AccessTokenConfig;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import javax.net.ssl.HttpsURLConnection;
import org.apache.http.entity.ContentType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

public class Utils {

	static IdGenerator idGenerator;
	static String access_token = "";
	static long expireTime = 0l;

	static {
//        idGenerator = new SnowflakeIdGenerator(0, 0);
		idGenerator = new IdGenerator();
	}

	public static long generateId() {
		return idGenerator.nextId();
	}

	public static MultipartFile fileToMultipartFile(File file) throws IOException {
		FileInputStream inputStream = new FileInputStream(file);
		return new MockMultipartFile(file.getName(), file.getName(),
			ContentType.APPLICATION_OCTET_STREAM.toString(), inputStream);
	}

	public static Date addEightHours(Date date) {
		return new Date(date.getTime() + 8 * 60 * 60 * 1000);
	}

	public static Boolean deleteFileUnderProjectDir(String fileName) {
		// 然后应该删除项目目录下的本地文件
		File targetFile = new File(System.getProperty("user.dir") + File.separator + fileName);
		return targetFile.delete();
	}

	public static String getAccessToken() {
		return AccessTokenConfig.getAccessToken();
	}

    public static boolean isSameAvatar(String avatar1, String avatar2)
        throws IOException {
	    if (avatar1 == null || !avatar1.startsWith("https://"))
	        throw new IOException("头像路径非法");
	    if (avatar2 == null || !avatar2.startsWith("https://"))
            throw new IOException("头像路径非法");
	    if (avatar1.equals(avatar2))
	        return true;
	    URL url1 = new URL(avatar1), url2 = new URL(avatar2);
        HttpsURLConnection conn1 =
            (HttpsURLConnection) url1.openConnection(), conn2 =
            (HttpsURLConnection) url2.openConnection();
        InputStream in1 = conn1.getInputStream(),
            in2 = conn2.getInputStream();
        byte[] bs1 = in1.readAllBytes(), bs2 = in2.readAllBytes();
        return Arrays.compare(bs1, bs2) == 0;
    }
}
