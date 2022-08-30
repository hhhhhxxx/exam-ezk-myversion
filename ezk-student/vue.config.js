'use strict'
const path = require('path')

function resolve (dir) {
  return path.join(__dirname, dir)
}

module.exports = {
  publicPath: './',
  outputDir: 'student',
  assetsDir: 'static',
  lintOnSave: true,
  productionSourceMap: false,
  devServer: {
    open: true,
    host: 'localhost',
    port: 9500,
    https: false,
    hotOnly: false,
    proxy: {
      '/api': {
        target: 'http://localhost:88',
        changeOrigin: true
      },
      '^/boss': {
        target: 'http://localhost:88',
        // pathRewrite: { "^/boss": "/" },
        changeOrigin: true
      },
      '^/front': {
        target: 'http://localhost:88',
        patRewrite: { '^/api': '/' },
        changeOrigin: true
      },
      // '^/user': {
      //   target: 'http://localhost:8890',
      //   patRewrite: { '^/user': '/' },
      //   changeOrigin: true
      // }
      '^/user': {
        target: 'http://localhost:88',
        // patRewrite: { '^/user': '/' },
        changeOrigin: true
      }
    }
  },
  pages: {
    index: {
      entry: 'src/main.js',
      template: 'public/index.html',
      filename: 'index.html'
    }
  },
  chainWebpack (config) {
    // set svg-sprite-loader
    config.module
      .rule('svg')
      .exclude.add(resolve('src/icons'))
      .end()
    config.module
      .rule('icons')
      .test(/\.svg$/)
      .include.add(resolve('src/icons'))
      .end()
      .use('svg-sprite-loader')
      .loader('svg-sprite-loader')
      .options({
        symbolId: 'icon-[name]'
      })
      .end()
  }
}
