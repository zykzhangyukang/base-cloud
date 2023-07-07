package com.coderman.auth.utils;

import com.coderman.auth.vo.func.FuncTreeVO;
import org.apache.commons.collections4.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

public class TreeUtils {


    /**
     * 获取深层级的功能id (叶子节点)
     *
     * @param list
     * @param funcTreeVO
     * @return
     */
    public static void getDeepFuncIdList(List<Integer> list, FuncTreeVO funcTreeVO) {

        if (Objects.nonNull(funcTreeVO)) {
            if (CollectionUtils.isEmpty(funcTreeVO.getChildren())) {
                list.add(funcTreeVO.getFuncId());
            } else {

                for (FuncTreeVO child : funcTreeVO.getChildren()) {
                    getDeepFuncIdList(list, child);
                }
            }
        }
    }

    /**
     * 获取树节点以及所有子节点的功能id
     *
     * @param list
     * @param funcTreeVO
     */
    public static void getAllFuncIdList(List<Integer> list, FuncTreeVO funcTreeVO){

        if(Objects.isNull(funcTreeVO) || Objects.isNull(funcTreeVO.getFuncId())){
            return;
        }
        list.add(funcTreeVO.getFuncId());

        if(CollectionUtils.isNotEmpty(funcTreeVO.getChildren())){

            for (FuncTreeVO child : funcTreeVO.getChildren()) {
                getAllFuncIdList(list,child);
            }
        }
    }


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
                List<FuncTreeVO> childrenList = Optional.ofNullable(parentFuncTreeNode.getChildren()).orElse(new ArrayList<>());
                childrenList.add(funcTreeVO);
                childrenList.sort(Comparator.comparingInt(FuncTreeVO::getFuncSort));
                parentFuncTreeNode.setChildren(childrenList);
            } else {

                rootFuncTreeNode.add(funcTreeVO);
            }
        }

        rootFuncTreeNode.sort(Comparator.comparingInt(FuncTreeVO::getFuncSort));
        return rootFuncTreeNode;
    }
}
