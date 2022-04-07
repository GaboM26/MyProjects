###########################################
#MAIN CLASS
#AUTHOR: David Gabriel Millares Bellido
#UNI: DGM2148
#Programming Project 1 - Chat Application
###########################################


#Packages utilized
import sys, getopt
from server import *
from client import *
import socket

def validInput(a): #Checks if there are correct arguments to start client
    if(len(a) != 6):
        return False

    try:
        socket.inet_aton(a[3])
        return True
    except socket.error:
        return False
    return True
##########################################################################3333
#START OF MAIN 
if(len(sys.argv) < 2): #Quick check for Valid Input
    print("Error: must run with <mode> <command-line args>")
    quit()

mode = sys.argv[1]

if(mode == "-c"): #Client program
    if(validInput(sys.argv)): 
        port1 = int(sys.argv[4])
        port2 = int(sys.argv[5])
        if(1024 <= port1 and port1 <= 65535 and 1024 <= port2 and port2 <= 65535):
            try:
                c = client(sys.argv[2], sys.argv[3], sys.argv[4], sys.argv[5])
                c.runClient()
            except KeyboardInterrupt:
                c.run = False
                c.cs.settimeout(0.001)
                print("[Silent Leave]")
                print(">>>[Goodbye]")
        else:
            print("Error - Port number(s) invalid")
    else:
        print("Error - Invalid client args")
        quit()
elif(mode == "-s"): #Server program
    try:
        port = int(sys.argv[2])
        if(1024 <= port and  port <= 65535):
            try:
                s = server(port)
                s.activateServer()
            except KeyboardInterrupt:
                print("[SERVER GOING OFFLINE]")
        else:
            print("Error - Port number must be between 1024-65535")
            quit()
    except ValueError:
        print("Error - int expected for server port")
else:
    print("Error - Unknown mode")


# END OF MAIN
