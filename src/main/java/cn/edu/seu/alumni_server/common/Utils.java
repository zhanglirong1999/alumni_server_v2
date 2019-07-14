package cn.edu.seu.alumni_server.common;

public class Utils {

    static SnowflakeIdGenerator idGenerator;

    static {
        idGenerator = new SnowflakeIdGenerator(0, 0);
    }

    public static long getNextId() {
        return idGenerator.nextId();
    }
}
