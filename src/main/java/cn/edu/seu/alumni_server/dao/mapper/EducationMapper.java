package cn.edu.seu.alumni_server.dao.mapper;

import cn.edu.seu.alumni_server.dao.entity.Education;
import java.util.List;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface EducationMapper extends Mapper<Education> {

	// 选择所有的学校名称
	public List<String> getSchoolNames();

	// 按照关键词查询东南大学的学院的填写
	public List<String> getCollegeNamesByKeyWord(String school, String keyWord);

	// 替换学院名称
	public void updateCollegeNames(String rs, List<String> cs);

}