package cn.edu.seu.alumni_server.controller.dto;

import lombok.Data;

import java.util.List;

@Data
public class SearchResultDTO {
    private List<BriefInfo> name;
    private List<BriefInfo> school;
    private List<BriefInfo> college;
    private List<BriefInfo> city;
    private List<BriefInfo> company;
    private List<BriefInfo> position;
    private List<BriefInfo> selfDesc;
}
