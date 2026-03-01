# Export environment from .env
set -a
source .env
set +a

docker compose down

# Package account-service
cd ./account-service && ./mvnw clean package -DskipTests=true && cd ..

# Package api-gateway
cd ./api-gateway && ./mvnw clean package -DskipTests=true && cd ..

# Package cash-service
cd ./cash-service && ./mvnw clean package -DskipTests=true && cd .. 

# Package my-bank-front-app
cd ./my-bank-front-app && ./mvnw clean package -DskipTests=true && cd .. 

# Package notification-service
cd ./notification-service && ./mvnw clean package -DskipTests=true && cd .. 

# Package transfer-service
cd ./transfer-service && ./mvnw clean package -DskipTests=true && cd .. 

# docker-compose up -d --build