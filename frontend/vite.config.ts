import { fileURLToPath, URL } from 'node:url'
import { defineConfig } from 'vitest/config'
import vue from '@vitejs/plugin-vue'
import vuetify, { transformAssetUrls } from 'vite-plugin-vuetify'
import vueDevTools from 'vite-plugin-vue-devtools'

export default defineConfig({
  // Reuse the repository-root `.env` (shared with the Spring Boot backend)
  // instead of keeping a second frontend-only file in sync.
  envDir: fileURLToPath(new URL('..', import.meta.url)),
  plugins: [
    vue({
      template: { transformAssetUrls },
    }),
    vuetify({ autoImport: true, styles: { configFile: 'src/styles/settings.scss' } }),
    vueDevTools(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url)),
    },
  },
  // The Vuetify plugin rewrites component styles to virtual SASS modules when a
  // configFile is used. Vite's dependency pre-bundler cannot resolve those
  // virtual modules, so keep Vuetify out of the optimized deps.
  optimizeDeps: {
    exclude: ['vuetify'],
  },
  test: {
    environment: 'node',
    include: ['src/tests/**/*.spec.ts'],
  },
})
