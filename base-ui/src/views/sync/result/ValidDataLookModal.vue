<template>
    <a-modal v-model:visible="visible"
             :title="null"
             :closable="false"
             :footer="null"
             @cancel="handleClose"
    >
        <a-spin :spinning="spinning" size="small" >
        <div class="content-wrap">
            <p v-if="validTables.length !==0">共有<b class="red">{{count}}</b>个字段不一致</p>
            <table cellpadding="0" v-for="(item,index) in validTables" :key="index">
                <thead>
                <tr>
                    <th colspan="2" style="text-align: center">{{item.srcTable +'->' + item.destTable}}</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td style="text-align: center">字段</td>
                    <td style="text-align: center">值</td>
                </tr>
                {{item.srcCloumnList}}
                <tr v-for="(items,indexs) in  item.srcColumnList" :key="indexs">
                    <td colspan="1" align="center">
                        {{items + '->' + item.destColumnList[indexs]}}
                    </td>
                    <td colspan="1" align="center"
                        :class="{'red': item.srcResultList[indexs] !==item.destResultList[indexs]}">
                        {{item.srcResultList[indexs] + ' -> '+item.destResultList[indexs]}}
                    </td>
                </tr>
                </tbody>
            </table>
            <div class="isCenter" v-if="validTables.length === 0">暂无数据</div>
        </div>
        </a-spin>
    </a-modal>
</template>

<script>
    import {syncResultValidData} from "@/api/sync";

    export default {
        name: "ValidDataLookModal.vue",
        components: {},
        data() {
            return {
                spinning: false,
                visible: false,
                count: 0,
                validTables: [],
            }
        },
        methods: {
            handleClose() {
                this.visible = false
            },
            open(p) {
                this.visible = true;
                this.spinning = true;
                let param = {msgContent: p.msgContent}
                syncResultValidData(param).then(res => {
                    this.validTables = res.result;
                    let c = 0;
                    this.validTables.forEach((item, index) => {
                        item.srcResultList.forEach((item, ind) => {
                            if (item !== this.validTables[index].destResultList[ind]) {
                                c += 1;
                            }
                        })
                    })
                    this.count = c;
                }).finally(()=>{
                    this.spinning = false;
                })
            }
        },
    }
</script>

<style scoped>
    .red {
        color: #ff4400;
    }
    .content-wrap {
        max-height: 400px;
        overflow: auto;
        font-size: 12px;
    }

    .content-wrap > p {
        margin-top: 0;
    }

    .content-wrap table {
        width: 100%;
        border-left: 1px solid #EBEEF5;
    }

    .content-wrap th {
        background-color: #EBEEF5;
        border-right: 1px solid #EBEEF5;
        border-bottom: 1px solid #EBEEF5;
        height: 40px;
    }

    .content-wrap td {
        border-right: 1px solid #EBEEF5;
        border-bottom: 1px solid #EBEEF5;
        height: 40px;
    }

    .isCenter {
        text-align: center;
    }
</style>