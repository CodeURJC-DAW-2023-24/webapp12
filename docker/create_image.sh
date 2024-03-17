#sets build context to parent and builds using Dockerfile from docker directory
docker build -t daarling/yourhometel -f Dockerfile ..
#Pushes the image to dockerhub
docker push daarling/yourhometel
#Run the image using docker-compose from docker directory
docker-compose up