package com.coderman.sync.service.callback.impl;

import com.coderman.api.constant.CommonConstant;
import com.coderman.api.util.ResultUtil;
import com.coderman.api.vo.PageVO;
import com.coderman.api.vo.ResultVO;
import com.coderman.service.anntation.LogError;
import com.coderman.service.anntation.LogErrorParam;
import com.coderman.service.util.SpringContextUtil;
import com.coderman.sync.callback.CallbackContext;
import com.coderman.sync.callback.CallbackModel;
import com.coderman.sync.callback.meta.CallbackTask;
import com.coderman.sync.constant.PlanConstant;
import com.coderman.sync.constant.SyncConstant;
import com.coderman.sync.context.SyncContext;
import com.coderman.sync.dto.CallbackPageDTO;
import com.coderman.sync.dto.CallbackRepeatDTO;
import com.coderman.sync.service.callback.CallbackService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
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

        String destProject = callbackPageDTO.getDestProject();
        String dbName = SyncContext.getContext().getDbByProject(destProject);

        if (StringUtils.isBlank(dbName)) {
            return ResultUtil.getWarnPage(CallbackModel.class, "目标系统不能为空，请选择目标系统！");
        }

        String dbType = SyncContext.getContext().getDbType(dbName);
        if (StringUtils.isEmpty(dbType)) {
            return ResultUtil.getWarnPage(CallbackModel.class, "无此系统信息，请重新选择系统！");
        }

        if (SyncConstant.DB_TYPE_MONGO.equals(dbType)) {

            return this.selectCallbackPageByMongo(callbackPageDTO);

        } else {
            return this.selectCallbackPageBySql(callbackPageDTO);
        }
    }

    @Override
    @LogError(value = "重新回调")
    public ResultVO<Void> repeatCallback(@LogErrorParam CallbackRepeatDTO callbackRepeatDTO) {

        String destProject = callbackRepeatDTO.getDestProject();
        List<String> uuidList = callbackRepeatDTO.getUuidList();

        if (StringUtils.isBlank(destProject)) {

            return ResultUtil.getWarn("目标项目不能为空！");
        }

        String dbName = SyncContext.getContext().getDbByProject(destProject);
        String dbType = SyncContext.getContext().getDbType(dbName);

        if (CollectionUtils.isEmpty(uuidList)) {

            return ResultUtil.getWarn("请选择需要回调的数据！");
        }

        if (SyncConstant.DB_TYPE_MONGO.equals(dbType)) {

            MongoTemplate mongoTemplate = SpringContextUtil.getBean(dbName + "_mongoTemplate");
            Query query = new Query();
            List<CallbackModel> resultList = mongoTemplate.find(query, CallbackModel.class);
            for (CallbackModel callbackModel : resultList) {

                CallbackTask callbackTask = new CallbackTask();
                callbackTask.setProject(callbackModel.getDestProject());
                callbackTask.setUuid(callbackModel.getUuid());
                callbackTask.setFirst(false);
                callbackTask.setDb(callbackModel.getDbName());
                callbackTask.setMsg(callbackModel.getMsgContent());

                // 加入回调队列
                CallbackContext.getCallbackContext().addTask(callbackTask);
            }

        } else {

            JdbcTemplate jdbcTemplate = SpringContextUtil.getBean(dbName + "_template");

            // 查询需要回调的记录
            String selectSql = "select uuid, dest_project, msg_content, db_name from pub_callback where status = '" + PlanConstant.CALLBACK_STATUS_FAIL + "' and uuid in ";

            selectSql += " (" + StringUtils.repeat("?", ",", uuidList.size()) + ")";

            if (!SyncConstant.DB_TYPE_ORACLE.equals(dbType)) {
                selectSql += ";";
            }

            List<Map<String, Object>> resultMapList = jdbcTemplate.queryForList(selectSql, uuidList.toArray());

            for (Map<String, Object> resultMap : resultMapList) {

                CallbackTask callbackTask = new CallbackTask();
                callbackTask.setProject(resultMap.get("dest_project").toString());
                callbackTask.setUuid(resultMap.get("uuid").toString());
                callbackTask.setFirst(false);
                callbackTask.setDb(resultMap.get("db_name").toString());
                callbackTask.setMsg(resultMap.get("msg_content").toString());

                // 加入回调队列
                CallbackContext.getCallbackContext().addTask(callbackTask);
            }
        }

        return ResultUtil.getSuccess();
    }

    private ResultVO<PageVO<List<CallbackModel>>> selectCallbackPageBySql(CallbackPageDTO callbackPageDTO) {

        String srcProject = callbackPageDTO.getSrcProject();
        String destProject = callbackPageDTO.getDestProject();
        Date startTime = callbackPageDTO.getStartTime();
        Date endTime = callbackPageDTO.getEndTime();
        String planCode = callbackPageDTO.getPlanCode();
        String status = callbackPageDTO.getStatus();
        String msgContent = callbackPageDTO.getMsgContent();
        Integer repeatCount = callbackPageDTO.getRepeatCount();
        String msgId = callbackPageDTO.getMsgId();
        String dbName = SyncContext.getContext().getDbByProject(destProject);


        Integer currentPage = callbackPageDTO.getCurrentPage();
        Integer pageSize = callbackPageDTO.getPageSize();

        if (currentPage == null) {
            currentPage = 1;
        }

        if (pageSize == null) {
            pageSize = CommonConstant.SYS_PAGE_SIZE;
        }

        String dbType = SyncContext.getContext().getDbType(dbName);

        JdbcTemplate jdbcTemplate;

        try {
            jdbcTemplate = SpringContextUtil.getBean(dbName + "_template");
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

        if (StringUtils.isNotBlank(msgId)) {

            stringBuilder.append(" and msg_id = ? ");
            paramList.add(msgId);
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

            return ResultUtil.getFailPage(CallbackModel.class,  "查询列表错误！" + ExceptionUtils.getRootCauseMessage(e));
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
                model.setMsgId(resultMap.get("msg_id").toString());
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
            builder.append(" as rownumber, uuid,msg_id, src_project,dest_project,msg_content,create_time,send_time,ack_time,repeat_count,status,remark ");
            builder.append(" from pub_callback with(nolock) ");

        } else if (StringUtils.equals(dbType, SyncConstant.DB_TYPE_ORACLE)) {

            builder.append(" a1.* ");
            builder.append(" from (select uuid,msg_id,src_project ,dest_project ,msg_content ,create_time ,send_time ,ack_time, repeat_count, status, remark, rownum as rn from pub_callback");

        } else {

            builder.append(" uuid ,msg_id, src_project, dest_project,msg_content,create_time,send_time,ack_time,repeat_count,status,remark ");
            builder.append(" from pub_callback ");
        }
        return builder.toString();
    }

    private ResultVO<PageVO<List<CallbackModel>>> selectCallbackPageByMongo(CallbackPageDTO callbackPageDTO) {
        return null;
    }
}
