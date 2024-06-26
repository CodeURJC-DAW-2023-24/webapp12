## Demostration Video: https://youtu.be/AgHdY62ueg0

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
    
- Phase 2
  - Pages
  - Execution instructions
  - Configuration
  - Database Entity diagram
  - Class and templates diagram
  - Group members participation

- Phase 3
  - 1.API REST documentation
    - 1.1.OpenAPI
    - 1.2.HTML  
  - 2.Class and templates diagram
  - 3.Execution instructions
  - 4.Docker image documentation
  - 5.Virtual Machine deployment
  - 6.URL to Virtual Machine
  - 7.Group members participation

- Phase 4
  - Angular requirements and dependencies
  - Class and templates diagram
  - Group members participation
  - Demonstration video  

# Phase 0: Team creation and definition of website functionalities
### App name: yourHOmeTEL

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


# GROUP MEMBERS PARTICIPATION

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

- Five most participation files

| File number | File |
|:--:|:------------------------:|
| #1 | SecurityConfiguration    |
| #2 | User Controller          |
| #3 | CSRFHandlerConfiguration |
| #4 | ReservationInfo          |
| #5 | addHotel                 |


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
| #1 | UserController.java      |
| #2 | HotelController.java     |
| #3 | InitDataBaseService.java |
| #4 | UserService.java         |
| #5 | UserRepository.java      |




### Laila El Khattabi El Hassnaoui

  - Description:
Laila was tasked for creating the model of database entities and it´s logic, implementation of ajax functionality, backend logic for hotels and users and also the achievement to show images on the web that were saved in the database.

  - Five most participation files

| Commit number | Description |  File |
|:--:|:----------------------------------------:|:------------------------------------------------------------------------------------------------:  |
| #1 | Show saved images from database             |https://github.com/CodeURJC-DAW-2023-24/webapp12/commit/53e7f9297978794e1b775fab4988c6ebd8869e83 |
| #2 | Functional hotel and it´s modification      |https://github.com/CodeURJC-DAW-2023-24/webapp12/commit/045c6fd536a04ed4d203f7121ef4f79074082c90 |
| #3 | Allows to edit profile and it´s image       |https://github.com/CodeURJC-DAW-2023-24/webapp12/commit/9029b9e3b1daf1d013e5b533c08f96a01c8fc352 |
| #4 | Database entities and logic                 |https://github.com/CodeURJC-DAW-2023-24/webapp12/commit/9d5a82c25c7ac261fe54322321045a069b69d5a9 |
| #5 | Allows update index hotel images           |https://github.com/CodeURJC-DAW-2023-24/webapp12/commit/0b92837e4f8cc7a9e544328fc3b284b554f1b729

  - Five most participation files

| File number | File |
|:--:|:------------------------:    |
| #1 | HotelController.java             |
| #2 | User.Controller.java              |
| #3 | InitDatabaseInitializater.java    |
| #4 | Load,updateImagesDatabase    |
| #5 | Ajax functions               |

### Carlos Hermán Andrés Andrés

  - Description:
Carlos was tasked to handle the reviews of the hotels, the implementation of some type of charts and AJAX technology throughout the app.

  - Most important commits

| Commit number | Description |  File |
|:--:|:----------------------------------------:|:------------------------------------------------------------------------------------------------:  |
| #1 | AJAX included wherever it's needed        | https://github.com/CodeURJC-DAW-2023-24/webapp12/commit/aa5514671d7187a0b737e33bbd4e84873b84404b  |
| #2 | Post Reviews working                      | https://github.com/CodeURJC-DAW-2023-24/webapp12/commit/b1d0518c2b589299f24b6f9fd3b25456d45bc252  |
| #3 | Reviews graphs fully working              | https://github.com/CodeURJC-DAW-2023-24/webapp12/commit/9eca9a618fb7b4119dcff70bc55fa226f9448159  |
| #4 | Hotel reviews page and styles             | https://github.com/CodeURJC-DAW-2023-24/webapp12/commit/f6669b09db6f77bed120f06102d927119f83a1b2  |
| #5 | Review controller without DB management   | https://github.com/CodeURJC-DAW-2023-24/webapp12/commit/328a80a1672ee6249c1ddc4ee8eecd3b4f6e5b5c  |

  - Most participated files 

| File number | File |
|:--:|:------------------------:|
| #1 | UserController           |
| #2 | hotelReview.html         |
| #3 | loadHotels.js            |
| #4 | loadMoreReservations.js  |
| #5 | HotelController          |


# Phase 3: API REST and Docker images
  ## 1. API REST documentation
  ### 1.1. OpenAPI: (https://github.com/CodeURJC-DAW-2023-24/webapp12/blob/main/api_docs/api-docs-input.yaml)
  ### 1.2. HTML: (https://rawcdn.githack.com/CodeURJC-DAW-2023-24/webapp12/797c0bae3126d5584d9ef2e2cca7778a04a40f8b/api_docs/index.html)
    
  ## 2. Class and templates diagram 
  ![REST DIAGRAM](https://github.com/CodeURJC-DAW-2023-24/webapp12/blob/7a6ecfc4698ca8a85c34dae51c09af332d429d72/assets/diagramaRest.jpeg)
  
  ## 3. Execution instructions
  
  1. Clone the repository with
  ```
  git clone https://github.com/CodeURJC-DAW-2023-24/webapp12.git
  ```
  2. Switch to "docker" directory 
  ```
  cd webapp12/docker
  ```
  3. Install docker on your system [here](https://docs.docker.com/engine/install/)
  4. Find the installed docker, and run it
  5. While being on the directory webapp12/docker, excute on terminal:
  ```
  docker-compose up
  ```
  6. Once the previous step is finished, open an internet browser, and search for [https://localhost:8443](https://localhost:8443)
  
  ## 4. Docker image documentation
  
  1. Use a browser to create an account on [Dockerhub](https://hub.docker.com/)
  2. Clone the repository with
  ```
  git clone https://github.com/CodeURJC-DAW-2023-24/webapp12.git
  ```
  3. Switch to "docker" directory 
  ```
  cd webapp12/docker
  ```
  4. Install docker on your system [here](https://docs.docker.com/engine/install/)
  5. Find the installed docker, and run it
  6. Connect your account to Docker. You can use:
  ```
  docker login
  ```
  And then type your credentials   
  
  7. While being on the directory webapp12/docker, excute on terminal:
  ```
  ./create_image.sh
  ```
  
  ## 5. Virtual Machine deployment
  
  ### 5.1. Connect to virtual machine
  Make sure to have the SSH key on the directory you execute these commands from:
  ```
  cp prAppWeb12.key ~/ 
  chmod 600 ~/prAppWeb12.key
  ssh -i ~/prAppWeb12.key vmuser@10.100.139.161
  ```

  ### 5.2 Install Docker
  ```
  sudo apt-get update
  sudo apt-get install ca-certificates curl
  sudo install -m 0755 -d /etc/apt/keyrings
  sudo curl -fsSL https://download.docker.com/linux/ubuntu/gpg -o /etc/apt/keyrings/docker.asc
  sudo chmod a+r /etc/apt/keyrings/docker.asc
  
  echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.asc] https://download.docker.com/linux/ubuntu \
  $(. /etc/os-release && echo "$VERSION_CODENAME") stable" | \
  sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
  sudo apt-get update
  ```
  
  ### 5.3 Install Docker-Compose plugin
  ```
  sudo apt-get install docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin
  ```
  
  ### 5.4 Test Docker is correctly instaled running
  ```
  sudo docker run hello-world
  ```

 ### 5.5 Prepare Docker Compose
  ```
  sudo curl -L "https://github.com/docker/compose/releases/download/1.29.2/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
  sudo chmod +x /usr/local/bin/docker-compose
  ```

  ### 5.6 Clone the repository
  Execute:
  ```
  ls
  ```
  If you dont see a webapp12 folder, then execute:
  ```
  git clone https://github.com/CodeURJC-DAW-2023-24/webapp12.git
  ```

 ### 5.7 Switch to directory and grant premissions
  ```
  cd webapp12/docker
  chmod +x create_image.sh
  ```

 ### 5.8 Execute the script
  ```
  ./create_image.sh
  ```

 ### 5.9 Open a browser and access
  ```
  https://10.100.139.161:8443/index
  ```


## 6. URL to Virtual Machine: [https://10.100.139.161:8443](https://10.100.139.161:8443)
  
## 7. Group Members Participation

  ### MARIO RECIO MONTERO

  - Description: Implemented various rest controllers for all entities, created docker files and functionality and configurated Virtual Machine deployment, alongside images and controllers adaptations for Linux environment, bug fixes and testing.


  - Five most important commits

| Commit Number | Description | Link |
|:--:|:----------------------------------------:|:------------------------------------------------------------------------------------------------:|
| #1 | JSON view and multiple REST controllers  | https://github.com/CodeURJC-DAW-2023-24/webapp12/commit/2ec3897df735bec20d0d53c9e9b544edd2fc7a8f |
| #2 | Impelemented User REST controllers       | https://github.com/CodeURJC-DAW-2023-24/webapp12/commit/0bcb19efcd5c70da1dd49a7b2723eb3cd4c78c75 |
| #3 | Docker implementation                    | https://github.com/CodeURJC-DAW-2023-24/webapp12/commit/0f916430fa90e97931845863ac2eacdd5d7d7401 |
| #4 | Implemented Reservation REST controllers | https://github.com/CodeURJC-DAW-2023-24/webapp12/commit/8dcde4fbd9569384269039e40f3ad90e59c21950 |
| #5 | Hotel image REST implementation          | https://github.com/CodeURJC-DAW-2023-24/webapp12/commit/00757925520e0064bf1fc109c974afb74f3bbda1 |

- Five highest participation files

| File number | File |
|:--:|:------------------------:|
| #1 | UserRest.java            |
| #2 | ReviewRest.java          |
| #3 | Dockerfile               |
| #4 | docker-compose.yml       |
| #5 | HotelImageRest.java      |

### PAULA MONROBEL UGIDOS
  - Description: Implemented various rest controllers for review and review entities, help with the names of the urls and bug fixes and testing.


  - Five most important commits

| Commit Number | Description | Link |
|:--:|:-------------------------------:|:------------------------------------------------------------------------------------------------:|
| #1 | User Rest Index                 | https://github.com/CodeURJC-DAW-2023-24/webapp12/commit/ee368a2f4c392700b8d493210b3f34c636418b10 |
| #2 | Review Rest Size and Pertentage | https://github.com/CodeURJC-DAW-2023-24/webapp12/commit/594342fbdcfb97ce6f015c2f318831b41e7629ec |
| #3 | Room Rest Put and Delete        | https://github.com/CodeURJC-DAW-2023-24/webapp12/commit/595b3f491a7680742d5e05c8d795bd72daa6bfb4 |
| #4 | Room Rest Getters and Post      | https://github.com/CodeURJC-DAW-2023-24/webapp12/commit/d1606e41aa795a823633df9cc4b5abde8e4c2eb4 |
| #5 | User Rest Getters and Put       | https://github.com/CodeURJC-DAW-2023-24/webapp12/commit/c3d660e06b47728791538e2ec4cebd5d27588683 |

- Five highest participation files

| File number | File |
|:--:|:------------------------:|
| #1 | UserRest.java               |
| #2 | ReviewRest.java             |
| #3 | Security Configuration.java |
| #4 | InitDataBase.java           |
| #5 | UserImageRest.java          |


### Laila El Khattabi El Hassnaoui
  - Description: Implemented various rest controllers for Hotel entity, I have fixed the urls names and added annotations to the rest controllers in order to generate the documentation with OpenApi.

  - Five most important commits

| Commit Number | Description | Link |
|:--:|:-------------------------------:|:------------------------------------------------------------------------------------------------:|
| #1 | Hotel Rest Getters              | https://github.com/CodeURJC-DAW-2023-24/webapp12/commit/d9d24f1510eeb46d5e2f2d03a2d8c94eb8dbec62 |
| #2 | Annotations OpenApi             | https://github.com/CodeURJC-DAW-2023-24/webapp12/commit/dd5d499d76046e318552c24213f179f2d3725a33 |
| #3 | Hotel Rest Delete               | https://github.com/CodeURJC-DAW-2023-24/webapp12/commit/9da374f1634c6109369c3849ad64c8304f91c869 |
| #4 | Hotel Rest Getters              |  https://github.com/CodeURJC-DAW-2023-24/webapp12/commit/79b15040e5884aac4e66bb60a2cae1a637f1b93e|
| #5 | File .yaml and swagger          | https://github.com/CodeURJC-DAW-2023-24/webapp12/commit/d5abbd93a95df135fd68914b10012d8b77899615  |

- Five highest participation files

| File number | File |
|:--:|:------------------------:|
| #1 | HoteRest.java               |
| #2 | ReviewRest.java             |
| #3 | Security Configuration.java |
| #4 | RoomRest.java               |
| #5 | ReservationRest.java        |


### Carlos Hermán Andrés Andrés
  - Description: Implemented various rest controllers for Hotel entity, discussed about the structure and organization of the REST controllers, and added documentation with OpenAPI to the project.

  - Five most important commits

| Commit Number | Description | Link |
|:--:|:-------------------------------:|:------------------------------------------------------------------------------------------------:|
| #1 | Added OpenAPI doc to UserImageRest | https://github.com/CodeURJC-DAW-2023-24/webapp12/commit/0029c5ff5d0805537a86aa07603129a72a2debbd |
| #2 | Added edit controllers to HotelRest | https://github.com/CodeURJC-DAW-2023-24/webapp12/commit/f06b6441d3a93645adc1b15eb41c2c53333eea7c |
| #3 | Controller that gets all Hotel | https://github.com/CodeURJC-DAW-2023-24/webapp12/commit/20556ed7de87294d856d74a6e4b2134843323c3a |
| #4 | Several fixes to the hotelRest controllers | https://github.com/CodeURJC-DAW-2023-24/webapp12/commit/0d779c284a3f252782eb1c1b307dc93dd441964f |
| #5 | Added OpenAPI documentation to userRest controller | https://github.com/CodeURJC-DAW-2023-24/webapp12/commit/1f198a3534e74d4cdd661fc8042298975bff4ab2 |

- Five highest participation files

| File number | File |
|:--:|:------------------------:|
| #1 | HoteRest.java               |
| #2 | UserRest.java             |
| #3 | SecurityConfiguration.java |
| #4 | HotelService.java               |
| #5 | ReviewRest.java        |


# Phase 4: SPA implementation with Angular

## 1. Angular requirements and dependencies
To be able to execute angular projects, you first need to install the following:
  ### 1.1 Install Angular command-line tool
  -g option allows a global instalation, aviable for all your directories
  ```
  npm install -g @angular/cli
  ```

 ### 1.2 Clone or update the repository
  If you havent cloned the repository yet, then run
  ```
  git clone https://github.com/CodeURJC-DAW-2023-24/webapp12.git
  cd /webapp12/frontend
  ```
  If you have already cloned the repository, then you could try to pull for new changes.
  Make sure to execute the command on the root of the cloned project
  ```
  git pull
  cd frontend
  ```

### 1.3 Install Angular modules
  Once you are on the frontend folder, you can install the Angular modules defined on the 
  Angular project with:
  ```
  npm install
  ```

### 1.4 Serve Angular project
  Once finished, you can run the following
  ```
  npm start
  ```
  And access the main page with: https://localhost:8444/new/mainPage

## 2. Class and templates diagram 
  ![SPA DIAGRAM1](https://github.com/CodeURJC-DAW-2023-24/webapp12/blob/main/assets/Diagrama%20peque%C3%B1o.svg)
  ![SPA DIAGRAM2](https://github.com/CodeURJC-DAW-2023-24/webapp12/blob/main/assets/Diagrama%20grande.svg)

## 3. Group Members Participation

  ### MARIO RECIO MONTERO

  - Description: Initialized and condifigurated angular project. Managed routing security and developed mutiple angular components for profile pages. Created new docker image containing Angular dependencies and launched the new angular app on the Virtual Machine.


  - Five most important commits

| Commit Number | Description | Link |
|:--:|:----------------------------------------:|:------------------------------------------------------------------------------------------------:|
| #1 | Edit profile and Client Reservation      | https://github.com/CodeURJC-DAW-2023-24/webapp12/commit/3a296c92defd290655b94b8035fccdebb047cf32 |
| #2 | REST images and Main Page                | https://github.com/CodeURJC-DAW-2023-24/webapp12/commit/3a296c92defd290655b94b8035fccdebb047cf32 |
| #3 | Docker implementation                    | https://github.com/CodeURJC-DAW-2023-24/webapp12/commit/571b3f68903a3ce38a34f89d9a14127e324dcda4 |
| #4 | View Hotels Manager and Add Hotel        | https://github.com/CodeURJC-DAW-2023-24/webapp12/commit/d3a5df5c1fddc96f67326ebd0ea7d3dde9562837 |
| #5 | Edit Hotel and routing security          | https://github.com/CodeURJC-DAW-2023-24/webapp12/commit/c708ea2a042f21e9f62c973c7269b347932fbeef |

- Five highest participation files

| File number | File |
|:--:|:------------------------:|
| #1 | AddHotel.component.ts    |
| #2 | EditHotel.component.ts   |
| #3 | Dockerfile               |
| #4 | MainPageComponent.ts     |
| #5 | App-routing.module.ts    |

### PAULA MONROBEL UGIDOS
  - Description: Implements various angular components, fixed bugs and reformat and fixed little details.


  - Five most important commits

| Commit Number | Description | Link |
|:--:|:-------------------------------:|:------------------------------------------------------------------------------------------------:|
| #1 | HotelReviews TypeScript and CSS | https://github.com/CodeURJC-DAW-2023-24/webapp12/commit/51cdc6135da0560f03e88138c2a2df0067499c9c |
| #2 | HotelReviews Funcional          | https://github.com/CodeURJC-DAW-2023-24/webapp12/commit/447e977f009dae507d647266af5b7f48c1b532da |
| #3 | ManagerValidation Funcional     | https://github.com/CodeURJC-DAW-2023-24/webapp12/commit/6a78b05d184522f7c4d9b188b943c0a21d5a77b4 |
| #4 | ReservationInfo Funcional       | https://github.com/CodeURJC-DAW-2023-24/webapp12/commit/6adbf88a72c0ebf0da8b8ed1fdc8b770790e3d5a |
| #5 | Pagination for hotelClientList  | https://github.com/CodeURJC-DAW-2023-24/webapp12/commit/2b12b91269c6acd5c792094e39e43a29a61c4b8f |

- Five highest participation files

| File number | File |
|:--:|:--------------------------------:|
| #1 | managerValidation.component.ts   |
| #2 | managerValidation.component.html |
| #3 | hotelReviewsl.component.ts       |
| #4 | clientList.component.html        |
| #5 | HotelRest.java                   |


### Laila El Khattabi El Hassnaoui
  - Description: Implemented various angular components and fixed some details
  - Five most important commits

| Commit Number | Description | Link |
|:--:|:-------------------------------:|:------------------------------------------------------------------------------------------------:|
| #1 | Not rooms funcional             | https://github.com/CodeURJC-DAW-2023-24/webapp12/commit/edafd43d4190da4991a9634f0696699a240e40cc |
| #2 | Hotel information funcional     | https://github.com/CodeURJC-DAW-2023-24/webapp12/commit/e713d3897764808e06294d8fea34ffe94c9b99ae |
| #3 | HotelReviews                    | https://github.com/CodeURJC-DAW-2023-24/webapp12/commit/f22a9cdcc090f03cdbcac9391d8cd0778763b9ef |
| #4 | clientList component            | https://github.com/CodeURJC-DAW-2023-24/webapp12/commit/b8dcd081e7f8bab0a5ccf92a97cccdaacebf9409 |
| #5 | Typescript hotelInformation      | https://github.com/CodeURJC-DAW-2023-24/webapp12/commit/e06a8ca935ca9237ddc31e69febe728de8c6a867|

- Five highest participation files

| File number | File |
|:--:|:------------------------------:|
| #1 | notRooms.component.html        |
| #2 | notRooms.component.ts          |
| #3 | hotelInformation.component.html|
| #4 | hotelInformation.component.ts  |
| #5 | hotelReviews.component.html    |


### Carlos Hermán Andrés Andrés
  - Description: Implementation of several angular components, addition of pageable elements, bugfixer in late stages of sprint and designer of SPA diagram.

  - Five most important commits

| Commit Number | Description | Link |
|:--:|:-------------------------------:|:------------------------------------------------------------------------------------------------:|
| #1 | Pagination for hotel clientList | https://github.com/CodeURJC-DAW-2023-24/webapp12/commit/2b12b91269c6acd5c792094e39e43a29a61c4b8f |
| #2 | Load more clients working | https://github.com/CodeURJC-DAW-2023-24/webapp12/commit/918d3321571e562eb4f1e6c1e7d3816188f608cb |
| #3 | Added empty list control to clientReservations | https://github.com/CodeURJC-DAW-2023-24/webapp12/commit/853a4137ef4d0be85e3ca2004fe4f1b00c6501a9 |
| #4 | Added clientList component funcionality | https://github.com/CodeURJC-DAW-2023-24/webapp12/commit/8ce4f77158a404a79d2f92f91b3d27057f136bcf |
| #5 | Created components for clientList | https://github.com/CodeURJC-DAW-2023-24/webapp12/commit/ce208bc7c9762de4d4c4d7807b51f6b755d77c2a |

- Five highest participation files

| File number | File |
|:--:|:------------------------:|
| #1 | index |
| #2 | clientList.component.html |
| #3 | clientList.component.ts |
| #4 | managerValidation.component.ts |
| #5 | clientReservations.component.ts |




