CC = python3
SERVER = -s 
PORT = 2626
CLIENT = -c

server: server.py ChatApp.py
	$(CC) ChatApp.py $(SERVER) $(PORT)

cli1: server.py ChatApp.py client.py
	$(CC) ChatApp.py $(CLIENT) Tom 10.150.0.3 $(PORT) 2900 

cli2: server.py ChatApp.py client.py
	$(CC) ChatApp.py $(CLIENT) Joe 10.150.0.3 $(PORT) 2361 

cli3: server.py ChatApp.py client.py
	$(CC) ChatApp.py $(CLIENT) Bob 10.150.0.3 $(PORT) 2802 
