# webapp12

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

## Phase 0: Team creation and definition of website functionalities
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


## Phase 1: Page layout with HTML and CSS
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

![Diaagrama final](https://github.com/CodeURJC-DAW-2023-24/webapp12/assets/123741250/9bc5616e-a5e2-4492-87d5-3c8f806c5ad3)



