# Makefile cho Maven Project với Docker support

MVN=mvn
MODULE?=.

# Thông tin docker image
DOCKER_REGISTRY?=harbor.local/vcorestack
IMAGE_NAME?=bpm-gateway
IMAGE_TAG?=latest
CONTAINER_NAME?=bpm-gateway

.PHONY: help clean compile test package install run build build-skip docker-build docker-run docker-stop docker-push

help:
	@echo "Các lệnh hỗ trợ:"
	@echo "  make clean          - Xoá target"
	@echo "  make compile        - Compile project"
	@echo "  make test           - Chạy unit test"
	@echo "  make package        - Đóng gói JAR/WAR"
	@echo "  make install        - Cài artifact vào local repo"
	@echo "  make run            - Chạy Spring Boot app (bpm-gateway)"
	@echo "  make build          - Clean + package"
	@echo "  make build-skip     - Clean + package (bỏ qua test)"
	@echo "  make docker-build   - Build docker image cho bpm-gateway"
	@echo "  make docker-run     - Run container bpm-gateway"
	@echo "  make docker-stop    - Stop container bpm-gateway"
	@echo "  make docker-push    - Push docker image lên registry"

clean:
	$(MVN) -f pom.xml clean

compile:
	$(MVN) -f pom.xml compile

test:
	$(MVN) -f pom.xml test

package:
	$(MVN) -f pom.xml package

install:
	$(MVN) -f pom.xml install

build:
	$(MVN) -f pom.xml clean package

build-skip:
	$(MVN) -f pom.xml clean package -DskipTests

run:
	$(MVN) -f bpm-gateway/pom.xml spring-boot:run

docker-build:
	docker build -t $(DOCKER_REGISTRY)/$(IMAGE_NAME):$(IMAGE_TAG) bpm-gateway

docker-run:
	docker run -d --name $(CONTAINER_NAME) -p 8080:8080 $(DOCKER_REGISTRY)/$(IMAGE_NAME):$(IMAGE_TAG)

docker-stop:
	docker stop $(CONTAINER_NAME) || true && docker rm $(CONTAINER_NAME) || true

docker-push:
	docker push $(DOCKER_REGISTRY)/$(IMAGE_NAME):$(IMAGE_TAG)
