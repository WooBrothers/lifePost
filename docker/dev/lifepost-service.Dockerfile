# 1단계: Maven을 사용하여 스프링 부트 애플리케이션 빌드
FROM maven:3.8.3-openjdk-17-slim AS build

COPY ../../../member /member

WORKDIR /member

# 애플리케이션 빌드
RUN mvn clean install

# 2단계: Java 17을 기반으로 하는 공식 OpenJDK 이미지를 사용하여 실행 이미지 생성
FROM adoptopenjdk/openjdk17:alpine-slim

# 작업 디렉토리 설정
WORKDIR /member

# 빌드 단계에서 생성된 JAR 파일을 복사합니다.
COPY --from=build /app/target/your-spring-boot-app.jar /app/app.jar

# 포트 8080을 노출합니다.
EXPOSE 8080

# 스프링 부트 애플리케이션을 실행합니다.
CMD ["java", "-jar", "app.jar"]