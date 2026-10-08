import { fileURLToPath, URL } from 'node:url'
import { defineConfig } from 'vitest/config'
import vue from '@vitejs/plugin-vue'
import vuetify, { transformAssetUrls } from 'vite-plugin-vuetify'
import vueDevTools from 'vite-plugin-vue-devtools'

const settingsFile = fileURLToPath(new URL('./src/styles/settings.scss', import.meta.url))

export default defineConfig({
  // Reuse the repository-root `.env` (shared with the Spring Boot backend)
  // instead of keeping a second frontend-only file in sync.
  envDir: fileURLToPath(new URL('..', import.meta.url)),
  plugins: [
    vue({
      template: { transformAssetUrls },
    }),
    // `styles: 'sass'` points component styles at the real `.sass` sources
    // instead of the `virtual:plugin-vuetify:*` modules the plugin generates
    // when given a `configFile`. Those virtual modules only exist after their
    // importing `.js` file has been resolved, so a dev-server pre-transform can
    // request them first and fail with "Failed to load url
    // virtual:plugin-vuetify:...". The theme variables are injected through the
    // Sass preprocessor below instead.
    vuetify({ autoImport: true, styles: 'sass' }),
    vueDevTools(),
  ],
  css: {
    preprocessorOptions: {
      sass: {
        additionalData: (source: string, filename: string) => {
          // settings.scss configures Vuetify via `@use ... with (...)`, so it
          // must not be prepended to itself.
          if (filename.includes('settings.scss')) return source
          return `@use "${settingsFile.replace(/\\/g, '/')}" as *\n${source}`
        },
      },
    },
  },
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url)),
    },
  },
  // Keep Vuetify out of the dependency pre-bundler; otherwise its precompiled
  // `.css` files are used and the custom Sass settings above are ignored.
  optimizeDeps: {
    exclude: ['vuetify'],
  },
  test: {
    environment: 'node',
    include: ['src/tests/**/*.spec.ts'],
  },
})
