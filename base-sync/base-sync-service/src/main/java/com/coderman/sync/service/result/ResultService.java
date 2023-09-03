package com.coderman.sync.service.result;

import com.coderman.api.vo.PageVO;
import com.coderman.sync.dto.ResultPageDTO;
import com.coderman.sync.result.ResultModel;
import com.coderman.sync.vo.CompareVO;

import java.io.IOException;
import java.util.List;

public interface ResultService {

    /**
     * 同步记录搜索
     *
     * @return
     */
    com.coderman.api.vo.ResultVO<PageVO<List<ResultModel>>> page(ResultPageDTO resultPageDTO) throws IOException;


    /**
     * 重新同步
     *
     * @param uuid uuid
     * @return
     */
    com.coderman.api.vo.ResultVO<Void> repeatSync(String uuid);


    /**
     * 标记成功
     *
     * @param uuid uuid
     * @param  remark 标记备注
     * @return
     */
    com.coderman.api.vo.ResultVO<Void> signSuccess(String uuid,String remark) throws IOException;


    /**
     * 比对同步结果
     *
     * @param msgContent 消息内容
     * @return
     */
    com.coderman.api.vo.ResultVO<List<CompareVO>> selectTableData(String msgContent, boolean convert) throws Throwable;


    /**
     * 消费成功 && 标记完成 写入redis
     */
    void successMsgSave2Redis(String msgId);

    /**
     *
     * 消费成功 && 标记完成 写入redis
     *
     * @param msgId
     * @param expiredSeconds
     */
    void successMsgSave2Redis(String msgId,Integer expiredSeconds);


    /**
     * 消费成功 && 标记完成是否存在与redis中
     *
     * @param msgId
     * @return
     */
    boolean successMsgExistRedis(String msgId);
}
