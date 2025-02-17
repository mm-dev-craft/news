FROM nginx:latest
LABEL authors="michell"

RUN rm /etc/nginx/conf.d/default.conf

# Copy the custom nginx configuration file
COPY ./nginx/nginx.conf /etc/nginx/nginx.conf

# Copy the dist folder to the location specified in the nginx.conf
COPY ./dist /usr/share/nginx/html

# Expose port 80
EXPOSE 80

# Start nginx
CMD ["nginx", "-g", "daemon off;"]