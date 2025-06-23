.

---

## 🚀 React + Spring Boot Full Stack Application

This project is a full-stack CRUD web application using **ReactJS** for the frontend and **Spring Boot** for the backend, connected to a **MySQL** database hosted on **AWS RDS**. Deployment is done on **EC2** instances, and optionally containerized using **Docker**.

---

## 🛠️ System Requirements

Install the following packages on your EC2/Ubuntu instance:

```bash
sudo apt update

# Java 17
sudo apt install openjdk-17-jdk -y

# Maven
sudo apt install maven -y

# npm
sudo apt install npm -y

# Docker
curl -fsSL https://get.docker.com -o get-docker.sh
sudo sh get-docker.sh

# MySQL Client
sudo apt install mysql-client-core-8.0 -y

# AWS CLI
sudo snap install aws-cli --classic

# eksctl (for EKS setup)
curl --silent --location "https://github.com/eksctl-io/eksctl/releases/latest/download/eksctl_$(uname -s)_amd64.tar.gz" | tar xz -C /tmp
sudo mv /tmp/eksctl /usr/local/bin

# kubectl
sudo snap install kubectl --classic

# AWS Configure
aws configure
```

---

## 🔧 Backend Setup (Spring Boot)

### 1. Clone the Repo

```bash
git clone https://github.com/kaushaldarji7182/ReactJS-Spring-Boot-CRUD-Full-Stack-App.git
cd ReactJS-Spring-Boot-CRUD-Full-Stack-App/springboot-backend
```

### 2. Configure Application

Edit `application.properties` with your RDS details:

```properties
spring.datasource.url=jdbc:mysql://<RDS-endpoint>:3306/capstone_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=admin
spring.datasource.password=<your-password>

# Allow frontend CORS
frontend.url=http://<frontend-public-ip>:3000
```

### 3. Build and Run Backend

```bash
mvn clean install
# For foreground run
mvn spring-boot:run

# Or for background
nohup mvn spring-boot:run > backend.log 2>&1 &
```

---

## 🌐 Frontend Setup (ReactJS)

### 1. Update API Endpoint

Edit the following file:

```bash
nano ~/ReactJS-Spring-Boot-CRUD-Full-Stack-App/react-frontend/src/services/EmployeeService.js
```

Update the base URL with backend's **Load Balancer DNS/IP**:

```js
const EMPLOYEE_API_BASE_URL = "http://<backend-lb-dns>:8080/api/v1/employees";
```

Or use `.env` for easy configuration:

```bash
REACT_APP_BACKEND_URL=http://<backend-lb-dns>:8080/api/v1/employees
```

### 2. Run Frontend

```bash
cd ~/ReactJS-Spring-Boot-CRUD-Full-Stack-App/react-frontend

export NODE_OPTIONS=--openssl-legacy-provider
npm install
rm -rf build
npm run build

# Serve production build
npm install -g serve
nohup serve -s build -l 3000 > serve.log 2>&1 &
```

---

## 🐬 MySQL Database

To connect to your RDS DB manually:

```bash
mysql -h <RDS-endpoint> -u admin -p
```

Create the `capstone_db` database and required tables manually or via JPA auto-creation.

---

## 🐳 Docker (Optional)

### 🧱 Frontend Dockerfile

```Dockerfile
FROM node:18-alpine AS builder
ENV NODE_OPTIONS=--openssl-legacy-provider
WORKDIR /app
COPY . .
RUN npm install && npm run build

FROM nginx:alpine
COPY --from=builder /app/build /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
```

### ⚙️ Backend Dockerfile

```Dockerfile
FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
```

---

## 🔄 IP/URL Update Checklist

When EC2 IPs change, make sure to:

1. ✅ Update React frontend `.env` file:

   ```env
   REACT_APP_BACKEND_URL=http://<new-backend-ip>:8080/api/v1/employees
   ```

2. ✅ Update Spring Boot `application.properties`:

   ```properties
   frontend.url=http://<new-frontend-ip>:3000
   ```

---

## 📋 Process Management

### 🔍 Check Running Services

```bash
ps aux | grep mvn
ps aux | grep serve
```

### ❌ Stop Services

```bash
ps -ef | grep spring-boot
ps -ef | grep serve
pkill -9 <PID>
```

---

## ✅ LoadBalancer Integration (Kubernetes/EKS)

In `react-frontend-service.yaml`, set the backend service URL using the LoadBalancer DNS:

```yaml
env:
  - name: REACT_APP_BACKEND_URL
    value: http://<backend-lb-dns>:8080/api/v1/employees
```

---

This should now be copy-paste ready for your repo. Let me know if you want me to include CI/CD steps, `Docker Compose`, or EKS manifests too.
