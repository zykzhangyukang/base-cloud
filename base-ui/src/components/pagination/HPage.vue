<template>
    <a-pagination
            :style="{marginTop:'10px',textAlign:'right'}"
            show-size-changer
            :current="current"
            :page-size="pageSize"
            :show-total="total => `共 ${total} 条记录`"
            :total="total"
            @change="pageCurrentChange"
            @showSizeChange="pageSizeChange" />
</template>
<script>
    export default {
        name: 'HPage',
        props: {
            current: [String, Number],
            pageSize: [String, Number],
            total: [String, Number]
        },
        methods: {
            pageCurrentChange (page, pageSize) {
                this.$emit('change', page, pageSize)
            },
            pageSizeChange (current, size) {
                this.$emit('show-size-change', current, size)
            },
        },
    };
</script>