'use strict';
const EndWebpackPlugin = require('./plugins/webpack.end.js');
module.exports = {
  devServer: {
    port: 9991,
    open: false,
    proxy: {
      '/api': {
        target: 'http://127.0.0.1:8000/api',
        ws: true,
        pathRewrite: {'^/api' : ''},
        changeOrigin: true
      },
    }
  },
  configureWebpack: config => {
    if (process.env.ENV === 'production') {
      config.plugins.push(new EndWebpackPlugin('dist','微服务系统'))
    }
  },
  chainWebpack: (config) => {
    // config.resolve.alias
    //   .set('@', resolve('src')) 
    //   .end();
  },
  css: {
    loaderOptions: {
      less: {
        lessOptions: {
          modifyVars: {
            // 'primary-color': '#1890ff', // 主色调
            // 'link-color': '#1890ff', // 链接颜色
          },
          javascriptEnabled: true,
        },
      },
    },
  },
};