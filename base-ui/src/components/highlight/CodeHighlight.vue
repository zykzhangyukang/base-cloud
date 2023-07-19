<template>
    <div>
        <pre><code ref="codeBlock" :class="language">{{ code }}</code></pre>
    </div>
</template>

<script>
    import hljs from 'highlight.js/lib/core';
    import 'highlight.js/styles/dark.css';
    export default {
        name: "CodeHighlight.vue",
        props: {
            code: {
                type: String,
                required: true,
            },
            language: {
                type: String,
                default: 'plaintext', // 默认语言为纯文本，如果没有指定语言，代码将以纯文本形式显示
            },
        },
        mounted() {
            this.highlightCode();
        },
        updated() {
            this.highlightCode();
        },
        methods: {
            highlightCode() {
                const languages = hljs.listLanguages(); // 获取所有支持的语言列表
                hljs.highlightBlock(this.$refs.codeBlock); // 高亮代码
                if (!languages.includes(this.language)) {
                    // 如果指定的语言不受支持，则使用纯文本形式显示代码
                    this.$refs.codeBlock.classList.add('hljs-plaintext');
                }
            },
        },
    };
</script>

<style scoped>

</style>