# Curriculum Vitae &mdash; szablon napisany w HTML i CSS

<p align="center">
  <img height="850" width="600" src="https://raw.githubusercontent.com/WojtekWernicki/curriculum-vitae/master/cv.png">
</p>

Prosty szablon CV stworzony z myślą o programistach. Zawiera sekcje o doświadczeniu zawodowym, edukacji, wykonanych projektach z klikalnymi linkami do repozytorium, znanych językach programowania, narzędziach i językach obcych. Zobacz przykładowe CV [tutaj](example-cv.pdf).

## Instrukcja obsługi

Jeżeli nie zamierzasz niczego zmieniać w stylach CV (np. koloru), to pobierz/sklonuj repozytorium i przejdź od razu do punktu 6.

0. Zainstaluj Node.js (https://nodejs.org/en/), gulp i gulp-cli: `npm install -g gulp gulp-cli`
1. Pobierz/sklonuj repozytorium
2. W konsoli przejdź do folderu z szablonem i uruchom instalację pakietów npm: `npm install`
3. Wklej zdjęcie, którego użyjesz w swoim CV, do folderu `src/img`
4. Uruchom gulpa: `gulp`
5. Jeżeli chcesz zmienić kolor, możesz to zrobić w pliku [src/scss/style.scss](src/scss/style.scss)
6. Dokonaj niezbędnych zmian w HTML-u. 
7. Używając Google Chrome, otwórz CV i wydrukuj je do PDF-a
   1. Wybierz drukarkę "Zapisz jako PDF"
   2. Ustaw marginesy na "Brak"
   3. Zaznacz opcję "Obraz w tle"
8. Gratulacje, masz swoje CV!

## Inne

W stopce umieszczona jest już prawna formułka, dzięki której pracodawca może przetwarzać Twoje dane osobowe umieszczone w CV.

Do wykonania tego szablonu użyłem stworzonego przez siebie **startera [gulp-starter](https://github.com/WojtekWernicki/gulp-starter)**. Za pomysł dziękuję Andrzejowi Prusinowskiemu ([avris.it](https://avris.it)) i recenzentom mojej pracy: Natalii i Michałowi.