# Этап сборки
FROM node:22-alpine as build

WORKDIR /app
COPY package*.json ./

RUN npm install

COPY . .

RUN npm run build

# Этап production
FROM nginx:alpine

# Копируем собранное приложение
COPY --from=build /app/dist /usr/share/nginx/html

# Копируем кастомную nginx конфигурацию для SPA (опционально)
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]