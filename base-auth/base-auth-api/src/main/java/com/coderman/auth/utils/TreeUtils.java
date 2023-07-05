package com.coderman.auth.utils;

import com.coderman.auth.vo.func.FuncTreeVO;
import org.apache.commons.collections4.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

public class TreeUtils {

    /**
     * 构建功能树
     *
     * @param funcTreeVoList 功能树
     * @return
     */
    public static List<FuncTreeVO> buildFuncTreeByList(List<FuncTreeVO> funcTreeVoList) {

        List<FuncTreeVO> rootFuncTreeNode = new ArrayList<>();

        if (CollectionUtils.isEmpty(funcTreeVoList)) {
            return Collections.emptyList();
        }

        Map<Integer, FuncTreeVO> funcTreeVoMap = funcTreeVoList.stream().collect(Collectors.toMap(FuncTreeVO::getFuncId, e -> e, (k1, k2) -> k2));

        for (FuncTreeVO funcTreeVO : funcTreeVoList) {

            Integer parentId = funcTreeVO.getParentId();

            if (funcTreeVoMap.containsKey(parentId)) {

                FuncTreeVO parentFuncTreeNode = funcTreeVoMap.get(parentId);
                List<FuncTreeVO> childrenList = Optional.ofNullable(parentFuncTreeNode.getChildrenList()).orElse(new ArrayList<>());
                childrenList.add(funcTreeVO);
                childrenList.sort(Comparator.comparingInt(FuncTreeVO::getFuncSort));
                parentFuncTreeNode.setChildrenList(childrenList);
            } else {

                rootFuncTreeNode.add(funcTreeVO);
            }
        }

        rootFuncTreeNode.sort(Comparator.comparingInt(FuncTreeVO::getFuncSort));
        return rootFuncTreeNode;
    }
}
