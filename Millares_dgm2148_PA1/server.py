
#SERVER CLASS 
from socket import *
import time
import os
from datetime import datetime

class server:
    def __init__(this, thePort):
        this.clients = []
        this.ip = 0
        this.port = thePort
        this.sock = None 

    def activateServer(this):
        import socket
        print(">>> Initializing Server...")
        this.sock = socket.socket(AF_INET,SOCK_DGRAM)
        this.sock.bind((socket.gethostname(), this.port))
        this.ip = socket.gethostbyname(socket.gethostname())
        print("Server Online - IP: ", this.ip, " - Port Number: ", this.port)
        this._runServer()
        quit()
##############################Stable State########################################
    def _runServer(this):
        #try:
        print("The server is ready to receive")
        while True:
            m, clientAddress = this.sock.recvfrom(2048) #Listen for packets
            message = m.decode()
            this._evaluateMessage(message, clientAddress)#Number specifies process
        print("server offline")
        return ;
        #except KeyboardInterrupt:
         #   print("\nServer Interrupted - Bye")



    def _evaluateMessage(this, message, clientAddress):
        try:
            msg = message.split("%")
            m = int(msg[0])
            if(m == 110): #reg request
                print("registration request from:", clientAddress[0])
                this.sendAck("910", clientAddress)
                newCli = this.registerClient(clientAddress)
                if(newCli):
                    this.tableUpdate(None)
                    this.sendTable(None)
                    print("SUCCESS")
            elif(m == 120): #dereg request
                print("deregistration request from:", clientAddress[0])
                this.sendAck("920", clientAddress)
                this.offlineUser(clientAddress)
                this.tableUpdate(clientAddress)
                this.sendTable(clientAddress)
                print("SUCCESS")
            elif(m==250):
                print("Offline chat request from", clientAddress[0])
                this.sendAck("950", clientAddress)
                if(this.checkOffline(this.getAdd(msg[1]))): #If user is actually offline
                    this.sendAck("951", clientAddress)
                    this.saveMsg(msg[1], msg[2])
                    offlineAdd = this.getAdd(msg[1]) # offline users address
                    time.sleep(0.05)
                    wasOffline = this.offlineUser(offlineAdd)
                    if(not(wasOffline)):
                        this.tableUpdate(offlineAdd)
                        this.sendTable(offlineAdd)
                    print("SUCCESS")
                else:
                    print("FAILED")
                    this.sendAck("954", clientAddress)
                    time.sleep(0.005)
                    this.sendOneTable(clientAddress) #sends table to single Client
            elif(m==115):
                print("Reregistration request from", clientAddress[0])
                time.sleep(0.05)
                this.onlineUser(clientAddress)
                this.sendOneTable(clientAddress)
                if(this.hasOM(clientAddress)):
                    this.sendOM(clientAddress)
                else:
                    this.sendAck("955", clientAddress)
                this.tableUpdate(clientAddress) #Client automatically will enter receive table mode
                this.sendTable(clientAddress)
                print("SUCCESS")
            elif(m==240):
                print("Chat All request from", clientAddress[0])
                this.sendAck("940", clientAddress)
                this.sendAll(msg[2], msg[1])


        except ValueError: #Unknown request or not an int (out of order)
            print("Request from ", clientAddress, " invalid")
            return ;

    def registerClient(this, clientAddress):
        u = ""
        sameData= [False, False, False] # [Name, IP, port]
        for i in range(3):
           m, a =  this.sock.recvfrom(2048)
           data = m.decode()
           for cli in this.clients:
               c= cli.split("%")
               sameData[i] = c[i]==data or sameData[i]
           u += data + "%"
        if(sameData[0] and sameData[1] and sameData[2]):
            this.sendAck("913", clientAddress)
            print("FAILURE: MUST SEND REREG REQUEST")
        elif(sameData[0]):
            this.sendAck("404", clientAddress)
            print("FAILURE: ALREADY TAKEN USERNAME")
        else:
            u += "Online"
            this.sendAck("911", clientAddress)
            this.clients.append(u)
            return True
        return False

        #SHOULD ADD A TRUE FOR SUCCESS OR FALSE FOR NOSUCCESS

    def sendTable(this, exclude):
        #First sends how many users client should expect then sends 912 to acknowledge end
        ads = [] #addresses
        status = [] #offline or online for each
        for cli in this.clients:
            temp = cli.split("%")
            ads.append((temp[1], int(temp[2])))
            status.append(temp[3])
        i = 0
        for ad in ads:
            if(ad != exclude and status[i] == "Online"):
                this.sendOneTable(ad)
            i += 1


    def sendAck(this, process, clientAddress):
        this.sock.sendto(process.encode(), clientAddress)
        return ;

    def tableUpdate(this, exclude):
        ads = []
        for cli in this.clients:
            temp = cli.split("%")
            theAd = (temp[1], int(temp[2]))
            if(exclude != theAd and temp[3] == "Online"):
                this.sendAck("500", theAd)
        return ;

    def offlineUser(this, usr):
        ads = [] #addresses
        i = 0
        for cli in this.clients:
            temp = cli.split("%")
            theAd = (temp[1], int(temp[2]))
            if(usr == theAd and temp[3] != "Offline"):
                this.clients[i] = temp[0] + "%" +  temp[1] +"%"+ temp[2] + "%Offline"
                return False
            i+=1
        return True

    def getAdd(this, name):#Returns address of name or Null if not valid username
        for cli in this.clients:
            u = cli.split("%")
            if(name == u[0]):
                return (u[1], int(u[2]))
        return (None, None)

    def saveMsg(this, nn, msg):
        theFile = open(nn + "_msgs.txt", "a")
        theFile.write(">>>"+  msg + "\n")
        theFile.close()
        return 


    def sendOneTable(this, clientAddress):
        for cli in this.clients:
            this.sock.sendto(cli.encode(), clientAddress)
        this.sendAck("912", clientAddress)
        return

    def checkOffline(this, add): #Checks if user is offline via ping
        try:
            if(this.isOffline(add)):
                return True
            this.sock.settimeout(0.5)
            this.sock.sendto("999".encode(), add)
            m,a = this.sock.recvfrom(2048)
            this.sock.settimeout(None)
            if(int(m.decode()) == 199):
                return False
            return False
        except:
            this.sock.settimeout(None)
            return True

    def onlineUser(this, add):
        ads = [] #addresses
        i = 0
        for cli in this.clients:
            temp = cli.split("%")
            theAd = (temp[1], int(temp[2]))
            if(add == theAd): #Compares addresses and if found breaks loop
                this.clients[i] = temp[0] + "%" +  temp[1] +"%"+ temp[2] + "%Online"
                break
            i+=1
    def isOffline(this, add):
        for cli in this.clients:
            temp = cli.split("%")
            theAd = (temp[1], int(temp[2]))
            if(add == theAd):
                return temp[3] == "Offline"

    def hasOM(this, add):
        name = this.getName(add)
        fileName = name + "_msgs.txt"
        return os.path.exists(fileName)

    def sendOM(this, add):
        this.sendAck("956", add)
        fileName = this.getName(add) + "_msgs.txt"
        theFile = open(fileName, "r")
        msgs = theFile.readlines()
        for msg in msgs:
            this.sock.sendto(msg.encode(), add)
        this.sendAck("957", add)
        theFile.close()
        os.remove(fileName)
        return

    def getName(this, address): #address is tuple
        for usr in this.clients:
            u = usr.split("%")
            test = (u[1], int(u[2]))
            if(address == test):
                return u[0]
        return None

    def sendAll(this, msg, source):
        for cli in this.clients:
            temp = cli.split("%")
            theAd = (temp[1], int(temp[2]))
            if(temp[0] != source):
                if(temp[3] == "Online"):
                    this.sendOne(this.makeM(msg, source), theAd)
                    this.sock.settimeout(0.5)
                    try:
                        m,a = this.recvfrom(2048)
                    except:
                        if(this.checkOffline(theAd)):
                            this.saveMsg(temp[0],this.makeOM(msg, source))
                            this.offlineUser(theAd)
                            this.tableUpdate(theAd)
                            this.sendTable(theAd)
                        else:
                            continue
                else:
                    this.saveMsg(temp[0], this.makeOM(msg, source))
    
    def sendOne(this, msg, add):
        this.sendAck("241", add)
        time.sleep(0.05)
        this.sock.sendto(msg.encode(), add)
        return 
    def makeOM(this, msg, source):
        time = datetime.now().strftime("%d/%m/%Y %H:%M:%S")
        return ">>>Channel_Message " + source + ": <" + time + "> " + msg
    def makeM(this, msg, source):
        return "Channel_Message " + source + ":" + msg

