FROM ubuntu:16.04
MAINTAINER Alexander Kiryanenko <kiryanenkoav@gmail.com>

RUN apt-get update
# Для сборки Nginx понадобится Perl библиотека регулярных выражений и заголовки OpenSSL
RUN apt-get install -y gcc make nginx libpcre3 libpcre3-dev libssl-dev

# Копируем исходный код в Docker-контейнер
ENV APP /root/app
ADD ./ $APP

# Сборка Nginx с поддержкой rtmp
WORKDIR $APP/nginx/nginx-1.6.0
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

# Установка JDK
RUN apt-get install -y openjdk-8-jdk-headless
RUN apt-get install -y maven

# Собираем и устанавливаем пакет
WORKDIR $APP/app
RUN mvn package

# Make ports available to the world outside this container
EXPOSE 80
EXPOSE 1935

# run the application
CMD service nginx start && java -Dserver.port=8000 -Xmx300M -Xmx300M -jar target/*.jar
