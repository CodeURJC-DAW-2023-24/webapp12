
## Table of contents
- Phase 0
  - App name
  - Team members
  - Tools used
  - Entities
  - Permissions
  - Images
  - Grafics
  - Complementary technologies
  - Advanced query
- Phase 1
  - Pages
  - Diagram

# Phase 0: Team creation and definition of website functionalities
### App name: YourHOmeTEL

### Team members 
| Name | Email | GitHub user |
|:------------------------------:|:---------------------------------:|:--------------------:|
| Mario Recio Montero            | m.recio.2020@alumnos.urjc.es      | Mario-Recio          |
| Sara Tuset Villoria            | s.tuset.2020@alumnos.urjc.es      | SaraTuset            |
| Paula Monrobel Ugidos          | p.monrobel.2019@alumnos.urjc.es   | Akram1013            |
| Laila El Khattabi El Hassnaoui | l.elkhattabi.2020@alumnos.urjc.es | lailaelkhattabielhas |
| Carlos Herman Andrés Andrés    | ch.andres.2020@alumos.urjc.es     | Carlos-Herman        |


### Tools used (trello) 

Link: https://trello.com/invite/g12daw/ATTI831eec52f3cf9323d36dd12dcf07b91c5373DA36 



### Entities

- User 

- Hotel 

- Reservation 

- Review


Relationship: A user can make reservations in a hotel or manage a hotel, and create, consult or modify reviews about it. 

![Captura de pantalla 2024-02-05 112946](https://github.com/CodeURJC-DAW-2023-24/webapp12/assets/80918031/86aa9dd5-b248-49a1-9272-c62fb666de4a)

### Permissions

Anonymous User: Can view hotel pages and reviews. 

Registered User: 

- **Customer**: can book rooms, write reviews and view their personal information.   

- **Hotel Manager**: can modify the information of the hotels he/she manages, and view different statistics and metrics of interest.   

- **Administrator**: can change hotel information and delete reviews.  He/she can also manage information related to the users of the site, as well as manage, verify and validate user events. 

All users will also have the option to log in or register an account. To create a hotel manager account, you must have approval from the administration. 
 

### Images 

Hotel: Picture of the hotel and/or rooms. 

Rating: It is possible for the customer to put a photo in their rating. 

Registered user: A profile picture for your avatar. 

 

### Graphics

**For Hotel Managers** 

- Rating scores over time (dotted graph connected with lines) 

- Number of bookings over time (bar graph) 

**For customer users**

 - Most repeated tags in bookings (pie chart) 

 - Most visited locations in bookings (pie chart) 

 - Most used companies for bookings (pie chart) 

**For administrator users**

 - Number of new users and hotels registered over time (dotted line graph) 

 - Number of active users each day (dotted line graph) 

 

### Complementary technology

Integration with Google Maps to show hotels in a certain area, using the interactive map. 

Possibility to print a PDF with reservation details. 
 

### Algorithm or advanced query

An automatic hotel recommendation algorithm will be used, based on each user's previous bookings and ratings. Each hotel can be marked with a series of tags, and remember the user's preferred choices to show personalized suggestions. 


# Phase 1: PAGE LAYOUT WITH HTML AND CSS
### Pages

- Index: Our home page. You start here to use our page.
  
  From here you can log in, or sign up, and watch information about the hotel rooms and reviews they have.


![index](https://github.com/CodeURJC-DAW-2023-24/webapp12/assets/117302443/4b233ea0-b422-48d0-9d54-3827dc6bc632)


- Hotel reviews: In here you can see if other people enjoyed the hotel.
  
  On top you can go to the information of the hotel, and on the bottom you can see peoples reviews. You can rate a hotel only if you had been in there.


![valoraciones](https://github.com/CodeURJC-DAW-2023-24/webapp12/assets/117302443/2dbfe4bd-8154-4fac-8a7b-ab3bdc29b153)


- Hotel information: All the details about the room and it's availability.
  
  You can see pictures of the room, tecnical information like how many people can you book it for and a description. Here you will be able to book it when you log in, to log in you can do it on the top of the page.


![informacionHotel](https://github.com/CodeURJC-DAW-2023-24/webapp12/assets/117302443/4e7fd816-48ba-419f-a94c-ad5abfed062b)


- Log in: Insert credential and get to your profile.

  If you have an account, instert your username and password and keep surfing our page. If not, create one at the link bellow.


![login](https://github.com/CodeURJC-DAW-2023-24/webapp12/assets/117302443/61bae9d4-6c33-4645-b120-5eb02ab90983)


- Sign up: Create your account here and enjoy all the funcionalities of our page.

  Insert your full name, username and create a pasword and enjoy your new and custimized profile


![registro](https://github.com/CodeURJC-DAW-2023-24/webapp12/assets/117302443/8f9d3663-2d41-4673-8116-de9362d8e69c)


- Profile page: Here you can see and edit your personal information.

  Upload a picture of yourself and coustome your profile as you like. Then you can see some relevant information at the sidebar, like your reservations, as a client, or your hotels, if you are a hotel manager, just by clicking at the buttons on your left.


  ![perfil](https://github.com/CodeURJC-DAW-2023-24/webapp12/assets/117302443/4c232d54-c742-48ba-b765-e63ec16bf7b3)
  ![editPerfil](https://github.com/CodeURJC-DAW-2023-24/webapp12/assets/117302443/8327acd4-9244-4dcf-856d-fd4ea9b31312)


- Reservations: Watch your reservations here.

  It doesn´t matter if you want to see if you have booked well the room or check past hotels, this is your place. Seek for all the information you need.


  ![reservas](https://github.com/CodeURJC-DAW-2023-24/webapp12/assets/117302443/f3433eed-512a-45bb-9b5a-e7ed5466afb8)


- List of hotels: As a hotel manager, here you can see your hotels.

  Check all about your hotels and edit the current information about them in here. 


  ![listaHoteles](https://github.com/CodeURJC-DAW-2023-24/webapp12/assets/117302443/6d999702-32a3-4e12-a5db-5eccfa871c37)


- List of clients:Check the information about your clients here.

  This list of profiles shows his clients in an specific hotel to the hotel manager.


  ![listaClientes](https://github.com/CodeURJC-DAW-2023-24/webapp12/assets/117302443/ab93d07b-b877-464f-b63d-83e748e38920)


- List of managers: The administrator can look up and manage the hotel managers in the app.


  ![listaManagers](https://github.com/CodeURJC-DAW-2023-24/webapp12/assets/117302443/3d61c902-59ee-434d-90f8-93e185af50cf)


- Hotel validation: Validate the hotels in your page here

  As an administrator, keep the quality of your page accepting and denying hotels in here.


![hotel validation](https://github.com/CodeURJC-DAW-2023-24/webapp12/assets/117302443/fe2af387-170a-44f2-9cc9-98c8d7bf18a7)

  
- Charts: No matter if you are a hotel manager or an administrator, here you can check your stats.


  The page looks alike, but has diferent type of charts for the diferent users.

  
![charts admin](https://github.com/CodeURJC-DAW-2023-24/webapp12/assets/117302443/f3bb5ac3-65ee-4b2c-bdf2-5a1f1e2f5a5d)


### Diagram Navigation
![Final](https://github.com/CodeURJC-DAW-2023-24/webapp12/assets/123741250/13f95e0b-1883-4fb3-bb92-1f54bc84a9bd)


# PHASE 2: WEB WITH HTML GENERATED IN SERVER AND AJAX

## PAGES

- Index: Our home page. You start here to use our page.
  
  From here you can log in, or sign up, and watch information about the hotel rooms and reviews they have.
![Index](https://github.com/CodeURJC-DAW-2023-24/webapp12/assets/80918031/5b6f17af-2d70-4b36-9d57-ee244c69a614)

- Login: Insert credential and get to your profile.

  If you have an account, instert your username and password and keep surfing our page. If not, create one at the link bellow.
![login](https://github.com/CodeURJC-DAW-2023-24/webapp12/assets/80918031/8efe3b55-0c00-48b4-a8ae-5600ed2810dc)

-Login Error: If you put an invalid credential you will end up here.

![loginError](https://github.com/CodeURJC-DAW-2023-24/webapp12/assets/80918031/360d5502-8921-45b7-beb0-f8cd9d14b6df)

- Sign Up:  Create your account here and enjoy all the funcionalities of our page.

  Insert your full name, username and create a pasword and enjoy your new and custimized profile
![register](https://github.com/CodeURJC-DAW-2023-24/webapp12/assets/80918031/4d85ed8f-fe3a-494f-8457-55a9de26f907)

-Nick Taken: If you try to sign up with an username already in use, you will go to this page.

![nickTaken](https://github.com/CodeURJC-DAW-2023-24/webapp12/assets/80918031/7a41ff3b-a169-450d-9f75-00163facabca)

- Profile: Here you can see and edit your personal information.

  Upload a picture of yourself and coustome your profile as you like. Then you can see some relevant information at the sidebar, like your reservations, as a client, or your hotels, if you are a hotel manager, just by clicking at the buttons on your left.

![Profile](https://github.com/CodeURJC-DAW-2023-24/webapp12/assets/80918031/af91e015-31fd-4e18-a534-72d84685cd0f)

-Edit Profile: Here you can change your user information.

You can update all your user information, even your image.

![editProfile](https://github.com/CodeURJC-DAW-2023-24/webapp12/assets/80918031/c28a08d1-54eb-485c-981c-37d77a2b1224)

- Hotel reviews: Here you can see if other people enjoyed the hotel.
  
  On top you can go to the information of the hotel, and on the bottom you can see peoples reviews.

![hotelReviews](https://github.com/CodeURJC-DAW-2023-24/webapp12/assets/80918031/3010c3ea-9d11-4a4c-99a4-6136c4964ba9)

- Hotel information: All the details about the room and it's availability.
  
  You can see pictures of the hotel and a description. Here you will be able to book it when you log in, to log in you can do it on the top of the page.

![infoHotel](https://github.com/CodeURJC-DAW-2023-24/webapp12/assets/80918031/47c37705-1171-4a4c-a0fa-ab7f85f8641b)

  -Not Rooms: If there is not available rooms when you try to do the reservation you will be sent to this page.

![notRooms](https://github.com/CodeURJC-DAW-2023-24/webapp12/assets/80918031/2989da63-cd5a-47eb-bb14-3ac0bd4c9f87)

- Reservations: Watch your reservations here.

  It doesn´t matter if you want to see if you have booked well the room or check past hotels, this is your place. Seek for all the information you need.

![clientReservations](https://github.com/CodeURJC-DAW-2023-24/webapp12/assets/80918031/2b960aeb-a27a-4533-bbb8-36c3b7a769f8)

- Reservation Information: Watch all the information about one of your resrvations.

  If you want to cancel your reservation you can do it in this page.

![infoReservation](https://github.com/CodeURJC-DAW-2023-24/webapp12/assets/80918031/f80fa923-06ac-490c-9733-79b70bd700f6)


- List of hotels: As a hotel manager, here you can see your hotels.

  Check all about your hotels, edit the current information about them or add new hotels here. 

![managerHotels](https://github.com/CodeURJC-DAW-2023-24/webapp12/assets/80918031/8b2d7b58-8291-4cc8-8bc7-b33bdd2a3faa)


  -Add Hotel: As a manager, here you can add new hotels.

  In this page you can put all the information about your hotel.

![addHotel](https://github.com/CodeURJC-DAW-2023-24/webapp12/assets/80918031/6c6a8c82-c7c8-4e02-a4ad-787efccf2b16)

  -Add Photo Hotel: As a manager, here you can add the photo of your hotel.

![addPhotoHotel](https://github.com/CodeURJC-DAW-2023-24/webapp12/assets/80918031/7579eb5f-2247-4655-a925-03ea87b7e80e)

  -Edit Hotel: As a manager, here you can edit the information of your hotels.

  You can update all the information about your hotel.

![editHotel](https://github.com/CodeURJC-DAW-2023-24/webapp12/assets/80918031/78b3ebcf-e09b-4e31-9372-5775dcef4e33)


  -Manager Validation: As an administrator, you can validated the new managers.

  Check the new registered manager and choose if you validate them.

![managerValidation](https://github.com/CodeURJC-DAW-2023-24/webapp12/assets/80918031/d72062f1-8927-4426-874a-2d923409bd9f)

## Diagram Navigation

![navmap](https://github.com/CodeURJC-DAW-2023-24/webapp12/assets/80918031/216006da-6899-44f7-9a3b-63e936b969bc)

## EXECUTION INSTRUCTIONS

###VERSION 

The tecnologies that we use in this phase are:

    -MySQL: 8.0.19
  
    -Maven : 4.0.0
  
    -Spring Boot: 3.2.3

    -Java: 21

    -Google ReCAPTCHA: Version 2

## CONFIGURATION 

In order to execute the app, you will need to follow the next steps:

  - 1.Download the zip that appears in this link, https://github.com/CodeURJC-DAW-2023-24/webapp12

  - 2.Navigate to the MySQL website at https://dev.mysql.com/downloads/workbench/ and select the 'Download' option.

  - 3.Choose your operating system and click 'Download' to initiate the download procedure.

  - 4.Open MySQL Workbench using your applications menu or start menu.

  - 5.Select 'Local instance 3306' from the 'MySQL Connections' section to establish a connection with the default local   MySQL server

  - 6.Provide your MySQL server credentials if requested.

  - 7.Upon establishing the connection, you'll have the capability to create or manage databases and tables, as well as execute SQL queries using the MySQL Workbench interface.

  - 8.Before utilizing this database, it's necessary to input new data by populating the DataSampleService with information regarding the elements in the tables

  - 9.Additionally, it's advisable to update the 'application.properties' file to use 'create-drop' instead of 'update'.

# EXECUTION
The code for the application is developed in Visual Studio Code. To execute the application, you need to press the 'Run' button within the IDE, ensuring that it is set to the backend directory. Following that, open your preferred browser, such as Google Chrome, and navigate to https://localhost:8443/ to view the main page

# DATABASE ENTITY DIAGRAM

![FINAL DIAGRAM ER](https://github.com/CodeURJC-DAW-2023-24/webapp12/assets/123741250/cd20321b-4803-4d3b-91d2-5fba12500976)


# CLASS AND TEMPLATES DIAGRAM

![classAndTemplates](https://github.com/CodeURJC-DAW-2023-24/webapp12/assets/80918031/c7cdb1af-a1b2-461c-9a78-fd93ccec1a6e)


# Group Members Participation

### Paula Monrobel Ugidos

  - Description: 
Paula implemented the security of the application. She made the login, register, addHotel and addReservation funcionality.

  - Five most important commits

| Commit Number | Description | Link |
|:--:|:----------------------------------------:|:------------------------------------------------------------------------------------------------:|
| #1 | Https and port 8443                      | https://github.com/CodeURJC-DAW-2023-24/webapp12/commit/f808c3e650fb7fe23c825d797e9fea01a2159ab9 |
| #2 | Funcional login with the security        | https://github.com/CodeURJC-DAW-2023-24/webapp12/commit/06d6576cb3395df10ac27ba7229d87a365b10155 |
| #3 | Load user from database for the security | https://github.com/CodeURJC-DAW-2023-24/webapp12/commit/b82d17620744cf1836eb28cd07f2f6ef9433b62d |
| #4 | Add Hotel funcionality                   | https://github.com/CodeURJC-DAW-2023-24/webapp12/commit/0919514f0510402152d3343ba84da95c86401d46 |
| #5 | Add Reservation funcionality             | https://github.com/CodeURJC-DAW-2023-24/webapp12/commit/15e4aece696816d8a61f168e71f71972a2dd17a9 |

### Mario Recio Montero

  - Description: 
Mario was in charge of setting up the database logic and entities, implementation of controllers, backend logic for users, managers and admins, reCAPTCHA technollogy, advanced algorithms, images logic, dynamic mustache templates, along side other feats, bug fixes and code quality improvements.

  - Five most important commits

| Commit Number | Description | Link |
|:--:|:----------------------------------------:|:------------------------------------------------------------------------------------------------:|
| #1 | Implemented Google reCAPTCHA verification| https://github.com/CodeURJC-DAW-2023-24/webapp12/commit/18f7065766ff6dabd74900b933f32e47b358b9f5 |
| #2 | Implemented advanced algorithm for hotels| https://github.com/CodeURJC-DAW-2023-24/webapp12/commit/f00a838b0ef83cfe393160240e51c6604819f4d4 |
| #3 | Allows creation of hotels with photos    | https://github.com/CodeURJC-DAW-2023-24/webapp12/commit/a4d37e910af711c4199b1b94d6531d4d9d6f963c |
| #4 | Access restriction on private entities   | https://github.com/CodeURJC-DAW-2023-24/webapp12/commit/62dd991a08903f2294c8bf16b9f2e65089e5853e |
| #5 | Logic and html for manager validation    | https://github.com/CodeURJC-DAW-2023-24/webapp12/commit/1dae0bf8500ea86dbebe63d151c8ebbdc866a7e4 |


  - Five most participation files

| File number | File |
|:--:|:------------------------:|
| #1 | SecurityConfiguration    |
| #2 | User Controller          |
| #3 | CSRFHandlerConfiguration |
| #4 | ReservationInfo          |
| #5 | addHotel                 |

