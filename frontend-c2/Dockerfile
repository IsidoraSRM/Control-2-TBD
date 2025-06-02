# Usamos una imagen ligera de Node para compilar y servir la aplicación
FROM node:18-alpine

# Establecemos el directorio de trabajo en el contenedor
WORKDIR /app

# Copiamos los archivos de dependencias y los instalamos
COPY package*.json ./
RUN npm install

# Copiamos el resto del código fuente
COPY . .

# Construimos la aplicación; Vite generará la versión de producción en la carpeta "dist"
RUN npm run build

# Exponemos el puerto en el que se servirá la aplicación
EXPOSE 3000

# Ejecutamos la aplicación en modo preview para ver la versión producida
CMD ["npx", "vite", "preview", "--host", "0.0.0.0", "--port", "3000"]