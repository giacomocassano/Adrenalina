# GUIDA ALL'UTILIZZO DEI FILE JAR. [ITA]
Per entrambi i Jar, client e server, è richiesto l'utilizzo di Java12.

### SERVER
Avvio Normale
L'avvio normale del jar del server si esegue scrivendo sulla console il seguente comando:

java -jar [server jar path]
	
#### Avvio con modalità persistenza.
In aggiunta al comando di sopra:
Il server deve conoscere il percorso del file di salvataggio e del file di caricamento della vecchia partita:

| Comando | Descrizione | Sintassi |
|:-----------------------|:------------------|:------------------:|
| load | Selezionare il path del file da caricare | -load | 
| save | Selezionare il path del file dove salvare| -save | 

Esempi 

-save C:\Users\user\Desktop\save.json

-load C:\Users\user\Desktop\load.json

E' possible ripristinare una partita per poi non salvare il suo proseguimento, ad esempio:

java -jar server.jar -load C:\Users\user\Desktop\load.json

Allo stesso modo è possibile salvare una partita senza ripristinarne una:

java -jar server.jar -save C:\Users\user\Desktop\save.json
In questo caso il programma si occuperà di creare il file di salvataggio in quel percorso.


### CLIENT
L'avvio normale del jar del client si esegue scrivendo sulla console il seguente comando:

java --module-path [PATH JAVA FX] --add-modules=javafx.controls -jar  [CLIENT PATH]

In questo caso verrà chiesto all'utente se vuole utilizzare socket o rmi e se vuole usare la cli o la gui.

Nel caso si voglia specificare uno o più dei parametri di configurazione da linea di comando, è presentata una tabella
descrittiva e in seguito alcuni esempi:

| Comando  | Descrizione                                        | Argomento     | Default       |
|:-----------------------|:------------------|:------------------:|:-------------|
| -ui      | Seleziona l'interfaccia utente                      | [cli\gui]     | -             |
| -com    | Seleziona il protocollo di comunicazione                         | [soc\rmi]     | -             |
| -ip      | Seleziona l'ip del server                              | [server ip]   | localhost     |
| -sp      | Seleziona la porta socket                         | [port]        | 12345         |
| -rp      | seleziona la port rmi                                | [port]        | 12333         |


Esempio:
--module-path [PATH JAVA FX] --add-modules=javafx.controls -jar client.jar -ui cli -com soc -ip 127.0.0.1 -sp 12345 -rp 12333                                         


Esempio: configurazione consigliata se Client e Server sono sulla stessa macchina:

java --module-path [PATH JAVA FX] --add-modules=javafx.controls -jar client.jar -ui cli -com soc -ip localhost

Esempio: utilizzo della gui:

java --module-path [PATH JAVA FX] --add-modules=javafx.controls -jar client.jar -ui gui -ip localhost

