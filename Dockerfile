FROM ubuntu:16.04
FROM python:3-onbuild
MAINTAINER Alexander Kiryanenko <kiryanenkoav@gmail.com>

RUN apt-get update
# Для сборки Nginx понадобится Perl библиотека регулярных выражений и заголовки OpenSSL
RUN apt-get install -y nginx libpcre3 libpcre3-dev libssl-dev

# Сборка Nginx с поддержкой rtmp
WORKDIR ./nginx/nginx-1.6.0
# Конфигурируем Nginx
RUN ./configure \
    --prefix=/usr \
    --add-module=../nginx-rtmp-module/ \
    --pid-path=/var/run/nginx.pid \
    --conf-path=/etc/nginx/nginx.conf \
    --error-log-path=/var/log/nginx/error.log \
    --http-log-path=/var/log/nginx/access.log \
    --with-http_ssl_module
# Компилируем и устанавливаем
RUN make && make install
# Скопируем файл stat.xsl из папки с исходниками в папку nginx
ADD nginx/nginx-rtmp-module/stat.xsl /etc/nginx/

RUN rm /etc/nginx/sites-enabled/default

ADD nginx/be-sky.conf /etc/nginx/sites-available
RUN ln -s /etc/nginx/sites-available/be-sky.conf /etc/nginx/sites-enabled/

ADD nginx/nginx.conf /etc/nginx

RUN nginx -t
RUN service nginx restart


# Make ports available to the world outside this container
EXPOSE 80
EXPOSE 1935


# Set the working directory to /app
WORKDIR /app
# Copy the current directory contents into the container at /app
ADD ./app /app


# run the application
CMD service nginx start && gunicorn app:app
