<template>
    <a-table
            size='small'
            ref='table'
            :scroll='{x: width, y:height}'
            v-bind="$attrs"
    >
        <template #[item]="data" v-for="item in Object.keys($slots)" :key="item">
            <slot :name="item" v-bind="data || {}"></slot>
        </template>
    </a-table>
</template>
<script>
    export default {
        name: 'HTable',
        props: {
            page: {
                type: Boolean,
                default: true
            },
            width: {
                type: [String, Number],
                default: 'calc(100%+50px)'
            }
        },
        data () {
            return {
                height: 'auto'
            };
        },
        mounted () {
            this.setHeight();
        },
        methods: {
            setHeight () {
                if (this.$el) {
                    let rect = this.$el.getBoundingClientRect();
                    let bodyRect = document.body.getBoundingClientRect();
                    let maxHeight = bodyRect.height - rect.top - 125;
                    if (this.page) {
                        maxHeight -= 40;
                    }
                    if (maxHeight < 200) {
                        this.height = 200;
                        return void 0;
                    }
                    this.height = maxHeight;
                } else {
                    this.height = 'auto';
                }
            }
        },
    };
</script>