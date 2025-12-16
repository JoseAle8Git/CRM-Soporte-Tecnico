# Dockerfile (Ubicación: ./frontend/Dockerfile)

# Fase de Construcción (Build Phase) - Nodo
FROM node:20 AS builder

# Directorio de trabajo
WORKDIR /app

# Copiar los archivos de definición del proyecto
COPY package.json package-lock.json ./

# Instalar dependencias
RUN npm install

# Copiar el resto del código
COPY . .

# Compilar el proyecto para producción
# Ajusta el nombre del proyecto si es necesario. La salida va a /app/dist/<project-name>
# Ejemplo: ng build --configuration production --output-path=./dist
RUN npm run build -- --configuration production

# Fase de Ejecución (Runtime Phase) - Nginx ligero
FROM nginx:alpine

# Copiar los archivos compilados del frontend a la carpeta de Nginx
COPY --from=builder /app/dist/frontend/browser /usr/share/nginx/html

# Puerto por defecto de Nginx
EXPOSE 80

# Comando por defecto de Nginx
CMD ["nginx", "-g", "daemon off;"]