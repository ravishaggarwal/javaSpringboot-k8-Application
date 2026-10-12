# app-k8-deployment Health — Local Deployment on Docker Desktop Kubernetes

This README documents how to build and run the app-k8-deployment Spring Boot application locally on a Mac using Docker Desktop Kubernetes and Helm, with **Horizontal Pod Autoscaler (HPA)** and **PodDisruptionBudget (PDB)** enabled. .

Check the cluster and tools:

```bash
kubectl config use-context docker-desktop
kubectl get nodes
helm version
docker version
```

## 1. Build and test the Spring Boot application

```
APP_VERSION=1.0.0 ./gradlew clean test bootJar
```

Optional component tests:

```bash
./gradlew componentTest
```

Confirm the generated JAR:

```bash
ls -lh build/libs/app-k8-deployment-health-1.0.0.jar
```


## 2. Build the local Docker image

```bash
cd ..
docker build \
  --build-arg APP_VERSION=1.0.0 \
  -f container/Dockerfile \
  -t app-k8-deployment-health:1.0.0 \
  .

docker images app-k8-deployment-health
```

Docker Desktop Kubernetes can normally use images built in Docker Desktop's image store directly. The local Helm values below set `image.pullPolicy: Never`, so Kubernetes will not try to pull from JFrog.



## 3. Build the local Docker image Deploy to Kubernetes using Helm charts:

```bash
helm upgrade --install app-k8-deployment-health \
  ./helm/app-k8-deployment-health \
  --namespace app-k8-deployment-local-Mac \
  --create-namespace \
  -f ./helm/app-k8-deployment-health/values-localMac.yaml \
  --wait \
  --timeout 5m
```

Verify:

```bash
kubectl get pods,deploy,svc,hpa,pdb -n app-k8-deployment-local-Mac
kubectl rollout status deployment/app-k8-deployment-health -n app-k8-deployment-local-Mac
kubectl logs -n app-k8-deployment-local-Mac deployment/app-k8-deployment-health
```


## 4. Test the application and Actuator endpoints

```bash
kubectl exec -it -n app-k8-deployment-local-Mac deployment/app-k8-deployment-health -- /bin/sh
```


Inside the debug container:

```bash
curl http://localhost:8080/health
curl http://localhost:8080/actuator/health
curl http://localhost:8080/actuator/health/readiness
curl http://localhost:8080/actuator/health/liveness
exit
```

## 5. Test the application and Actuator endpoints
```json
{
  "status": "success",
  "message": "application is healthy",
  "version": "1.0.0",
  "environment": "app-k8-deployment-local"
}
```

