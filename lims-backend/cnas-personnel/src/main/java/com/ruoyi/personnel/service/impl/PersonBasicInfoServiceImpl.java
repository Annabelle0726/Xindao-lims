package com.ruoyi.personnel.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.ruoyi.common.core.domain.entity.DepartmentDto;
import com.ruoyi.common.core.domain.entity.DepartmentLims;
import com.ruoyi.common.core.domain.entity.User;
import com.ruoyi.common.utils.QueryWrappers;
import com.ruoyi.framework.exception.ErrorException;
import com.ruoyi.inspect.util.HackLoopTableRenderPolicy;
import com.ruoyi.personnel.dto.PersonBasicInfoDto;
import com.ruoyi.personnel.dto.PersonBasicInfoWorkDto;
import com.ruoyi.personnel.dto.UserPageDto;
import com.ruoyi.personnel.mapper.AnnexMapper;
import com.ruoyi.personnel.mapper.PersonBasicInfoFileMapper;
import com.ruoyi.personnel.mapper.PersonBasicInfoMapper;
import com.ruoyi.personnel.mapper.PersonBasicInfoWorkMapper;
import com.ruoyi.personnel.pojo.Annex;
import com.ruoyi.personnel.pojo.PersonBasicInfo;
import com.ruoyi.personnel.pojo.PersonBasicInfoFile;
import com.ruoyi.personnel.pojo.PersonBasicInfoWork;
import com.ruoyi.personnel.service.PersonBasicInfoService;
import com.ruoyi.system.mapper.DepartmentLimsMapper;
import com.ruoyi.system.mapper.UserMapper;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STMerge;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-08-30 09:19:57
 */

@Service
@Transactional(rollbackFor = Exception.class)
public class PersonBasicInfoServiceImpl extends ServiceImpl<PersonBasicInfoMapper, PersonBasicInfo> implements PersonBasicInfoService {

    @Autowired
    private DepartmentLimsMapper departmentMapper;

    @Value("${file.path}")
    private String imgUrl;
    @Value("${wordUrl}")
    private String wordUrl;
    @Resource
    private UserMapper userMapper;
    @Resource
    private AnnexMapper annexMapper;
    @Resource
    private PersonBasicInfoFileMapper personBasicInfoFileMapper;
    @Resource
    private PersonBasicInfoWorkMapper personBasicInfoWorkMapper;

    @Override
    public List<DepartmentDto> selectCNSAPersonTree() {
        List<DepartmentDto> departments = departmentMapper.selectDepartment();
        List<DepartmentDto> limsUser = baseMapper.selectLimsUser();
        departments.addAll(limsUser);
        //获取父节点
        return departments.stream().filter(m -> m.getFatherId() == null).peek(
                (m) -> m.setChildren(getChildren(m, departments))
        ).collect(Collectors.toList());
    }

    @Override
    public Map<String,Object> getCNASPersonnelInfo(Integer userId) {
        Map<String, Object> map = new HashMap<>();
        Map<Integer,List<DepartmentLims>>  childrenMap = new HashMap<>();
        List<DepartmentLims> deptS = departmentMapper.selectList(null);
        for (DepartmentLims dept : deptS) {
            if(!Objects.isNull(dept.getFatherId())) {
                if(!childrenMap.containsKey(dept.getFatherId())) {
                        childrenMap.put(dept.getFatherId(),new ArrayList<>());
                }
                childrenMap.get(dept.getFatherId()).add(dept);
            }
        }
        // 父节点
        List<DepartmentLims> deptF = new ArrayList<>();
        for (DepartmentLims dept : deptS) {
            if(Objects.isNull(dept.getFatherId())) {
                deptF.add(buildTree(dept,childrenMap));
            }
        }
        map.put("department",deptF);
        map.put("PersonBasicInfoDto",baseMapper.getCNASPersonnelInfo(userId));
        map.put("annexList",annexMapper.selectList(new LambdaQueryWrapper<Annex>().eq(Annex::getUserId,userId)));
        return map;
    }

    private DepartmentLims buildTree(DepartmentLims departmentLims, Map<Integer,List<DepartmentLims>> childrenMap) {
        if(childrenMap.containsKey(departmentLims.getId())) {
            departmentLims.setChildren(childrenMap.get(departmentLims.getId()));
            for (DepartmentLims departmentLims1 : departmentLims.getChildren()) {
                buildTree(departmentLims1,childrenMap);
            }
        }
        return departmentLims;
    }

    @Override
    public void saveCNASPersonnelInfo(PersonBasicInfoDto personBasicInfoDto) {
        User user = new User();
        user.setId(personBasicInfoDto.getUserId());
        user.setAccount(personBasicInfoDto.getAccount());
        user.setName(personBasicInfoDto.getName());
        user.setNameEn(personBasicInfoDto.getNameEn());
        user.setAge(personBasicInfoDto.getAge());
        user.setPhone(personBasicInfoDto.getPhone());
        user.setEmail(personBasicInfoDto.getEmail());
        user.setSignatureUrl(personBasicInfoDto.getSignatureUrl());
        user.setPictureUrl(personBasicInfoDto.getPictureUrl());
        user.setDepartLimsId(personBasicInfoDto.getDepartLimsId());
        userMapper.updateById(user);
        PersonBasicInfo personBasicInfo = JSONObject.parseObject(JSON.toJSONString(personBasicInfoDto), PersonBasicInfo.class);
        PersonBasicInfo one = baseMapper.selectOne(new LambdaQueryWrapper<PersonBasicInfo>()
                .eq(PersonBasicInfo::getUserId, personBasicInfoDto.getUserId()));
        if(Objects.isNull(one)) {
            baseMapper.insert(personBasicInfo);
        }else {
            baseMapper.updateById(personBasicInfo);
        }
    }

    @Override
    public IPage<Map<String, Object>> basicInformationOfPersonnelSelectPage(Page page, String name, Integer departmentId) {
        return baseMapper.selectPersonBasecInfoAndUser(page, name, departmentId);
    }

    @Override
    public void exportPersonBasicInfo(UserPageDto userPageDto, HttpServletResponse response) throws Exception {
        ArrayList<PersonBasicInfoDto> data = new ArrayList<>();
        List<User> list = userMapper.selectList(null);
        for (User user : list) {
            PersonBasicInfoDto personBasicInfoDto = new PersonBasicInfoDto();
            PersonBasicInfo personBasicInfo = baseMapper.selectOne(Wrappers.<PersonBasicInfo>lambdaQuery().eq(PersonBasicInfo::getUserId, user.getId()));
            if (ObjectUtils.isNotEmpty(personBasicInfo)) {
                BeanUtils.copyProperties(personBasicInfo, personBasicInfoDto);
            }
            personBasicInfoDto.setName(user.getName());
            personBasicInfoDto.setAccount(user.getAccount());
            personBasicInfoDto.setPhone(ObjectUtils.isNotEmpty(user.getPhone()) ? user.getPhone() : " ");
            data.add(personBasicInfoDto);
        }
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("UTF-8");
        String fileName = URLEncoder.encode("人员基本信息列表导出", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        try {
            // 新建ExcelWriter
            ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()).build();
            WriteSheet mainSheet = EasyExcel.writerSheet(0, "人员基本信息导出").head(PersonBasicInfoDto.class).build();
            excelWriter.write(data, mainSheet);
            // 关闭流
            excelWriter.finish();
        } catch (IOException e) {
            throw new RuntimeException("导出失败");
        }
    }

    @Override
    public String exportPersonBasicInfoById(Integer id, HttpServletResponse response) {
        Map<String, Object> userMap = baseMapper.selectexportPersonBasic(id);
        User user = userMapper.selectById(id);
        PersonBasicInfo personBasicInfo = baseMapper.selectOne(Wrappers.<PersonBasicInfo>lambdaQuery().eq(PersonBasicInfo::getUserId, user.getId()));
        if (ObjectUtils.isEmpty(personBasicInfo)){
            throw new ErrorException("该用户的基本信息没有录入,暂无法导出");
        }
        // 证件
        List<Annex> annexList = annexMapper.selectList(Wrappers.<Annex>lambdaQuery()
                .eq(Annex::getUserId, id));
        // 工作经历
        List<PersonBasicInfoWork> personBasicInfoWorks = personBasicInfoWorkMapper.selectList(Wrappers.<PersonBasicInfoWork>lambdaQuery()
                .eq(PersonBasicInfoWork::getUserId, id));

        List<PersonBasicInfoWorkDto> workList = personBasicInfoWorks.stream().map(basicInfoWork -> {
            PersonBasicInfoWorkDto personBasicInfoWorkDto = new PersonBasicInfoWorkDto();
            personBasicInfoWorkDto.setWorkExperience(basicInfoWork.getWorkExperience());
            personBasicInfoWorkDto.setFill("主要工作经历\nMain work experience∑1");
            return personBasicInfoWorkDto;
        }).collect(Collectors.toList());

        // 检查列表长度并填充空对象
        while (annexList.size() < 10) {
            annexList.add(new Annex());
        }

        // 检查列表长度并填充空对象
        while (workList.size() < 4) {
            workList.add(new PersonBasicInfoWorkDto());
        }

        // 获取路径
        InputStream inputStream = this.getClass().getResourceAsStream("/static/person-basic-info.docx");
        Configure configure = Configure.builder()
                .bind("annexList", new HackLoopTableRenderPolicy())
                .bind("workList", new HackLoopTableRenderPolicy())
                .build();
        XWPFTemplate template = XWPFTemplate.compile(inputStream, configure).render(
                new HashMap<String, Object>() {{
                    put("user", userMap);
                    put("annexList", annexList);
                    put("workList", workList);
                }});

        try {
            response.setContentType("application/msword");
            String fileName = URLEncoder.encode(
                    userMap.get("name") + "人员档案", "UTF-8");
            response.setHeader("Content-disposition",
                    "attachment;filename=" + fileName + ".docx");
            OutputStream os = response.getOutputStream();
            template.write(os);
            os.flush();
            os.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("导出失败");
        }

        return null;
    }

    /**
     * 人员培训基本信息附件新增
     * @param userId
     * @param file
     * @return
     */
    @Override
    public boolean uploadBasicInfoFile(Integer userId, MultipartFile file) {
        if (userId == null) {
            throw new ErrorException("缺少人员id");
        }

        String urlString;
        String pathName;
        String path;
        String filename = file.getOriginalFilename();
        String contentType = file.getContentType();
        PersonBasicInfoFile personBasicInfoFile = new PersonBasicInfoFile();
        personBasicInfoFile.setUserId(userId);
        personBasicInfoFile.setFileName(filename);
        if (contentType != null && contentType.startsWith("image/")) {
            // 是图片
            path = imgUrl;
            personBasicInfoFile.setType(1);
        } else {
            // 是文件
            path = wordUrl;
            personBasicInfoFile.setType(2);
        }
        try {
            File realpath = new File(path);
            if (!realpath.exists()) {
                realpath.mkdirs();
            }
            pathName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmss")) + "_" + file.getOriginalFilename();
            urlString = realpath + "/" + pathName;
            file.transferTo(new File(urlString));
            personBasicInfoFile.setFileUrl(pathName);
            personBasicInfoFileMapper.insert(personBasicInfoFile);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("附件上传错误");
            return false;
        }
    }

    /**
     * 新增组织树
     * @param department
     * @return
     */
    @Override
    public int addDepartment(DepartmentLims department) {
        departmentMapper.insert(department);
        return department.getId();
    }

    /**
     * 删除组织树
     * @param id
     * @return
     */
    @Override
    public boolean delDepartment(Integer id) {
        //判断是否有子类,直到没有为止
        List<DepartmentLims> department = getDepartment(id);
        List<Integer> ids = department.stream().map(DepartmentLims::getId).collect(Collectors.toList());
        departmentMapper.delete(Wrappers.<DepartmentLims>lambdaQuery()
                .in(DepartmentLims::getId, ids));
        return true;
    }

    //判断是否有子类,直到没有为止
    public List<DepartmentLims> getDepartment(Integer id) {
        List<DepartmentLims> list = new ArrayList<>();
        DepartmentLims depart = departmentMapper.selectById(id);
        list.add(depart);
        List<DepartmentLims> departments = departmentMapper.selectList(Wrappers.<DepartmentLims>lambdaQuery().eq(DepartmentLims::getFatherId, id));
        if (ObjectUtils.isNotEmpty(departments)) {
            list.addAll(departments);
            for (DepartmentLims department : departments) {
                list.addAll(getDepartment(department.getId()));
            }
        }
        return list;
    }

    /**
     * 递归查询子节点
     * @param root  根节点
     * @param all   所有节点
     * @return 根节点信息
     */
    private List<DepartmentDto> getChildren(DepartmentDto root, List<DepartmentDto> all) {
        if (ObjectUtils.isNotEmpty(root.getId())) {
            return all.stream().filter(m -> Objects.equals(m.getFatherId(), root.getId())).peek(
                    (m) -> m.setChildren(getChildren(m, all))
            ).collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    // 水平合并单元格
    private static void mergeCellsHorizontally(XWPFTable table, int row, int fromCol, int toCol) {
        for (int i = fromCol; i <= toCol; i++) {
            if (i == fromCol) {
                table.getRow(row).getCell(i).getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.RESTART);
            } else {
                table.getRow(row).getCell(i).getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);
            }
        }
    }

    // 垂直合并单元格
    private static void mergeCellsVertically(XWPFTable table, int col, int fromRow, int toRow) {
        for (int i = fromRow; i <= toRow; i++) {
            if (i == fromRow) {
                table.getRow(i).getCell(col).getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.RESTART);
            } else {
                table.getRow(i).getCell(col).getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.CONTINUE);
            }
        }
    }
}
