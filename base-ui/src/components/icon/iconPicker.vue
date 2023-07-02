<template>
    <a-modal :footer="null" v-model:visible="visible" title="图标选择器">
        <a-radio-group v-model:value="value1" button-style="solid">
            <a-radio-button :value="1">线框风格</a-radio-button>
            <a-radio-button :value="2">实底风格</a-radio-button>
            <a-radio-button :value="3">双色风格</a-radio-button>
        </a-radio-group>
        <div style="margin-top: 15px;">
		<span v-for="(item,i) in iconList" :key="item" class="icons-list">
			<span v-if="value1===1">
				<component class="icon" @click="sendMsg(iconList[i])" v-if="iconList[i].indexOf('Outlined')>0"
                           :is="this.$antIcons[iconList[i]]"/>
			</span>
			<span v-if="value1===2">
				<component class="icon" @click="sendMsg(iconList[i])" v-if="iconList[i].indexOf('Filled')>0"
                           :is="this.$antIcons[iconList[i]]"/>
			</span>
			<span v-if="value1===3">
				<component class="icon" @click="sendMsg(iconList[i])" v-if="iconList[i].indexOf('TwoTone')>0"
                           :is="this.$antIcons[iconList[i]]"/>
			</span>
		</span>
        </div>
    </a-modal>
</template>
<script>
    import {
        defineComponent
    } from 'vue';

    import iconList from './icons.json'

    export default defineComponent({
        components: {},
        data() {
            return {
                iconList: iconList,
                value1: 1,
                visible: false,
            }
        },
        methods: {
            sendMsg: function (iconText) {
                this.$emit('success', iconText)
            },
            open() {
                this.visible = true;
            },
            close() {
                this.visible = false;
            }
        },
    });
</script>
<style scoped>
    .icon{
        font-size: 22px;
        margin-left: 5px;
    }
</style>