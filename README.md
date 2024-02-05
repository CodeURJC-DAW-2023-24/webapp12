# webapp12
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
