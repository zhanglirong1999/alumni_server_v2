package cn.edu.seu.alumni_server.analysis;

import cn.edu.seu.alumni_server.dao.mapper.EducationMapper;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import lombok.Data;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class XMLParser {

	@Autowired
	EducationMapper educationMapper;

	public void changeForOneCollege(EducationFilter filter) {
		Set<String> oldNames = new HashSet<>();
		for (String fromName : filter.getFromNames()) {
			oldNames.addAll(educationMapper.getCollegeNamesByKeyWord("东南大学", fromName));
		}
		Set<String> notNames = new HashSet<>(filter.getNotNames());
		Set<String> shouldBeRemoved = new HashSet<>();
		for (String oldName : oldNames) {
			for (String notName : notNames) {
				if (oldName.contains(notName)) {
					shouldBeRemoved.add(oldName);
				}
			}
		}
		oldNames.removeAll(shouldBeRemoved);
		List<String> shouldBeReplaced = new LinkedList<>(oldNames);
		System.out.println("开始" + filter.toString());
		System.out.println(shouldBeReplaced);
		if (!shouldBeReplaced.isEmpty())
			educationMapper.updateCollegeNames(filter.getToName(), shouldBeReplaced);
		System.out.println("结束" + filter.toString());
		System.out.println();
	}


	public void runForCopyEducation() throws DocumentException {
		SAXReader saxReader = new SAXReader();
		Document document = saxReader.read(
			Objects.requireNonNull(
				this.getClass().getClassLoader().getResource("config/college-filter.xml"))
		);
		// 提取信息
		List<Node> nodes = document.selectNodes("//college");
		for (Node n : nodes) {
			EducationFilter educationFilter = new EducationFilter(n, document);
			this.changeForOneCollege(educationFilter);
		}
		// 直接修改 Education 表
	}
}

@Data
class EducationFilter {

	private String toName;
	private List<String> fromNames;
	private List<String> notNames;

	public EducationFilter(Node node, Document document) {
		String xpath = node.getUniquePath();
		// 获取到真实的学院名
		String toXPath = String.format(
			"%s/toName/text()", xpath
		);
		Node toName = document.selectSingleNode(toXPath);
		this.toName = toName.getStringValue();
		// 获取到所有关键字
		this.fromNames = new LinkedList<>();
		String fromXPath = String.format(
			"%s//fromName/text()", xpath
		);
		List<Node> fromNames = document.selectNodes(fromXPath);
		for (Node n : fromNames) {
			this.fromNames.add(n.getStringValue());
		}
		// 获取到所有的排除性关键字
		this.notNames = new LinkedList<>();
		String notXPath = String.format(
			"%s//notName/text()", xpath
		);
		List<Node> notNodes = document.selectNodes(notXPath);
		for (Node n : notNodes) {
			this.notNames.add(n.getStringValue());
		}
	}
}
