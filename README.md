NASLOV
Mini E-commerce – Spring Boot backend (Java 21, Spring Boot 3.5.5)

KRATAK OPIS
Projekat je jednostavan REST API za mini e-commerce scenarij: proizvodi i korisnička korpa. Korišćeni su Spring Web, Spring Data JPA, Bean Validation, H2 za razvoj, springdoc-openapi za dokumentaciju, a za bezbednost je pripremljen JWT pristup (u toku). Fokus je bio na zdravoj osnovi, čistom slojevitom rasporedu i jasnim HTTP odgovorima.

STATUS PO ZADACIMA
Task 1 – REST API: završeno. Implementirani su sledeći zahtevi: listanje proizvoda sa paginacijom, sortiranjem i filtriranjem; dohvat pojedinačnog proizvoda; dodavanje stavke u korpu; prikaz sadržaja korpe; izmena količine stavke; brisanje stavke. Urađena je validacija ulaza, vraćaju se odgovarajući HTTP status kodovi, CORS je podešen za lokalni razvoj, dokumentacija je dostupna preko Swagger UI, ubačeni su početni podaci, i postoje osnovni unit testovi za servis i kontroler proizvoda.
Task 2 – Autentikacija i bezbednost: delimično. Napravljeni su entitet korisnika i uloge (USER i ADMIN), formirani su endpointi za registraciju i prijavu koji vraćaju JWT. Konfigurisan je stateless pristup sa filterom za čitanje tokena i postavljanje korisnika u bezbednosni kontekst. Korpa je modelovana kao korisnički izolovana. Molim pregled pre nego što se finalno spoji Task 2, jer je prvi put rađeno sa Spring Security i JWT i deo logike je još u doradi.
Task 3 – Docker: plan i najosnovniji pristup su opisani ispod. Kako sam ranije kačio samo najjednostavnije aplikacije na Docker, ovde bih primenio isti minimalistički pristup.

KAKO POKRENUTI LOKALNO
Preduslovi: Java 21 i Maven wrapper (dolazi uz projekat).
Koraci: u korenskom direktorijumu projekat se gradi standardnim Maven ciljevima (prvo čišćenje, zatim paketiranje), a zatim se pokreće generisani jar fajl.
Swagger UI: dostupno na klasičnoj putanji u aplikaciji koja radi na lokalnom portu, pa se preko pregledača otvara stranica sa listom ruta i dugmetom “Try it out”.
JWT konfiguracija: u konfiguraciji aplikacije postoji podešavanje za tajni ključ JWT. Potrebno je postaviti vrednost koja ima najmanje trideset dva znaka i odrediti trajanje tokena. Za razvoj je u redu koristiti statičnu vrednost; za produkciju je preporuka da se čita iz promenljive okruženja.

BRZI TEST SCENARIJI
Proizvodi: javne rute se mogu pozivati odmah nakon starta i vraćaju listu seed podataka.
Registracija i prijava: najpre se registruje korisnik sa email-om i lozinkom, zatim se poziva prijava i iz odgovora kopira vrednost tokena.
Korpa: rute za korpu zahtevaju Bearer token. Nakon unošenja tokena u zaglavlje, dodavanje stavke treba da vrati uspešan status, a prikaz korpe listu stavki sa izračunatom sumom po stavci.

ARHITEKTURA I RASPORED PAKETA
Razdvojeni su slojevi po funkcionalnim celinama: model, repozitorijum, servis i veb (kontroler), posebno za proizvode i posebno za korpu. Postoje zasebni paketi za bezbednost (opšti bezbednosni lanac, servis za tokene i filter), autentikaciju (kontroler, servis i transportni objekti), korisnike (entitet, repozitorijum i uloge) i konfiguracije (CORS i OpenAPI).
Filtriranje proizvoda oslonjeno je na Spring Data specifikacije, sa modernim početnim stanjem koje dozvoljava građenje uslova bez odlaska u “null” pattern. Pagina­cija i sortiranje koriste standardne mehanizme Spring Data, a odgovor se pakuje u jednostavan oblik pogodan za frontend.
Za korpu je lista stavki inicijalizovana defanzivno kako bi se izbeglo propuštanje null vrednosti, a operacije nad korpom rade se nad kolekcijom stavki povezanoj sa korisnikom.
CORS je podešen globalno za razvojne origina (lokalni portovi). Dokumentacija API-ja se generiše automatski i dostupna je kroz Swagger UI.

TESTOVI
Postoje unit testovi za servis proizvoda (provera uspešnog čitanja proizvoda i scenarija kada proizvod ne postoji, kao i paginacije) i “slice” test kontrolera proizvoda uz mokovani servis.
Za integraciono testiranje celog konteksta može se koristiti standardna Spring Boot anotacija za dizanje konteksta, ali zbog vremena je fokus bio na brzim i pouzdanim jedinicama.

SELF-ASSESSMENT
Ovo mi je prvi put da radim sa Spring-om i Spring Boot-om. U roku od tri dana, uz bitne ispite koje sam imao paralelno, fokusirao sam se na to da razumem osnovni tok HTTP zahteva kroz aplikaciju, pravilno odvajanje slojeva, paginaciju, sortiranje i filtriranje sa Spring Data-om, kao i CORS i OpenAPI integraciju.
Naučio sam kako se zahtevi hvataju u kontroleru, kako servis orkestrira poslovnu logiku i kako repozitorijum komunicira sa bazom. Kod bezbednosti sam napravio najveći pomak: razumeo sam kako funkcionišu stateless filteri, kako se čita token iz zaglavlja i kako se puni bezbednosni kontekst. Naišao sam na konkretne izazove sa generisanjem JWT tokena (redosled postavljanja claim-ova i postojanje subject-a), kao i sa inicijalizacijom kolekcija u entitetima kada se koristi graditelj; oba problema sam rešio tako da aplikacija radi stabilno za Task 1 i da je Task 2 u funkcionalnom stanju koje može na pregled.
Da sam imao više vremena, unapredio bih pokrivenost testovima (posebno za bezbednosni filter i rubne scenarije 403), dodao uniformno rukovanje greškama kroz globalni savetnik kontrolera, izvukao bazu u poseban kontejner i uveo migracije šeme. Takođe bih dopisao integracione testove sa kontejnerizovanom bazom.

POZNATA OGRANIČENJA I PREDLOG DALJIH KORAKA (TASK 2)
Implementacija bezbednosti je u toku. Tokeni se uspešno izdaju, filter postavlja korisnika kada token sadrži ispravan subject, a korpa je logički vezana za korisnika. Potrebno je još doraditi poruke grešaka za neautorizovane i zabranjene pristupe, dopuniti testove i potvrditi sve rubne slučajeve vlasništva nad stavkama korpe. Molim da se ova celina pregleda pre finalnog spajanja kako bi se eventualne nelogičnosti ispeglale kroz code review.

DOCKER PRISTUP (TASK 3 – PLAN I OSNOVNA POSTAVKA)
Moj pristup bi bio najosnovniji, kakav sam ranije koristio: dvostepeni proces u kojem se aplikacija prvo izgradi, a zatim pokrene u lakšem JRE okruženju. Otvorio bih port aplikacije koji se može konfigurisati, postavio promenljivu okruženja za JWT tajni ključ i pokrenuo jar.
Za razvoj i demo dovoljno je mapiranje lokalnog porta na port aplikacije. Za ozbiljniji rad dodao bih datoteku sa više usluga kako bih izdvojio bazu u poseban kontejner i definisao trajni volumen za podatke, ali u okviru ovog zadatka bih ostao pri minimalnoj postavci da bi fokus bio na jezgru API-ja.

UPUTSTVA ZA POSTAVLJANJE U DOCKER-U (OPISNO)
Prvo se izgradi aplikacija standardnim Maven postupkom. Zatim se izgradi slika aplikacije korišćenjem jednostavne definicije sa dva sloja (faza izgradnje i faza pokretanja). Aplikacija se pokreće iz slike uz mapiranje porta i prosleđivanje promenljive okruženja za tajni ključ JWT, koja mora biti dovoljno dugačka. Ako je potrebno, port i naziv slike su podesivi kroz parametre prilikom pokretanja.
Ako bi se uvodila spoljašnja baza, dodala bi se dodatna usluga za bazu i odgovarajući parametri okruženja za adresu i kredencijale baze, ali to nije nužno za osnovni prikaz funkcionalnosti.

IZVINJENJE
Izvinjavam se na kašnjenju sa README datotekom. U prethodna tri dana imao sam važne ispite, pa sam deo vremena uložio u učenje Spring-a od nule i postavljanje čvrstog temelja. Zadovoljan sam koliko sam uspeo da savladam i implementiram za kratko vreme i mislim da je to vidljivo kroz kompletiran Task 1, kao i funkcionalnu osnovu Task-a 2. Molim da se bezbednosni deo pogleda pre spajanja, rado ću dopuniti sve što bude potrebno nakon komentara.
