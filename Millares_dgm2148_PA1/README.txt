-------------------------------------------------------------
Programming Project 1 - Simple Chat Application
Name: David Gabriel Millares Bellido
UNI: DGM2148
-------------------------------------------------------------

Command Line Instructions for compiling and running program:
- PROVIDING MAKEFILE FOR CONVENIENCE IN TESTING
FOR SERVER: 
- python3 ChatApp.py -s <port-number>
- make (with a default port 2626)
FOR CLIENT:
- python3 ChatApp.py -c <nick-name> <Server IP> <Server Port> <user port>
- make <cli1/cli2/cli3> (more on this afterwards)
____________________________________________________________________________
- once logged in, type help in order to see all the functions that the chat
app offers

- I utilized a series of codes written down below with either [server] or
  [client] which shows who sent the code. Each three digit code refers to a
  specific action or acknowledgement on a process. There is some overlap
  among codes, but I split them into each category they fall into at the end
  of the read me
------------------------------------------------------------------------------
Makefile turned in that I used to test files, 4 targets
make: starts server
make cli1: starts one client with name Tom
make cli2: starts one client with name Joe
make cli3: starts one client with name Bob
------------------------------------------------------------------------
2.1 Registration: FUNCTIONAL
- Client exchanges a series of handshakes and data with server before
  entering the stable state
- Status displayed correctly if program runs correctly
- Client updates table properly through threading
- Silent leave IMPLEMENT
- Notified leave works properly
Server:
- Holds list of all clients
- Adds user data to table
- Server broadcasts table every update it makes
----------------------------------------------------------------------
2.2 Chatting: FUNCTIONAL
- Clients communicate between each other with local tables
- sending messages are just like we were instructed to
- Both clients exchange ACKs between each other with chat (ACK codes
are shown below)
- If ack doesn't arrive, then the message is sent to server
- dereg request works properly
---------------------------------------------------------------------
2.3  De-registration: FUNCTIONAL
- Server changes status to offline properly
- boradcasts new table to other clients properly
- sends an ACK to client that requested the de-reg
- - client tries 5 times before exiting application
- all other clients receive table
- displays message properly
------------------------------------------------------------------------
2.4 Offline chat: FUNCTIONAL
- Client sends offline chat in both cases
- Messages stored without ">>>" but are printed later with it
- Example case works fine
- Spacing between every new message is the only difference in output, but
  messages displayed just as expected
-------------------------------------------------------------------------
2.5 Group chat: FUNCTIONAL
-Clients are always added to predefined channel (all)
- accesed via send_all
- Messages stored adequately by server
- If server doesnt respond, message adequately displayed
- receiving messages from channel displays properly
- sends ack to sender client
- Server first pings if clients online, then
------------------------------------------------------------------------
Data structures:
-----------------------------------------------------------------------
- Regular list
- Table divided sections by "%" and stored data as strings
-------------------------------------------------------------------------
Extra functionalities:
- re-registration with same data is supported
DISCLAIMER: Same data means same port, ip, and name, if any of these
differ then it is not considered same data
- Server has descriptive outputs of every single action it is requested and
  the status of what happens with it
- Server exit realized by Ctrl+C on the server instance
- Pressing Ctrl+C on client side will display Silent Leave message and exit
  program completely
- If a client tries to log in twice, the "server" will ignore the request
  and a message saying "server appears to be offline" will appear (client
  can only log in once in a window)
-----------------------------------------------------------------------
Test cases (PROVIDED ON SEPARATE FILE)
-----------------------------------------------------------------------
Test case 1: OK
Test case 2: OK
- Implemented Exit when offline chat request fails due to what test 2 says
Test case 3:
-----------------------------------------------------------------------
KNOWN BUGS: 
- Time on messages stored is off by a few hours (different timezone), but is correct besides
  that. 
- Exit time when leaving through Ctrl+C takes some time, but works
--------------------------------------------------------
My protocol codes and their meanings (ACKS)
--------------------------------------------------------
New Registration:
--------------------------------------------------------
110: [client] Register request to server
910: [server] acknowledges client request
500: [server] users table update from server)
501: [server] signals that the whole table has been sent
911: [server] acknowledges that client was added to list
912: [server] table sent to client successfully
404: [server] new client trying to get a name, exits
913: [server] client already in list, but approved for return
--------------------------------------------------------
Chatting:
--------------------------------------------------------
200: [client] chat request from another client
201: [client] request accepted, listening for message
202: [client] message received and printed
--------------------------------------------------------
De-registration (notified leave):
--------------------------------------------------------
120: [client] de-registration request to server
920: [server] acknowledges client request
921: [server] client removed successfully
--------------------------------------------------------
Offline chat:
--------------------------------------------------------
250: [client] destination offline, send to server
950: [server] accepts offline chat request
951: [server] client confirmed offline
952: [server] message successfully stored
954: [server] client that timed out is Online
999: [server] pings client to see if alive
199: [client] pings back to acknowledge that client is alive
115: [client] register again request to server
955: [server] no offline messages
956: [server] Has offline messages
957: [server] all offline messages transmitted
---------------------------------------------------------
Group Chat:
---------------------------------------------------------
240: [client] chat_all request to server
940: [server] chat_all request accepted
241: [server] chat_all request to clients
242: [client] chat_all received properly
