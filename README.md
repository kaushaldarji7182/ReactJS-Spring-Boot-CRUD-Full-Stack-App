# 🚀 React + Spring Boot Full Stack CRUD App

This project is a full-stack CRUD web application using **ReactJS** (frontend) and **Spring Boot** (backend), connected to a **MySQL** database hosted on **AWS RDS**. It is deployable on **AWS EC2** or **EKS**, with optional **Docker** and **Load Balancer** integration.

---

## 🛠️ Environment Setup (On EC2 or Local Ubuntu)

Install the following packages:

```bash
sudo apt update

# Java 17
sudo apt install openjdk-17-jdk -y

# Maven
sudo apt install maven -y

# Node.js & npm
sudo apt install npm -y

# Docker
curl -fsSL https://get.docker.com -o get-docker.sh
sudo sh get-docker.sh

# MySQL Client
sudo apt install mysql-client-core-8.0 -y

# AWS CLI
sudo snap install aws-cli --classic

# eksctl
curl --silent --location "https://github.com/eksctl-io/eksctl/releases/latest/download/eksctl_$(uname -s)_amd64.tar.gz" | tar xz -C /tmp
sudo mv /tmp/eksctl /usr/local/bin

# kubectl
sudo snap install kubectl --classic

# Configure AWS CLI
aws configure
```

---

## 📦 Clone the Project

```bash
git clone https://github.com/kaushaldarji7182/ReactJS-Spring-Boot-CRUD-Full-Stack-App.git
```

---

## 🔧 Backend Setup (Spring Boot)

```bash
cd ReactJS-Spring-Boot-CRUD-Full-Stack-App/springboot-backend
```

### Configure Database Connection

Edit `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://<RDS-endpoint>:3306/capstone_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=admin
spring.datasource.password=<your-password>

# CORS origin for frontend
frontend.url=http://<frontend-lb-dns>:3000
```

### Run Backend

```bash
mvn clean install
# To run in foreground
mvn spring-boot:run

# To run in background
nohup mvn spring-boot:run > backend.log 2>&1 &
```

---

## 🌐 Frontend Setup (ReactJS)

```bash
cd ../react-frontend
```

### Configure Backend URL

**Option 1: Use `.env` file (recommended)**
Create or edit `react-frontend/.env`:

```env
REACT_APP_BACKEND_URL=http://<backend-lb-dns>:8080/api/v1/employees
```

**Option 2: Hardcode in `EmployeeService.js`**

```js
const EMPLOYEE_API_BASE_URL = "http://<backend-lb-dns>:8080/api/v1/employees";
```

### Run Frontend

```bash
export NODE_OPTIONS=--openssl-legacy-provider
npm install
rm -rf build
npm run build
npm install -g serve
nohup serve -s build -l 3000 > serve.log 2>&1 &
```

---

## 🐬 MySQL Access (via RDS)

To test DB connection:

```bash
mysql -h <RDS-endpoint> -u admin -p
```

Then create the database manually:

```sql
CREATE DATABASE capstone_db;
```

---

## 🐳 Docker Support (Optional)

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

## 🔄 IP/URL Update Checklist (EC2 Public IP or Load Balancer DNS)

When your **EC2 IP** or **Load Balancer DNS** changes, update the following:

### ✅ 1. React Frontend `.env`

**Path**: `react-frontend/.env`

```env
REACT_APP_BACKEND_URL=http://<backend-lb-dns>:8080/api/v1/employees
# or
REACT_APP_BACKEND_URL=http://<backend-ec2-ip>:8080/api/v1/employees
```

---

### ✅ 2. Spring Boot `application.properties`

**Path**: `springboot-backend/src/main/resources/application.properties`

```properties
frontend.url=http://<frontend-lb-dns>:3000
# or
frontend.url=http://<frontend-ec2-ip>:3000
```

---

### ✅ 3. `EmployeeService.js` (if not using `.env`)

**Path**: `react-frontend/src/services/EmployeeService.js`

```js
const EMPLOYEE_API_BASE_URL = "http://<backend-lb-dns>:8080/api/v1/employees";
// or
const EMPLOYEE_API_BASE_URL = "http://<backend-ec2-ip>:8080/api/v1/employees";
```

---

## 🧪 Process Management

### Check running processes:

```bash
ps aux | grep mvn
ps aux | grep serve
```

### Stop processes:

```bash
ps -ef | grep spring-boot
ps -ef | grep serve
pkill -9 <PID>
```

---

