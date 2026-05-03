# Giai đoạn 1: Build dự án
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
# Sửa lại: bỏ chữ demo/ vì pom.xml nằm ngay tại đây
RUN mvn clean package -DskipTests

# Giai đoạn 2: Chạy dự án
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
# Sửa lại: Copy từ thư mục target (không có demo/)
COPY data.db data.db
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
