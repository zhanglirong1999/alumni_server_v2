package cn.edu.seu.alumni_server.service.impl;

import cn.edu.seu.alumni_server.common.exceptions.AlumniCircleServiceException;
import cn.edu.seu.alumni_server.controller.dto.AlumniCircleBasicInfoDTO;
import cn.edu.seu.alumni_server.controller.dto.AlumniCircleDTO;
import cn.edu.seu.alumni_server.dao.entity.AlumniCircle;
import cn.edu.seu.alumni_server.dao.mapper.AlumniCircleMapper;
import cn.edu.seu.alumni_server.service.AlumniCircleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@SuppressWarnings("ALL")
public class AlumniCircleServiceImpl implements AlumniCircleService {

    @Autowired
    private AlumniCircleMapper alumniCircleMapper;

    @Override
    public AlumniCircle createAlumniCircleDAO(
        AlumniCircleDTO alumniCircleDTO
    ) throws AlumniCircleServiceException {
        if (!this.hasAlumniCircleId(alumniCircleDTO))
            throw new AlumniCircleServiceException("The alumni circle id is none or empty.");
        return alumniCircleDTO.toAlumniCircle();
    }

    @Override
    public Boolean hasAlumniCircleId(AlumniCircleDTO alumniCircleDTO) {
        return (
            alumniCircleDTO.getAlumniCircleId() != null &&
            !alumniCircleDTO.getAlumniCircleId().equals("")
        );
    }

    @Override
    public List<AlumniCircleBasicInfoDTO> queryEnrolledAlumniCircleByAccountId(
        Long accountId
    ) throws AlumniCircleServiceException {
        if (accountId == null || accountId.equals(""))
            throw new AlumniCircleServiceException("The user id is none or empty.");
        List<AlumniCircleBasicInfoDTO> ans =
            this.alumniCircleMapper.queryEnrolledAlumniCircleInfosByAccountId(accountId);
        return ans;
    }

    @Override
    public List<AlumniCircleBasicInfoDTO> queryAlumniCircleInfosByAlumniCircleName(String aluCirName) throws AlumniCircleServiceException {
        if (aluCirName == null || aluCirName.compareTo("") == 0 || aluCirName.equals(""))
            throw new AlumniCircleServiceException("The query word is none or empty.");
        return this.alumniCircleMapper.queryAlumniCircleInfosByAluCirName(aluCirName);
    }

    @Override
    public List<AlumniCircleBasicInfoDTO> queryAlumniCircleInfosFuzzilyByAluCirName(String aluCirName) throws AlumniCircleServiceException {
        if (aluCirName == null || aluCirName.compareTo("") == 0 || aluCirName.equals(""))
            throw new AlumniCircleServiceException("The query word is none or empty.");
        return this.alumniCircleMapper.queryAlumniCircleInfosFuzzilyByAluCirName(aluCirName);
    }
}
