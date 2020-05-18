# Getting Started

### Init DB
```postgres-sql
CREATE DATABASE octopusdb;
CREATE ROLE octopus WITH LOGIN NOSUPERUSER INHERIT CREATEDB NOCREATEROLE NOREPLICATION PASSWORD 'changeme';
GRANT ALL PRIVILEGES ON DATABASE octopusdb TO octopus;
```

### Install as systemd service
Step 1: Create an Application User and group
```shell script
sudo groupadd -r octopus
sudo useradd -r -s /bin/false -g octopus octopus
```
Step 2: Create Systemd Service

Create work directory 
```shell script
sudo mkdir -p /octopus/backend
sudo chown -R octopus:octopus /octopus/backend
```
Create systemd service
```shell script
sudo vim /etc/systemd/system/octopus.service
[Unit]
Description=Octopus Spring Boot service

[Service]
WorkingDirectory=/octopus/backend
ExecStart=/usr/bin/java -Xms128m -Xmx512m -jar octopus-*.jar
User=octopus
Type=simple
Restart=on-failure
RestartSec=10

[Install]
WantedBy=multi-user.target
```

Step 3: Start Java Application service with systemd
```shell script
# To start service during boot up of the host
sudo systemctl enable octopus.service

# To check service status
sudo systemctl status octopus.service

# To start/stop/restart service 
sudo systemctl start octopus.service
sudo systemctl stop octopus.service
sudo systemctl restart octopus.service
```

Accessing the logs with journalctl
```shell script
sudo journalctl -u octopus.service -f
```

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.2.6.RELEASE/maven-plugin/)
* [Spring Security](https://docs.spring.io/spring-boot/docs/2.2.6.RELEASE/reference/htmlsingle/#boot-features-security)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.2.6.RELEASE/reference/htmlsingle/#boot-features-developing-web-applications)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/2.2.6.RELEASE/reference/htmlsingle/#boot-features-jpa-and-spring-data)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/docs/2.2.6.RELEASE/reference/htmlsingle/#using-boot-devtools)

### Guides
The following guides illustrate how to use some features concretely:

* [Securing a Web Application](https://spring.io/guides/gs/securing-web/)
* [Spring Boot and OAuth2](https://spring.io/guides/tutorials/spring-boot-oauth2/)
* [Authenticating a User with LDAP](https://spring.io/guides/gs/authenticating-ldap/)
* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/bookmarks/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)

