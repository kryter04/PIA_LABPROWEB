// tailwind.config.js

/** @type {import('tailwindcss').Config} */
module.exports = { // Nota: aquí es module.exports
  content: ['./src/**/*.{astro,html,js,jsx,md,mdx,svelte,ts,tsx,vue}'],
  theme: {
    extend: {
      // Aquí metemos nuestros colores personalizados
      colors: {
        // Este es el verde chido de tus wireframes
        'brand-green': '#10B981',
        // Este es el rojo "peligro" para borrar cosas
        'brand-red': '#EF4444',
      },
    },
  },
  // Le añadimos un plugin para que los formularios se vean bien sin esfuerzo
  plugins: [
    require('@tailwindcss/forms'),
  ],
};