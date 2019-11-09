package cn.edu.seu.alumni_server.service;

import cn.edu.seu.alumni_server.common.exceptions.AlumniCircleServiceException;
import cn.edu.seu.alumni_server.controller.dto.AlumniCircleDTO;
import cn.edu.seu.alumni_server.dao.entity.AlumniCircle;

import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("ALL")
public interface AlumniCircleService {

    /**
     * 根据前端输入的 校友圈 dto 对象来判断生成一个经过检验合法的校友圈对象.
     * @param alumniCircleDTO 前端的输入需要校验.
     * @return 返回一个合法的对象.
     */
    public AlumniCircle createAlumniCircleDAO(AlumniCircleDTO alumniCircleDTO) throws AlumniCircleServiceException;

    /**
     * 判断前端的输入是否最基本的有效.
     * @param alumniCircleDTO 前端的输入.
     * @return 返回判断是否有效.
     */
    public Boolean hasAlumniCircleId(AlumniCircleDTO alumniCircleDTO);

    /**
     * 根据用户的 id 查询其参与的所有群组
     * @param accountId 用户 id
     * @return 返回一个参与群组信息的列表.
     */
    public List<AlumniCircleDTO> queryAlumniCircleById(Long accountId) throws AlumniCircleServiceException;
}
