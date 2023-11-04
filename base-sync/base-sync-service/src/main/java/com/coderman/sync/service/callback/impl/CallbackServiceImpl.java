package com.coderman.sync.service.callback.impl;

import com.coderman.api.constant.CommonConstant;
import com.coderman.api.util.ResultUtil;
import com.coderman.api.vo.PageVO;
import com.coderman.api.vo.ResultVO;
import com.coderman.service.anntation.LogError;
import com.coderman.service.anntation.LogErrorParam;
import com.coderman.service.util.SpringContextUtil;
import com.coderman.sync.callback.CallbackModel;
import com.coderman.sync.constant.SyncConstant;
import com.coderman.sync.context.SyncContext;
import com.coderman.sync.dto.CallbackPageDTO;
import com.coderman.sync.service.callback.CallbackService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class CallbackServiceImpl implements CallbackService {


    @Override
    @LogError(value = "消息回调列表")
    public ResultVO<PageVO<List<CallbackModel>>> selectCallbackPage(@LogErrorParam CallbackPageDTO callbackPageDTO) {

        Integer currentPage = callbackPageDTO.getCurrentPage();
        Integer pageSize = callbackPageDTO.getPageSize();
        String srcProject = callbackPageDTO.getSrcProject();
        String destProject = callbackPageDTO.getDestProject();
        Date startTime = callbackPageDTO.getStartTime();
        Date endTime = callbackPageDTO.getEndTime();
        String planCode = callbackPageDTO.getPlanCode();
        String status = callbackPageDTO.getStatus();
        String msgContent = callbackPageDTO.getMsgContent();
        Integer repeatCount = callbackPageDTO.getRepeatCount();

        if (currentPage == null) {
            currentPage = 1;
        }

        if (pageSize == null) {
            pageSize = CommonConstant.SYS_PAGE_SIZE;
        }

        String dbname = SyncContext.getContext().getDbByProject(destProject);

        if (StringUtils.isBlank(dbname)) {
            return ResultUtil.getWarnPage(CallbackModel.class, "无此系统信息，请重新选择系统！");
        }

        String dbType = SyncContext.getContext().getDbType(dbname);

        if (StringUtils.isEmpty(dbType)) {
            return ResultUtil.getWarnPage(CallbackModel.class, "无此系统信息，请重新选择系统！");
        }

        if (SyncConstant.DB_TYPE_MONGO.equals(dbType)) {

            return this.selectCallbackPageByMongo(srcProject, destProject, status, repeatCount, startTime, endTime, planCode, msgContent, currentPage, pageSize, dbname, dbType);

        } else {
            return this.selectCallbackPageBySql(srcProject, destProject, status, repeatCount, startTime, endTime, planCode, msgContent, currentPage, pageSize, dbname, dbType);
        }
    }

    private ResultVO<PageVO<List<CallbackModel>>> selectCallbackPageBySql(String srcProject, String destProject
            , String status, Integer repeatCount, Date startTime, Date endTime
            , String planCode, String msgContent, Integer currentPage, Integer pageSize, String dbname, String dbType) {

        JdbcTemplate jdbcTemplate;

        try {
            jdbcTemplate = SpringContextUtil.getBean(dbname + "_template");
        } catch (NoSuchBeanDefinitionException e) {
            return ResultUtil.getWarnPage(CallbackModel.class, "无此系统信息，请重新选择系统！");
        }

        StringBuilder stringBuilder = new StringBuilder();
        List<Object> paramList = new ArrayList<>();

        stringBuilder.append(" where dest_project = ? ");
        paramList.add(destProject);

        if (StringUtils.isNotBlank(srcProject)) {

            stringBuilder.append(" and src_project = ? ");
            paramList.add(srcProject);
        }

        if (StringUtils.isNotBlank(planCode)) {

            stringBuilder.append(" and msg_content like ? ");
            String likeKey = "%" + planCode + "%";
            paramList.add(likeKey);
        }

        if (StringUtils.isNotBlank(msgContent)) {

            stringBuilder.append(" and msg_content like ? ");
            String likeKey = "%" + msgContent + "%";
            paramList.add(likeKey);
        }

        if (StringUtils.isNotBlank(status)) {

            stringBuilder.append(" and status = ? ");
            paramList.add(status);
        }


        if (null != repeatCount) {

            if (repeatCount == 10000) {

                stringBuilder.append(" and repeat_count = ? ");
                paramList.add(0);
            } else {
                stringBuilder.append(" and repeat_count >= ?  ");
                paramList.add(repeatCount);
            }
        }

        if (null != startTime) {

            if (StringUtils.equals(dbType, SyncConstant.DB_TYPE_ORACLE)) {
                stringBuilder.append(" and create_time >=  to_day(?,'yyyy-mm-dd hh24:mi:ss')  ");
            } else {
                stringBuilder.append(" and create_time >= ? ");
            }
            paramList.add(startTime);
        }

        if (null != endTime) {

            if (StringUtils.equals(dbType, SyncConstant.DB_TYPE_ORACLE)) {
                stringBuilder.append(" and create_time <  to_day(?,'yyyy-mm-dd hh24:mi:ss')  ");
            } else {
                stringBuilder.append(" and create_time < ? ");
            }
            paramList.add(endTime);
        }

        String sql = "select count(0) from pub_callback " + stringBuilder.toString();
        if (StringUtils.equals(SyncConstant.DB_TYPE_MSSQL, dbType)) {

            sql = "select count(0) from pub_callback with(nolock) " + stringBuilder.toString();
        }

        long count;
        try {
            count = jdbcTemplate.queryForObject(sql, paramList.toArray(), Integer.class);
        } catch (Exception e) {
            return ResultUtil.getWarnPage(CallbackModel.class, "无此系统信息，请重新选择系统！");
        }

        List<CallbackModel> list = new ArrayList<>();
        PageVO<List<CallbackModel>> pageVO = new PageVO<>();

        pageVO.setTotalRow(count);
        pageVO.setCurrPage(currentPage);
        pageVO.setPageRow(pageSize);
        pageVO.setDataList(list);

        if (count > 0) {

            sql = this.buildMsgSelectSql(pageSize, dbType, pageSize * currentPage) + stringBuilder.toString();

            if (StringUtils.equals(SyncConstant.DB_TYPE_MSSQL, dbType)) {

                sql += " ) A where rownumber > ? ";
                paramList.add((currentPage - 1) * pageSize);

            } else if (StringUtils.equals(SyncConstant.DB_TYPE_ORACLE, dbType)) {

                sql += " order by callback_id desc ) a1 where a1.rn between ? and ? ";
                // oracle
                paramList.add((currentPage - 1) * pageSize);
                paramList.add(pageSize);
            } else {
                sql += " order by callback_id desc limit ?, ? ";
                paramList.add((currentPage - 1) * pageSize);
                paramList.add(pageSize);
            }

            List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sql, paramList.toArray());
            for (Map<String, Object> resultMap : resultList) {

                CallbackModel model = new CallbackModel();
                model.setUuid(resultMap.get("uuid").toString());
                model.setMsgContent(resultMap.get("msg_content").toString());
                model.setSrcProject(resultMap.get("src_project").toString());
                model.setDestProject(resultMap.get("dest_project").toString());
                model.setSendTime(resultMap.get("send_time") == null ? null : this.formatTime(resultMap.get("send_time").toString()));
                model.setCreateTime(resultMap.get("create_time") == null ? null : this.formatTime(resultMap.get("create_time").toString()));
                model.setAckTime(resultMap.get("ack_time") == null ? null : this.formatTime(resultMap.get("ack_time").toString()));
                model.setStatus(resultMap.get("status").toString());
                model.setRemark(resultMap.get("remark").toString());
                model.setRepeatCount(Integer.parseInt(resultMap.get("repeat_count").toString()));

                list.add(model);
            }
        }

        pageVO.setDataList(list);
        return ResultUtil.getSuccessPage(CallbackModel.class, pageVO);
    }

    private Date formatTime(String date) {
        if (date == null) {
            return null;
        }
        try {
            return DateUtils.parseDate(date, "yyyy-MM-dd HH:mm:ss.SSS");
        } catch (ParseException e) {
            return null;
        }
    }

    private String buildMsgSelectSql(Integer count, String dbType, int i) {

        StringBuilder builder = new StringBuilder();
        builder.append("select");
        if (StringUtils.equals(dbType, SyncConstant.DB_TYPE_MSSQL)) {

            builder.append(" top ").append(count).append(" = ");
            builder.append(" from ( select row_number() over(order by callback_id desc)");
            builder.append(" as rownumber, uuid, src_project,dest_project,msg_content,create_time,send_time,ack_time,repeat_count,status,remark ");
            builder.append(" from pub_callback with(nolock) ");

        } else if (StringUtils.equals(dbType, SyncConstant.DB_TYPE_ORACLE)) {

            builder.append(" a1.* ");
            builder.append(" from (select uuid,src_project ,dest_project ,msg_content ,create_time ,send_time ,ack_time, repeat_count, status, remark, rownum as rn from pub_callback");

        } else {

            builder.append(" uuid , src_project, dest_project,msg_content,create_time,send_time,ack_time,repeat_count,status,remark ");
            builder.append(" from pub_callback ");
        }
        return builder.toString();
    }

    private ResultVO<PageVO<List<CallbackModel>>> selectCallbackPageByMongo(String srcProject, String destProject, String status, Integer repeatCount, Date startTime, Date endTime, String planCode, String msgId, Integer currentPage, Integer pageSize, String s, String dbname) {
        return null;
    }
}
