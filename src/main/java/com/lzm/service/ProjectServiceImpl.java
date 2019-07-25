package com.lzm.service;

import com.lzm.conversion.DateConveter;
import com.lzm.mapper.KindMapper;
import com.lzm.mapper.ProjectMapper;
import com.lzm.mapper.TypeMapper;
import com.lzm.mapper.UserMapper;
import com.lzm.pojo.Project;
import com.lzm.pojo.ProjectExample;
import com.lzm.pojo.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author lzm
 * @create 2019- 07- 12- 11:39
 * 代码不够简洁
 */
@Service
public class ProjectServiceImpl implements ProjectService {
    @Autowired
    private ProjectMapper projectMapper;
    @Autowired
    private DateConveter dateConveter;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private TypeMapper typeMapper;
    @Autowired
    private KindMapper kindMapper;


    //发起众筹项目
    @Override
    public int addProject(Project project) {
       //预览后是否确认提交 0：未提交 1：提交
        project.setIsable("0");
        //审核   0：未通过   1：通过  2：待确认
        project.setIsforbid("0");
        project.setNumcol(0);
        project.setNumsup(0);
//        1.待众筹 2众筹中 3:完成众筹
        project.setStatue("1");
        Date date = dateConveter.convert(project.getLasttime());
        project.setDeadline(date);
        project.setStartdate(new Date(System.currentTimeMillis()));
        projectMapper.insert(project);
        return 1;
    }

    //流加载中用到，根据页数和一页显示数目排序
    @Override
    public List<Project> listProjectByPageAndLimit(int page, int limit) {
        ProjectExample projectExample=new ProjectExample();
        int start=(page-1)*limit;
       projectExample.setOrderByClause("id limit "+start+","+limit);
       //除开待审核的
       projectExample.or().andIsforbidEqualTo("1");
        List<Project> projects = projectMapper.selectByExample(projectExample);
        for (Project project:projects)
        {
            //获取多张图片路径
            String[] urls = project.getImagelist().split(",");
            project.setImageUrls(urls);
              project.setUser(userMapper.selectByPrimaryKey(project.getUserid()));
               project.setKidename(kindMapper.selectByPrimaryKey(project.getKindid()).getName());
            Type type = typeMapper.selectByPrimaryKey(project.getTypeid());
            if (type!=null)
            {
                project.setTypename(type.getName());
            }

        }
        return projects;
    }

    //后台管理
    @Override
    public List<Project> AdminListProjectByPageAndLimit(int page, int limit) {
        ProjectExample projectExample=new ProjectExample();
        int start=(page-1)*limit;
        projectExample.setOrderByClause("id limit "+start+","+limit);
        List<Project> projects = projectMapper.selectByExample(projectExample);
        if (projects.size()!=0)
        {
            for (Project project:projects)
            {
                String[] urls = project.getImagelist().split(",");
                project.setImageUrls(urls);
                project.setKidename(kindMapper.selectByPrimaryKey(project.getKindid()).getName());
                project.setUser(userMapper.selectByPrimaryKey(project.getUserid()));
                Type type = typeMapper.selectByPrimaryKey(project.getTypeid());
                if (type!=null)
                {
                    project.setTypename(type.getName());
                }

            }
            return projects;
        }else {
            return null;
        }

    }

    @Override
    public int AdminfindProjectPageCount() {
        ProjectExample projectExample=new ProjectExample();
        List<Project> projects = projectMapper.selectByExample(projectExample);
        int size = projects.size();
        return size;
    }

    //查询产品数量
    @Override
    public int findProjectPageCount() {
        ProjectExample projectExample=new ProjectExample();
        projectExample.or().andIsforbidEqualTo("1");
        List<Project> projects = projectMapper.selectByExample(projectExample);
        int size = projects.size();
        return size;
    }


    //根据综合排序查询全部的产品:未完成好
    @Override
    public List<Project> sortProductByAll(int page, int limit) {
        ProjectExample projectExample=new ProjectExample();
        projectExample.or().andIsforbidEqualTo("1");
        int start=(page-1)*limit;
        projectExample.setOrderByClause("numsup desc,numcol desc limit "+start+","+limit);
        List<Project> projects = projectMapper.selectByExample(projectExample);
        return projects;
    }

    @Override
    public List<Project> sortProductingByAll(int page, int limit) {
        ProjectExample projectExample=new ProjectExample();
        projectExample.or().andIsforbidEqualTo("1").andStatueEqualTo("2");
        int start=(page-1)*limit;
        projectExample.setOrderByClause("numsup desc,numcol desc limit "+start+","+limit);
        List<Project> projects = projectMapper.selectByExample(projectExample);
        return projects;
    }

    @Override
    public List<Project> sortProductWaitByAll(int page, int limit) {
        ProjectExample projectExample=new ProjectExample();
        projectExample.or().andIsforbidEqualTo("1").andStatueEqualTo("1");
        int start=(page-1)*limit;
        projectExample.setOrderByClause("numsup desc,numcol desc limit "+start+","+limit);
        List<Project> projects = projectMapper.selectByExample(projectExample);
        return projects;
    }

    @Override
    public List<Project> sortProductENdByAll(int page, int limit) {
        ProjectExample projectExample=new ProjectExample();
        projectExample.or().andIsforbidEqualTo("1").andStatueEqualTo("3");
        int start=(page-1)*limit;
        projectExample.setOrderByClause("numsup desc,numcol desc limit "+start+","+limit);
        List<Project> projects = projectMapper.selectByExample(projectExample);
        return projects;
    }


    @Override
    public List<Project> sortProductByMoney(int page, int limit) {
        ProjectExample projectExample=new ProjectExample();
        projectExample.or().andIsforbidEqualTo("1");
        int start=(page-1)*limit;
        projectExample.setOrderByClause("supmoney desc limit "+start+","+limit);
        List<Project> projects = projectMapper.selectByExample(projectExample);
        return projects;
    }

    @Override
    public List<Project> sortProductingByMoney(int page, int limit) {
        ProjectExample projectExample=new ProjectExample();
        projectExample.or().andIsforbidEqualTo("1").andStatueEqualTo("2");
        int start=(page-1)*limit;
        projectExample.setOrderByClause("supmoney desc limit "+start+","+limit);
        List<Project> projects = projectMapper.selectByExample(projectExample);
        return projects;
    }

    @Override
    public List<Project> sortProductWaitByMoney(int page, int limit) {
        ProjectExample projectExample=new ProjectExample();
        projectExample.or().andIsforbidEqualTo("1").andStatueEqualTo("1");
        int start=(page-1)*limit;
        projectExample.setOrderByClause("supmoney desc limit "+start+","+limit);
        List<Project> projects = projectMapper.selectByExample(projectExample);
        return projects;
    }

    @Override
    public List<Project> sortProductENdByMoney(int page, int limit) {
        ProjectExample projectExample=new ProjectExample();
        projectExample.or().andIsforbidEqualTo("1").andStatueEqualTo("3");
        int start=(page-1)*limit;
        projectExample.setOrderByClause("supmoney desc limit "+start+","+limit);
        List<Project> projects = projectMapper.selectByExample(projectExample);
        return projects;
    }

    @Override
    public List<Project> sortProductBySup(int page, int limit) {
        ProjectExample projectExample=new ProjectExample();
        projectExample.or().andIsforbidEqualTo("1");
        int start=(page-1)*limit;
        projectExample.setOrderByClause("numsup desc limit "+start+","+limit);
        List<Project> projects = projectMapper.selectByExample(projectExample);
        return projects;
    }


    @Override
    public List<Project> sortProductingBySup(int page, int limit) {
        ProjectExample projectExample=new ProjectExample();
        projectExample.or().andIsforbidEqualTo("1").andStatueEqualTo("2");
        int start=(page-1)*limit;
        projectExample.setOrderByClause("numsup desc limit "+start+","+limit);
        List<Project> projects = projectMapper.selectByExample(projectExample);
        return projects;
    }

    @Override
    public List<Project> sortProductWaitBySup(int page, int limit) {
        ProjectExample projectExample=new ProjectExample();
        projectExample.or().andIsforbidEqualTo("1").andStatueEqualTo("1");
        int start=(page-1)*limit;
        projectExample.setOrderByClause("numsup desc limit "+start+","+limit);
        List<Project> projects = projectMapper.selectByExample(projectExample);
        return projects;
    }

    @Override
    public List<Project> sortProductENdBySup(int page, int limit) {
        ProjectExample projectExample=new ProjectExample();
        projectExample.or().andIsforbidEqualTo("1").andStatueEqualTo("3");
        int start=(page-1)*limit;
        projectExample.setOrderByClause("numsup desc limit "+start+","+limit);
        List<Project> projects = projectMapper.selectByExample(projectExample);
        return projects;
    }

    @Override
    public Project selectProductById(int id) {
        Project project = projectMapper.selectByPrimaryKey(id);
        return project;
    }

    //根据产品id删除产品
    @Override
    public int deleteProjectByID(int id) {
        Project project = projectMapper.selectByPrimaryKey(id);
        if (project!=null)
        {
            projectMapper.deleteByPrimaryKey(id);
            return 1;
        }else {
            return 0;
        }
    }

    @Override
    public List<Project> selectProductLikeName(String name, int page, int limit) {
        ProjectExample projectExample=new ProjectExample();
        projectExample.or().andNameLike("%"+name+"%");
        int start=(page-1)*limit;
        projectExample.setOrderByClause("startdate desc limit "+start+","+limit);
        List<Project> projects = projectMapper.selectByExample(projectExample);
        if (projects.size()!=0)
        {
            for (Project project:projects)
            {
                String[] urls = project.getImagelist().split(",");
                project.setKidename(kindMapper.selectByPrimaryKey(project.getKindid()).getName());
                project.setImageUrls(urls);
                project.setUser(userMapper.selectByPrimaryKey(project.getUserid()));
                Type type = typeMapper.selectByPrimaryKey(project.getTypeid());
                if (type!=null)
                {
                    project.setTypename(type.getName());
                }

            }
            return projects;
        }else {
            return null;
        }

    }

    @Override
    public int selectProductLikeNameSize(String name) {
        ProjectExample projectExample=new ProjectExample();
        projectExample.or().andNameLike("%"+name+"%");
        List<Project> projects = projectMapper.selectByExample(projectExample);
        return projects.size();
    }


    @Override
    public List<Project> sortProductByDay(int page, int limit) {
        ProjectExample projectExample=new ProjectExample();
        projectExample.or().andIsforbidEqualTo("1");
        int start=(page-1)*limit;
        projectExample.setOrderByClause("startdate desc limit "+start+","+limit);
        List<Project> projects = projectMapper.selectByExample(projectExample);
        return projects;
    }

    @Override
    public List<Project> sortProductingByDay(int page, int limit) {
        ProjectExample projectExample=new ProjectExample();
        projectExample.or().andIsforbidEqualTo("1").andStatueEqualTo("2");
        int start=(page-1)*limit;
        projectExample.setOrderByClause("startdate desc limit "+start+","+limit);
        List<Project> projects = projectMapper.selectByExample(projectExample);
        return projects;
    }

    @Override
    public List<Project> sortProductWaitByDay(int page, int limit) {
        ProjectExample projectExample=new ProjectExample();
        projectExample.or().andIsforbidEqualTo("1").andStatueEqualTo("1");
        int start=(page-1)*limit;
        projectExample.setOrderByClause("startdate desc limit "+start+","+limit);
        List<Project> projects = projectMapper.selectByExample(projectExample);
        return projects;
    }

    @Override
    public List<Project> sortProductENdByDay(int page, int limit) {
        ProjectExample projectExample=new ProjectExample();
        projectExample.or().andIsforbidEqualTo("1").andStatueEqualTo("3");
        int start=(page-1)*limit;
        projectExample.setOrderByClause("startdate desc limit "+start+","+limit);
        List<Project> projects = projectMapper.selectByExample(projectExample);
        return projects;
    }

    @Override
    public List<Project> sortAllProduct(int page, int limit) {
        ProjectExample projectExample=new ProjectExample();
        projectExample.or().andIsforbidEqualTo("1");
        int start=(page-1)*limit;
        projectExample.setOrderByClause("id desc limit "+start+","+limit);
        List<Project> projects = projectMapper.selectByExample(projectExample);
        return projects;

    }

    //众筹中
    @Override
    public int findProjectingPageCount() {
        ProjectExample projectExample=new ProjectExample();
        projectExample.or().andIsforbidEqualTo("1").andStatueEqualTo("2");
        List<Project> projects = projectMapper.selectByExample(projectExample);
        int size = projects.size();
        return size;
    }


    @Override
    public int findProjectWaitPageCount() {
        ProjectExample projectExample=new ProjectExample();
        projectExample.or().andIsforbidEqualTo("1").andStatueEqualTo("1");
        List<Project> projects = projectMapper.selectByExample(projectExample);
        int size = projects.size();
        return size;
    }

    @Override
    public int findProjectEndPageCount() {
        ProjectExample projectExample=new ProjectExample();
        projectExample.or().andIsforbidEqualTo("1").andStatueEqualTo("3");
        List<Project> projects = projectMapper.selectByExample(projectExample);
        int size = projects.size();
        return size;
    }


}
