// highlightDirective.js
import hljs from 'highlight.js';
export const highlightjs = {
    mounted(el) {
        hljs.highlightElement(el);
    },
};
