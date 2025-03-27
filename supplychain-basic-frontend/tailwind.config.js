/** @type {import('tailwindcss').Config} */
export default {
    content: [
      "./index.html",
      "./src/**/*.{vue,js,ts,jsx,tsx}",
    ],
    theme: {
      extend: {
        colors: {
          primary: "#1e88e5",
          "primary-dark": "#0a2463",
          "primary-light": "#e3f2fd",
          secondary: "#ffa000",
          success: "#4caf50",
          danger: "#e53935",
          warning: "#ff9800",
          info: "#2196f3",
        }
      },
    },
    plugins: [],
  }