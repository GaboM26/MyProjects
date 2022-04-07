
#CLIENT CLASS 
import socket
from socket import *
import time
import threading
import time
from datetime import datetime
import sys

class client:
    import socket
    def __init__(this, name, sip, sport, cPort):
        #USEFUL METADATA
        import socket
        this.run = True
        this.users = [] #received from server
        this.gotProc = 0 #used to communicate thread and main
        this.name = name
        this.cPort = int(cPort)
        this.sData = (sip, int(sport))
        this.ip = socket.gethostbyname(socket.gethostname())
        this.cs = None #For Client Socket
        try:
            this.regReq()
        except:
            print("Server might be offline...")
            print("Exiting...")
            sys.exit()

    def regReq(this):
        import socket
        this.cs = socket.socket(AF_INET, SOCK_DGRAM)
        try:
            this.cs.bind((this.ip, this.cPort))
        except OSError:
            sys.exit("Error: You are already logged in!!")
        this.cs.settimeout(1)
        message = "110"
        this.cs.sendto(message.encode(), this.sData)
        sm, sAddress = this.cs.recvfrom(2048)
        if(isAck(sm, 910)): #Server waiting for my info
            for i in range(3):
                this.cs.sendto(str(this.myData(i)).encode(), this.sData)
            sm, sAddress = this.cs.recvfrom(2048)
            if(isAck(sm, 911)):
                print(">>>Client Registered Succesfully", end='\n>>>')
                sm, sAddress = this.cs.recvfrom(2048)
                if(isAck(sm, 500)):
                    this.getTable()
                else:
                    print("couldnt retrieve table, try again")
                    quit()
            elif(isAck(sm, 913)):
                notReg = True
                print(">>>[Returning client: input reg <nick-name> to register again]")
                while notReg:
                    x = input(">>>")
                    a = x.split()
                    if(len(a) != 2):
                        print("Usage is reg <nick-name>")
                    elif(a[0] == "reg" ):
                        if(a[1] == this.name):
                            this.cs.sendto("115".encode(), this.sData)
                            this.run = True
                            print("", end='\n>>>')
                            try:
                                this.getTable()
                                sm, a = this.cs.recvfrom(2048)
                            except:
                                print(">>>[Server Not Responding]")
                                sys.exit()
                            if(isAck(sm, 956)):
                                this.rcvOM()
                            break
                        else:
                            print(">>>Error: You must use your own nick-name")
                    else: 
                        print(">>>Error: No action allowed before reg <nick-name>")
            elif(isAck(sm, 404)):
                print("Error: username already in use")
                quit()
            else:
                print("Error: exiting")
                sys.exit()
        else:
            print(">>>Retrying registration")
            this.regReq()

    def myData(this, i):
        if(i==0):
            return this.name
        if(i==1):
            return this.ip
        if(i==2):
            return this.cPort
#############################STABLE STATE METHODS######################
    def runClient(this): 
        print("[Welcome, You Are Registered]")
        t1 = threading.Thread(target = this.servListen, args = ())
        t1.start()
        while True:
            x = input(">>>  ")
            a = x.split(" ", 2)
            if(a[0] == "send"):
                this.send(a)
            elif(a[0] == "dereg"):
                if(this.isValidDereg(a)):
                    this.run = False
                    this.dereg(a)
                    t1.join()
                    this.isDereg()
            elif(a[0] == "help"):
                hlp()
            elif(a[0] == "users"):
                this.printUsers()
            elif(a[0] == "send_all"):
                msg = a[1]
                if(len(a) == 3):
                    msg += " " + a[2]
                this.sendAll(msg)
            elif(a[0] != ""):
                print("Unknown command - type help to see valid commands")
        return ;

    def send(this, a):
                if(len(a) == 3):
                    if(a[1] != this.name):
                        dest = this.getAdd(a[1])
                        if(dest != (None, None)):
                            this.sendMsg( dest, a[1], a[2])
                        else:
                            print("Error: not a valid user to send to")
                    else:
                        print(">>>[Message received by", this.name,".]")
                        print(">>>",this.name, ":", a[2])
                else:
                    print("input should be: send <name> <message>, type [help] for more information.")
    def dereg(this, a):
        i = 0
        while(i < 5):
            this.cs.sendto("120".encode(), this.sData) #dereg request to server
            time.sleep(0.5)
            if(this.gotProc == 920 or this.gotProc ==921):
                break
            i += 1
        if(i==5):
            print(">>>[Server not responding]", end='\n>>>')
            print("[Exiting]")
            this.run = False
            sys.exit()
        else:
            print(">>>[You are Offline. Bye.]")
    def getTable(this):
        temp = []
        hasMore = True
        while hasMore:
            u, a = this.cs.recvfrom(2048)
            if(isAck(u, 912)):
                print("[Table Updated]", end='\n>>>')
                break
            temp.append(u.decode())
        this.users = temp

    def printUsers(this):
        for usr in this.users:
            u = usr.split("%")
            print(u[0], "is", u[3])

    def servListen(this): #thread runs this
        while(this.run):
            while True:
                try: 
                    m, a = this.cs.recvfrom(2048) #Will loop and make occasional checks
                    this.gotProc = int(m.decode())
                except:
                    if(this.run):#If this is false, then loop breaks and thread dies
                        continue
                    else:
                        this.gotProc = 0
                break
            if(this.gotProc == 500): #Table updated
                this.getTable()
                this.gotProc = 0
            elif(this.gotProc == 200): #Chat request
                this.msgFrom(a)
                this.gotProc = 0
            elif(this.gotProc == 999):
                this.pingBack()
            elif(this.gotProc == 954):
                print("[Client exists!!]") #Add Nickname
                this.getTable()
            elif(this.gotProc == 951):
                print("[Messages received by the server and saved]", end='\n>>>')
            elif(this.gotProc == 241):
                this.rcvAll()
            #Ends thread
            if(this.gotProc == 921):
                break
    
    def msgFrom(this, address): #takes address and starts communicating w/ other use
        nn = this.getName(address) #gets Nickname from IP address and port #
        if(nn == None):
            return (None, None) #if not on list, nothing happens
        this.sendAck("201", address)
        m, a = this.cs.recvfrom(2048)
        print(nn, ":", m.decode(), end='\n>>>') #SHOULD ADD TIMESTAMP
        this.sendAck("202", address)

    def sendAck(this, process, clientAddress):
        this.cs.sendto(process.encode(), clientAddress)
        return ;

    def getAdd(this, name):#Returns address of name or Null if not valid username
        for usr in this.users:
            u = usr.split("%")
            if(name == u[0]):
                return (u[1], int(u[2]))
        return (None, None)

    def sendMsg(this, dest, nn, msg): #returns True if everything ok
        if(this.isOffline(nn)): #check if table says that user is offline
            return this.servSend(msg, dest, nn)
        this.cs.sendto("200".encode(), dest)
        time.sleep(0.5)
        if(this.gotProc == 201):
            this.cs.sendto(msg.encode(), dest)
        else:
            print(">>>[No ACK from", nn, ", message sent to server.]")
            this.servSend( msg, dest, nn)
            return 
        time.sleep(0.05)
        if(this.gotProc == 202):
            print(">>>[Message received by", nn, ".]")
            return True
        return False

    def makeSSPacket(this, msg, dest, nn):

        time = datetime.now().strftime("%d/%m/%Y %H:%M:%S")	
        return "250%"+nn+"%"+this.name + ": <" + time + "> " + msg

    def servSend(this, msg, dest, nn): #sends to server for offline chat
        pack = this.makeSSPacket(msg, dest, nn)
        i=0
        while(i<5):
            this.cs.sendto(pack.encode(), this.sData)
            time.sleep(0.5)
            if(this.gotProc == 950 or this.gotProc == 951 or this.gotProc == 954):
                return
            i+=1
        print(">>>Server may be offline")
        print(">>>Couldn't deliver message")
        this.cs.settimeout(0.0001)
        this.run = False
        sys.exit() #EXIT BECAUSE OF TEST 2

    def getName(this, address): #address is tuple
        for usr in this.users:
            u = usr.split("%")
            test = (u[1], int(u[2]))
            if(address == test):
                return u[0]
        return None

    def isValidDereg(this, a):
        if(len(a) != 2):
            print("Usage: >>> dereg <nick-name>")
            return False
        if(this.name != a[1]):
            print("Error: you can only de-register yourself,", this.name)
            return False
        return True

    def isOffline(this, name):
        for usr in this.users:
            u = usr.split("%")
            if(u[0] == name and u[3] == "Offline"):
                return True
        return False
    
    def pingBack(this):
        this.cs.sendto("199".encode(), this.sData)
        return 

    def isDereg(this):
        notReg = True
        while notReg:
            x = input(">>>")
            a = x.split()
            if(len(a) != 2):
                print("Must use reg <nick-name>")
            elif(a[0] == "reg" ):
                if(a[1] == this.name):
                    this.cs.sendto("115".encode(), this.sData)
                    this.run = True
                    print("", end='\n>>>')
                    try:
                        this.getTable()
                        sm, a = this.cs.recvfrom(2048)
                    except:
                        print(">>>[Server Not Responding]")
                        sys.exit()
                    if(isAck(sm, 956)):
                        this.rcvOM()
                    break
                else:
                    print(">>>Error: You must use your own nick-name")
            else: 
                print(">>>Error: No action allowed before reg <nick-name>")
        this.runClient()
    def rcvOM(this):
        hasMsg = True
        print("[You have messages]")
        while hasMsg:
            m, a = this.cs.recvfrom(2048)
            if(isAck(m, 957)):
                break
            print( m.decode())
        return

    def sendAll(this, msg):
        pckt = "240%"+ this.name+ "% "+ msg
        i = 0
        while(i < 5):
            this.cs.sendto(pckt.encode(), this.sData)
            time.sleep(0.5)
            if(this.gotProc == 940):
                break
            i += 1
        if(i==5):
            print(">>>[Server not responding.]")
            return 
        print(">>>[Message received by Server.]")
        return

    def rcvAll(this):
        m, a = this.cs.recvfrom(2048)
        print(m.decode(), end='\n>>>')
        this.cs.sendto("242".encode(), this.sData)
        return


def isAck(m, proc): #takes a message and procedure its supposed to ack and returns a boolean
    try:
        ack = m.decode()
        return int(ack) == proc
    except:
        return False


def hlp():
    print(".- send <name> <message> : sends <message> to <name>")
    print(".- send_all <message> : sends <message> to all clients")
    print(".- dereg <name> : deregisters you from list and exits")
    print(".- users : shows all online users")
