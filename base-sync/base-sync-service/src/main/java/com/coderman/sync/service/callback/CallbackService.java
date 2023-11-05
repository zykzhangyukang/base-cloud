package com.coderman.sync.service.callback;

import com.coderman.api.vo.PageVO;
import com.coderman.api.vo.ResultVO;
import com.coderman.sync.callback.CallbackModel;
import com.coderman.sync.dto.CallbackPageDTO;
import com.coderman.sync.dto.CallbackRepeatDTO;

import java.util.List;

public interface CallbackService {

    /**
     * 消息回调列表
     * @param callbackPageDTO 查询参数
     * @return
     */
    ResultVO<PageVO<List<CallbackModel>>> selectCallbackPage(CallbackPageDTO callbackPageDTO);

    /**
     * 重新回调
     *
     * @param callbackRepeatDTO 请求参数
     * @return
     */
    ResultVO<Void> repeatCallback(CallbackRepeatDTO callbackRepeatDTO);
}
