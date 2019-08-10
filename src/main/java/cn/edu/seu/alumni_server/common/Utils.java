package cn.edu.seu.alumni_server.common;

public class Utils {

    static IdGenerator idGenerator;

    static {
//        idGenerator = new SnowflakeIdGenerator(0, 0);
        idGenerator = new IdGenerator();
    }

    public static long generateId() {
        return idGenerator.nextId();
    }
}
