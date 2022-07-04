## Hibernate Product Store - Demo Project
### Opis projektu
Ten projekt został stworzony w celu zaimplementowania połączenia z bazą danych i użycia Hibernate (ORM - Object Relational Mapper). W ramach projektu został stworzono modele 'Student' i 'Oceny'.
Między modelami zachądzą różne interakcje, jak np dodawanie Studneta, przydzielanie mu oceny z podziałem na przemiot, z możliwością wyciągnięcia oceny średniej, itp.
Obsługa aplikacji odbywa się przez parser konsolowy.

### Niezbędne zależności
Do uruchomienia projektu niezbędne jest posiadanie:

(lokalnej lub zdalnej) bazy danych MySQL,
Java JRE (Java Runtime Environment) (min. Java 11)
### Jak uruchomić
Do uruchomienia projektu konieczne jest skonfigurowanie połączenia bazodanowego. Wzór pliku konfiguracyjnego template_hibernate.cfg.xml znajduje się w katalogu src/main/resources/. Plik należy skopiować i zmienić treść następujących pól:

        <property name="connection.url"></property> <!--TODO: zmienić adres połączenia-->
        <property name="connection.username"/> <!--TODO: wypełnić nazwę użytkownika-->
        <property name="connection.password"/> <!--TODO: wypełnić hasło-->
Po zmianie ustawień należy zmienić nazwę pliku z template_hibernate.cfg.xml na hibernate.cfg.xml.

### Autorzy
Ja (2022) (R)