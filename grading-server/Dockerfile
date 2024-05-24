# 베이스 이미지로 OpenJDK 17 Slim을 사용
FROM openjdk:17-slim

# 패키지 정보 업데이트 및 필요한 의존성 설치
RUN apt-get update && apt-get install -y \
    python3 \
    python3-pip \
    libpython3-dev \
    build-essential \
    time

RUN pip3 install psutil

# 앱 jar 파일 복사
ARG JAR_FILE=build/libs/app.jar
COPY ${JAR_FILE} ./app.jar

# 타임존 설정 (필요한 경우)
ENV TZ=Asia/Seoul

# Java 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "./app.jar"]