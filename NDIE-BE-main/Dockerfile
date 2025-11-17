# 1단계: 빌드
FROM gradle:8.4-jdk21 AS builder

WORKDIR /app

# 프로젝트 루트 전체 복사 (settings.gradle.kts, build.gradle.kts, src 등)
COPY . .

# Gradle 빌드 실행
RUN gradle --no-daemon clean bootJar

# 2단계: 런타임
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# 빌드 결과물만 복사
COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
