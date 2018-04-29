> This is a repository for a university project at "Advanced programming methods"

# Manager de studenti

### Tags
- **Security** : 3 tipuri de utilizatori (admin, profesor, student) cu drepturi diferite in aplicatie. Adminul poate adauga studenti si utilizatori, cat si reseta parole. Profesorul poate adauga teme, poate da note si poate genera rapoarte despre notele studentilor, iar studentii pot vedea detalii despre propriile note. Toti utilizatori isi pot modifica parola asociata contului. Utilizatorii sunt stocati prin username si parola prin hash cu salt.

    **Utilizatori default** : 
    - admin : admin
    - profesor : profesor
    - student : student
 
- **Database** : Am folosit Apache Derby, baza de date fiind embeded / locala in aplicatie.

### Extra Tags
- **Rapoarte** : Pentru generarea de rapoarte in pdf am folosit iText 7 pentru a converti cod din html (mai usor de facut designul raportului) in pdf.

- **Stilizare** : Pentru stilizarea aplicatiei am incercat sa respect modelul de material design, folosind libraria JFoenix

### Fonturi necesare
- **Bebas Neue** : http://www.fontfabric.com/bebas-neue
- **Open Sans** : https://fonts.google.com/specimen/Open+Sans
- **Open Sans Condensed** : https://fonts.google.com/specimen/Open+Sans+Condensed
- **Roboto** : https://fonts.google.com/specimen/Roboto
- **Roboto Condensed** : https://fonts.google.com/specimen/Roboto+Condensed

### Librarii folosite
- **Apache Derby** : https://db.apache.org/derby
- **Font Awesome FX** : https://bitbucket.org/Jerady/fontawesomefx
- **iText 7** : https://itextpdf.com/itext7/pdfHTML
- **jFoenix** : http://jfoenix.com/

### Imagini din aplicatie

![login](https://user-images.githubusercontent.com/35651901/36538958-dc2d7b50-17dd-11e8-90c1-049a3b4a1601.gif)

![student](https://user-images.githubusercontent.com/35651901/36551387-22a2ff2a-1800-11e8-9b32-b7412cd7053c.gif)

![admin](https://user-images.githubusercontent.com/35651901/36551865-435a1388-1801-11e8-9199-4061cc9304d8.gif)

![profesor](https://user-images.githubusercontent.com/35651901/36553810-fc325f88-1805-11e8-9bda-d455bcc899f3.gif)

```sh
Nesa Rares - gr. 225
```