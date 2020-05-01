import signal

signal.signal(signal.SIGINT, signal.SIG_DFL)
import socket
import struct
import threading

PORT = 1234
IP = "localhost"
HEADER_LENGTH = 2


def receive_fixed_length_msg(sock, msglen):
    message = b''
    while len(message) < msglen:
        chunk = sock.recv(msglen - len(message))  # preberi nekaj bajtov
        if chunk == b'':
            raise RuntimeError("socket connection broken")
        message = message + chunk  # pripni prebrane bajte sporocilu

    return message


def receive_message(sock):
    header = receive_fixed_length_msg(sock,
                                      HEADER_LENGTH)  # preberi glavo sporocila (v prvih 2 bytih je dolzina sporocila)
    message_length = struct.unpack("!H", header)[0]  # pretvori dolzino sporocila v int
    message = None
    if message_length > 0:  # ce je vse OK
        message = receive_fixed_length_msg(sock, message_length)  # preberi sporocilo
        message = message.decode("utf-8")

    return message


def send_message(sock, message):
    encoded_message = message.encode("utf-8")  # pretvori sporocilo v niz bajtov, uporabi UTF-8 kodno tabelo

    # ustvari glavo v prvih 2 bytih je dolzina sporocila (HEADER_LENGTH)
    # metoda pack "!H" : !=network byte order, H=unsigned short
    header = struct.pack("!H", len(encoded_message))

    message = header + encoded_message  # najprj posljemo dolzino sporocilo, slee nato sporocilo samo
    sock.sendall(message);
    print("sending", message)


def handle_msg (sock, message):
    if message[0:3] == "RNM": #rename
        new_name = message[3:]
        for name in clients.keys():
            if name == new_name:  # client with that name exists
                print("ime obstaja error")
                msg ="RNR0"
                send_message(sock,msg)
                return
        for key in clients.keys():
            if clients[key] == sock:  #yeyy this is our clinet yey
                clients[new_name] = clients.pop(key)
                msg ="RNR"+new_name
                send_message(sock,msg)
                break
    
    if message[0:3] == "BCT":  #broadcast
         for val in clients.values():
             if(val != sock):
                name_len= int(message[3:5])
                name = message[5:5+name_len]
                send_message(val, "MSG"+message[3:])

    if message[0:3] == "PRV":  #PRV05Matejkako si kej
        print("_-----")
        name_len= int(message[3:5])
        name = message[5:5+name_len]
        if(name in clients.keys()):
            send_message(clients[name], "MSG"+message[5+name_len:])
        else:
            send_message(sock, "ERR"+"0")
            print(clients.keys())
        

    print(message)




# funkcija za komunikacijo z odjemalcem (tece v loceni niti za vsakega odjemalca)
def client_thread(client_sock, client_addr):
    global clients

    print("[system] connected with " + client_addr[0] + ":" + str(client_addr[1]))
    print("[system] we now have " + str(len(clients)) + " clients")

    try:

        while True:  # neskoncna zanka
            msg_received = receive_message(client_sock)

            if not msg_received:  # ce obstaja sporocilo
                break

            handle_msg(client_sock, msg_received)

            #print("[RKchat] [" + client_addr[0] + ":" + str(client_addr[1]) + "] : " + msg_received)

            # for client in clients:
            #     send_message(client, msg_received.upper())
    except:
        # tule bi lahko bolj elegantno reagirali, npr. na posamezne izjeme. Trenutno kar pozremo izjemo
        pass

    # prisli smo iz neskoncne zanke
    with clients_lock:
        for i in clients.keys():
            if clients[i] == client_sock:
                del clients[i]
                break

    print("[system] we now have " + str(len(clients)) + " clients")
    client_sock.close()


# kreiraj socket
server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server_socket.bind((IP, PORT))
server_socket.listen(1)

# cakaj na nove odjemalce
print("[system] listening ...")
clients = {}
clients_lock = threading.Lock()
while True:
    try:
        # pocakaj na novo povezavo - blokirajoc klic
        client_sock, client_addr = server_socket.accept()
        with clients_lock:
            clients[client_addr] = client_sock
            #clients.add(client_sock)

        thread = threading.Thread(target=client_thread, args=(client_sock, client_addr));
        thread.daemon = True
        thread.start()

    except KeyboardInterrupt:
        break

print("[system] closing server socket ...")
server_socket.close()
