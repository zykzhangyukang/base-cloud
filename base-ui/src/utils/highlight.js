// highlight.js

import hljs from 'highlight.js/lib/core';
import xml from 'highlight.js/lib/languages/xml';

hljs.registerLanguage('xml', xml);

export default {
    inserted: (el) => {
        hljs.highlightBlock(el);
    },
};
