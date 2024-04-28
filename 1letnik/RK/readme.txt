


				 _     ____  _   _   _  ___  
				| |   |  _ \| \ | | / |/ _ \ 
				| |   | | | |  \| | | | | | |
				| |___| |_| | |\  | | | |_| |
				|_____|____/|_| \_| |_|\___/ 

				    Domača naloga za RK
				           SSL/TLS

				  Avtor:        Lan Vukušič
				  Vpisna št:    63190321
				  Verzija:      2.0
				  Datum oddaje: 31.5.2020





1)
	V "chatClient" in "chatServer" nastavite:

	PORT = 1234
	IP  = "localhost"
	CLIENT = "client1"

		- IP trenutno dela samo za "localhost" za kar je izdan certifikat

		- CLIENT polja lahko izbiramo med:
			- client1 → Ana
			- client2 → Bine
			- cleint3 → cliente

	klonirajte polji IP ter PORT ter zaženite server na izbranem računalniku. 
	zaženite cliente na ostalih napravah (oz isti napravi)


2)
	Server vam (clientu) bo dodelil uporabniško ime glede na "commonName" polje certifikata

	če hočete zamenjati naslovnika, to naredite z komando !{ime}
	Če hočete spet pošiljati vsem to naredite z !@


3)
	Navodila lahko vidite tudi z ukazom !help

4)
	Za lažje ocenjevanje sta narejena 2 clienta (Ana in Bine, oziroma 1. in 2. certifikat)
	zaženite:
		- chatClient.py
		- chatClient2.py

Lp




