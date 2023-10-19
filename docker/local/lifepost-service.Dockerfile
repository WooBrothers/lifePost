FROM openjdk:17-jdk-slim

# 작업 디렉토리 설정
WORKDIR /home/lifepost/service

# 빌드 단계에서 생성된 JAR 파일을 복사합니다.
#COPY service.jar /home/lifepost/service/service.jar
COPY ./service.jar /home/lifepost/service/service.jar

# 포트 8080을 노출합니다.
EXPOSE 8080

# 스프링 부트 애플리케이션을 실행합니다.
CMD ["java", "-jar", "service.jar", "--spring.profiles.active=local"]