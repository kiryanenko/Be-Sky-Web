FROM ubuntu:16.04
FROM python:3-onbuild
MAINTAINER Alexander Kiryanenko <kiryanenkoav@gmail.com>

# Set the working directory to /app
WORKDIR /app
# Copy the current directory contents into the container at /app
ADD ./app /app

RUN apt-get update
RUN apt-get install -y nginx libpcre++-dev libssl-dev libxslt1-dev libgd2-xpm-dev libgeoip-dev

ADD be-sky.conf /etc/nginx/sites-available
RUN ln -s /etc/nginx/sites-available/be-sky.conf /etc/nginx/sites-enabled/
RUN rm /etc/nginx/sites-enabled/default

ADD nginx.conf /etc/nginx

RUN nginx -t
RUN service nginx restart

# Make port 80 available to the world outside this container
EXPOSE 80

# run the application
CMD service nginx start && gunicorn app:app
