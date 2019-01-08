
up:
	cat docker-compose.yml | sed "s/<password>/$(shell cat ./secrets/mongopass.txt)/g" | \
		docker-compose --tlsverify -H=dx30.local:2376 --tlscacert=/home/antoine/rasp/infra/dx30/dockerremote/certs/ca.pem \
		--tlscert=/home/antoine/rasp/infra/dx30/dockerremote/certs/client-dockerClient-cert.pem \
		--tlskey=/home/antoine/rasp/infra/dx30/dockerremote/certs/client-dockerClient-key.pem \
		-f - up -d --build

down:
	docker-compose --tlsverify -H=dx30.local:2376 --tlscacert=/home/antoine/rasp/infra/dx30/dockerremote/certs/ca.pem \
		--tlscert=/home/antoine/rasp/infra/dx30/dockerremote/certs/client-dockerClient-cert.pem \
		--tlskey=/home/antoine/rasp/infra/dx30/dockerremote/certs/client-dockerClient-key.pem \
		down

connect:
	mongo --authenticationDatabase=admin -u spring -p $(shell cat ./secrets/mongopass.txt) dx30.local:27017/application --shell

exec:
	docker --tlsverify -H=dx30.local:2376 \
	--tlscacert=/home/antoine/rasp/infra/dx30/dockerremote/certs/ca.pem \
	--tlscert=/home/antoine/rasp/infra/dx30/dockerremote/certs/client-dockerClient-cert.pem \
	--tlskey=/home/antoine/rasp/infra/dx30/dockerremote/certs/client-dockerClient-key.pem \
	exec -it mongodb bash