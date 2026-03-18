FROM alpine/java:21-jdk AS greadle-download

ENV GRADLE_USER_HOME="/home/.gradle"

WORKDIR /app

COPY gradle gradle
COPY gradlew gradlew

RUN chmod +x ./gradlew
RUN ./gradlew --version

COPY build.gradle.kts build.gradle.kts
COPY settings.gradle.kts settings.gradle.kts

RUN chmod +x ./gradlew
RUN ./gradlew tasks

FROM alpine/java:21-jdk AS runner

ENV GRADLE_USER_HOME="/home/.gradle"

COPY --from=greadle-download /home/.gradle /home/.gradle
COPY --from=greadle-download /app /app

WORKDIR /app

COPY src src

RUN ./gradlew build -x test

CMD ["./gradlew", "bootRun"]