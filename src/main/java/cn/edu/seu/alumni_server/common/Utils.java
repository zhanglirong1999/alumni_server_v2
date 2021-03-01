package cn.edu.seu.alumni_server.common;

import cn.edu.seu.alumni_server.common.config.AccessTokenConfig;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletRequest;

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
		if (avatar1 == null || !avatar1.startsWith("https://")) {
			throw new IOException("头像路径非法");
		}
		if (avatar2 == null || !avatar2.startsWith("https://")) {
			throw new IOException("头像路径非法");
		}
		if (avatar1.equals(avatar2)) {
			return true;
		}
		URL url1 = new URL(avatar1), url2 = new URL(avatar2);
		HttpsURLConnection conn1 =
			(HttpsURLConnection) url1.openConnection(), conn2 =
			(HttpsURLConnection) url2.openConnection();
		InputStream in1 = conn1.getInputStream(),
			in2 = conn2.getInputStream();
		byte[] bs1 = readBytes(in1), bs2 = readBytes(in2);
		if (bs1 == null) {
			return bs2 == null;
		} else {
			if (bs1.length != bs2.length) {
				return false;
			}
			for (int i = 0; i < bs1.length; ++i) {
				if (bs1[i] != bs2[i]) {
					return false;
				}
			}
			return true;
		}
	}

	public static byte[] readBytes(InputStream in) throws IOException {
		byte[] temp = new byte[in.available()];
		byte[] result = new byte[0];
		int size = 0;
		while ((size = in.read(temp)) != -1) {
			byte[] readBytes = new byte[size];
			System.arraycopy(temp, 0, readBytes, 0, size);
			result = mergeArray(result, readBytes);
		}
		return result;
	}

	public static byte[] mergeArray(byte[]... a) {
		// 合并完之后数组的总长度
		int index = 0;
		int sum = 0;
		for (byte[] bytes : a) {
			sum = sum + bytes.length;
		}
		byte[] result = new byte[sum];
		for (byte[] bytes : a) {
			int lengthOne = bytes.length;
			if (lengthOne == 0) {
				continue;
			}
			// 拷贝数组
			System.arraycopy(bytes, 0, result, index, lengthOne);
			index = index + lengthOne;
		}
		return result;
	}

	public static String getRequestIP(HttpServletRequest request) {
		String ip = null;

		// X-Forwarded-For：Squid 服务代理
		String ipAddresses = request.getHeader("X-Forwarded-For");

		if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
			// Proxy-Client-IP：apache 服务代理
			ipAddresses = request.getHeader("Proxy-Client-IP");
		}

		if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
			// WL-Proxy-Client-IP：weblogic 服务代理
			ipAddresses = request.getHeader("WL-Proxy-Client-IP");
		}

		if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
			// HTTP_CLIENT_IP：有些代理服务器
			ipAddresses = request.getHeader("HTTP_CLIENT_IP");
		}

		if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
			// X-Real-IP：nginx服务代理
			ipAddresses = request.getHeader("X-Real-IP");
		}

		// 有些网络通过多层代理，那么获取到的ip就会有多个，一般都是通过逗号（,）分割开来，并且第一个ip为客户端的真实IP
		if (ipAddresses != null && ipAddresses.length() != 0) {
			ip = ipAddresses.split(",")[0];
		}

		// 还是不能获取到，最后再通过request.getRemoteAddr();获取
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
}
