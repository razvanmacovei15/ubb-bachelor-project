import { defineConfig, loadEnv } from "vite";
import react from "@vitejs/plugin-react";
import path from "path";

export default defineConfig(({ mode }) => {
  // Load .env files based on mode (e.g. .env.production)
  const env = loadEnv(mode, process.cwd(), "");

  return {
    plugins: [react()],
    resolve: {
      alias: {
        "@": path.resolve(__dirname, "src"),
      },
    },
    envPrefix: "VITE_",
    base: "/", // Keep it "/" if served from root (e.g. http://maco-coding.go.ro)

    define: {
      __APP_ENV__: JSON.stringify(env.VITE_ENV), // optional if you want custom constants
    },

    server: {
      port: Number(env.APP_PORT) || 8050,
      host: true,
    },

    build: {
      outDir: "dist",
      sourcemap: mode !== "production", // Only include sourcemaps in dev
    },
  };
});
