# Guida all'Esecuzione del Gioco con Maven

## Prerequisiti

- **Java 21** o versione successiva installata sul tuo sistema.
- **Apache Maven 3.9.6** o successivo installato e configurato nel PATH.

Assicurati di poter eseguire correttamente i seguenti comandi prima di procedere:
```bash
java -version
mvn -version
```

Se questi comandi mostrano le versioni corrette, sei pronto ad eseguire il gioco.

## Clonare il Repository

Clona il repository utilizzando [git](https://git-scm.com/):
```bash
git clone https://link-al-tuo-repository.git
```

Dopo il clone, entra nella directory del progetto:
```bash
cd risiko-univaq
```

## Costruzione del Progetto

Prima di eseguire il gioco, è consigliabile scaricare le dipendenze e compilare il progetto:

```bash
mvn clean install
```

Questo comando:
-   Pulisce le vecchie build (`clean`)
-   Compila e testa il progetto, scaricando le dipendenze necessarie (`install`)

## Eseguire il Gioco

Per avviare il gioco direttamente da Maven, utilizza il `maven-exec-plugin`:
```bash
mvn exec:java -Dexec.mainClass="it.univaq.disim.lpo.risiko.core.StartGame"
```

## Come dovrebbe essere visualizzato il gioco?

**N.B.** Nel video è presente l'obiettivo *"Conquistare 1 territorio"*, che è stato usato **esclusivamente** ai fini del video (nel gioco reale ovviamente non è presente).

![Simulazione di una partita](documentation/VideoTutorial_RisikoUnivAQ.gif)

## Risoluzione dei Problemi

-   **Dipendenze non trovate:** Esegui `mvn clean install` per assicurarti che tutte le dipendenze siano scaricate.
-   **Versioni di Java non compatibili:** Controlla la versione di Java installata e assicurati che corrisponda a quella richiesta.