# Stage 1: Build the native image
FROM ghcr.io/graalvm/native-image:latest as builder

WORKDIR /app
COPY . .

# Build native binary
RUN ./gradlew clean nativeCompile

# Stage 2: Create a minimal Docker image
FROM alpine:latest
WORKDIR /app

# Install necessary dependencies for native image execution
RUN apk add --no-cache libstdc++

WORKDIR /app
COPY --from=build /app/build/native/nativeCompile/api-service /app/api-service

# Expose port (ubah sesuai dengan service Anda)
EXPOSE 40081

# Jalankan aplikasi
CMD ["./api-service"]
