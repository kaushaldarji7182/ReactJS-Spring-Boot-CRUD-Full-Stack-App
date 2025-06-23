# 🚀 React + Spring Boot Full Stack CRUD App

This project is a full-stack CRUD web application built with **ReactJS** (frontend) and **Spring Boot** (backend), connected to a **MySQL** database hosted on **AWS RDS**. It can be deployed on **EC2**, **EKS**, or run locally with optional **Docker** support.

---

## 🛠️ Initial Setup (Ubuntu EC2 or Local)

Run the following commands in your EC2 terminal:

```bash
sudo apt update

# Java 17 for Spring Boot
sudo apt install openjdk-17-jdk -y

# Maven for building backend
sudo apt install maven -y

# npm for React
sudo apt install npm -y

# Docker
curl -fsSL https://get.docker.com -o get-docker.sh
sudo sh get-docker.sh

# MySQL client
sudo apt install mysql-client-core-8.0 -y

# AWS CLI
sudo snap install aws-cli --classic

# eksctl (optional)
curl --silent --location "https://github.com/eksctl-io/eksctl/releases/latest/download/eksctl_$(uname -s)_amd64.tar.gz" | tar xz -C /tmp
sudo mv /tmp/eksctl /usr/local/bin

# kubectl
sudo snap install kubectl --classic
```

---

## 📦 Clone the Repository

```bash
git clone https://github.com/kaushaldarji7182/ReactJS-Spring-Boot-CRUD-Full-Stack-App.git
cd ReactJS-Spring-Boot-CRUD-Full-Stack-App
```

---

## ⚙️ Backend Setup (Spring Boot)

### 🔧 Step 1: Edit `application.properties`

**File path**:

```bash
springboot-backend/src/main/resources/application.properties
```

**Command to edit**:

```bash
nano springboot-backend/src/main/resources/application.properties
```

**Update the file with your RDS and frontend DNS/IP**:

```properties
spring.datasource.url=jdbc:mysql://<RDS-endpoint>:3306/capstone_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=admin
spring.datasource.password=<your-password>

# CORS allowed origin
frontend.url=http://<frontend-lb-dns>:3000
# or use EC2 public IP
# frontend.url=http://<frontend-ec2-ip>:3000
```

---

### ▶️ Step 2: Build and Run Backend

**Go to backend folder**:

```bash
cd springboot-backend
```

**Run these commands**:

```bash
mvn clean install
```

To run in **foreground**:

```bash
mvn spring-boot:run
```

To run in **background**:

```bash
nohup mvn spring-boot:run > backend.log 2>&1 &
```

---

## 🌐 Frontend Setup (React)

### 🔧 Step 1: Set Backend API URL

#### Option 1: Edit `.env` (Recommended)

**File path**:

```bash
react-frontend/.env
```

**Command to edit**:

```bash
nano react-frontend/.env
```

**Update**:

```env
REACT_APP_BACKEND_URL=http://<backend-lb-dns>:8080/api/v1/employees
# or
# REACT_APP_BACKEND_URL=http://<backend-ec2-ip>:8080/api/v1/employees
```

#### Option 2: Edit hardcoded URL in `EmployeeService.js`

**File path**:

```bash
react-frontend/src/services/EmployeeService.js
```

**Command to edit**:

```bash
nano react-frontend/src/services/EmployeeService.js
```

**Update**:

```js
const EMPLOYEE_API_BASE_URL = "http://<backend-lb-dns>:8080/api/v1/employees";
// or
// const EMPLOYEE_API_BASE_URL = "http://<backend-ec2-ip>:8080/api/v1/employees";
```

---

### ▶️ Step 2: Build and Run Frontend

**Go to frontend folder**:

```bash
cd react-frontend
```

**Run these commands**:

```bash
export NODE_OPTIONS=--openssl-legacy-provider
npm install
rm -rf build
npm run build
npm install -g serve
nohup serve -s build -l 3000 > serve.log 2>&1 &
```

---

## 🐬 Connect to MySQL RDS

**Command**:

```bash
mysql -h <RDS-endpoint> -u admin -p
```

Then create the database:

```sql
CREATE DATABASE capstone_db;
```

---

## 🐳 Docker Setup (Optional)

### 🧱 Frontend Dockerfile

**File path**:

```bash
react-frontend/Dockerfile
```

```dockerfile
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

---

### ⚙️ Backend Dockerfile

**File path**:

```bash
springboot-backend/Dockerfile
```

```dockerfile
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

## 🔄 When IP/DNS Changes (EC2 or LoadBalancer)

Update these values **whenever your EC2 public IP or Load Balancer DNS changes**:

### ✅ Frontend `.env`

**Path**:

```bash
react-frontend/.env
```

```env
REACT_APP_BACKEND_URL=http://<backend-lb-dns>:8080/api/v1/employees
# or
REACT_APP_BACKEND_URL=http://<backend-ec2-ip>:8080/api/v1/employees
```

---

### ✅ Backend `application.properties`

**Path**:

```bash
springboot-backend/src/main/resources/application.properties
```

```properties
frontend.url=http://<frontend-lb-dns>:3000
# or
frontend.url=http://<frontend-ec2-ip>:3000
```

---

### ✅ Hardcoded URL in `EmployeeService.js` (if not using .env)

**Path**:

```bash
react-frontend/src/services/EmployeeService.js
```

```js
const EMPLOYEE_API_BASE_URL = "http://<backend-lb-dns>:8080/api/v1/employees";
```

---

## 🧪 Process Management

### 🔍 Check running processes:

```bash
ps aux | grep mvn
ps aux | grep serve
```

### ❌ Stop processes:

```bash
ps -ef | grep spring-boot
ps -ef | grep serve
```

### 🔪 Kill by PID:

```bash
pkill -9 <PID>
```

---

Let me know if you'd like to add Kubernetes (`.yaml`) manifests, a `docker-compose.yml`, or CodePipeline automation instructions next!
