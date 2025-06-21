import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import tailwindcss from '@tailwindcss/vite'

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    react(),
    tailwindcss(),
  ],
  server: {
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, '')
      },
      // '/expense-tree': {
      //   target: 'http://localhost:8080/api/v1/expense-tree',
      //   changeOrigin: true,
      //   rewrite: (path) => path.replace(/^\/expense-tree/, '')
      // }
    }
  }
})
