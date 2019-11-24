package cn.edu.seu.alumni_server.controller;


import cn.edu.seu.alumni_server.common.dto.WebResponse;
import cn.edu.seu.alumni_server.common.exceptions.AlumniCircleServiceException;
import cn.edu.seu.alumni_server.controller.dto.AlumniCircleBasicInfoDTO;
import cn.edu.seu.alumni_server.controller.dto.PageResult;
import cn.edu.seu.alumni_server.service.AlumniCircleService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SuppressWarnings("ALL")
@RestController
//@Acl
public class AlumniCircleController {

	@Autowired
	HttpServletRequest request;

	@Autowired
	AlumniCircleService alumniCircleService;

	@PostMapping("/alumniCircle")
	public WebResponse changeAlumniCircle() {
		return new WebResponse().success();
	}

	@GetMapping("/alumniCircles/enrolledAlumniCircles")
	public WebResponse getEnrolledAlumniCirclesByAccountId(
		@RequestParam(value = "accountId", required = true) Long accountId,
		@RequestParam int pageIndex,
		@RequestParam int pageSize
	) {
		try {
			PageHelper.startPage(pageIndex, pageSize);
			List<AlumniCircleBasicInfoDTO> alumniCircleDTOList =
				this.alumniCircleService.queryEnrolledAlumniCircleByAccountId(accountId);
			return new WebResponse().success(
				new PageResult<>(((Page) alumniCircleDTOList).getTotal(), alumniCircleDTOList)
			);
		} catch (AlumniCircleServiceException | Exception e) {
			return new WebResponse().fail(e.getMessage());
		}
	}

	@GetMapping("/search/alumniCircles")
	public WebResponse searchByActivityName(
		@RequestParam(value = "alumniCircleName", required = true) String alumniCircleName,
		@RequestParam(value = "fuzzy", required = false, defaultValue = "true") Boolean fuzzy,
		@RequestParam int pageIndex,
		@RequestParam int pageSize
	) {
		try {
			PageHelper.startPage(pageIndex, pageSize);
			List<AlumniCircleBasicInfoDTO> ans = (
				fuzzy ?
					this.alumniCircleService
						.queryAlumniCircleInfosFuzzilyByAluCirName(alumniCircleName) :
					this.alumniCircleService
						.queryAlumniCircleInfosByAlumniCircleName(alumniCircleName)
			);
			return new WebResponse().success(
				new PageResult<>(((Page) ans).getTotal(), ans)
			);
		} catch (AlumniCircleServiceException e) {
			return new WebResponse().fail(e.getMessage());
		}
	}
}
