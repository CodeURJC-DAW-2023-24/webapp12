# webapp12
### Nombre de la aplicación: YourHOmeTEL

### Integrantes del equipo: 
| Nombre                         | Correo                            | Usuario GitHub       |
|:------------------------------:|:---------------------------------:|:--------------------:|
| Mario Recio Montero            | m.recio.2020@alumnos.urjc.es      | Mario-Recio          |
| Sara Tuset Villoria            | s.tuset.2020@alumnos.urjc.es      | SaraTuset            |
| Paula Monrobel Ugidos          | p.monrobel.2019@alumnos.urjc.es   | Akram1013            |
| Laila El Khattabi El Hassnaoui | l.elkhattabi.2020@alumnos.urjc.es | lailaelkhattabielhas |
| Carlos Herman Andrés Andrés    | ch.andres.2020@alumos.urjc.es     | Carlos-Herman        |


### Herramientas utilizadas (trello) 

Link: https://trello.com/invite/g12daw/ATTI831eec52f3cf9323d36dd12dcf07b91c5373DA36 



### Entidades

- Usuario 

- Hotel 

- Reserva 

- Reseña


Relación: Un usuario puede hacer reservas en un hotel o gestionar un hotel, y crear, consultar o modificar reseñas sobre este. 

![ERDAW](https://trello.com/invite/b/iGxtvT4U/ATTI1b226cf2e0e223b41ba1def2e83dffe85B04FE01/tablero-principal)

### Permisos

Usuario Anónimo: puede ver las páginas de los hoteles y las reseñas. 

Usuario Registrado: 

- **Cliente**: puede reservar habitaciones, escribir reseñas y consultar su información personal.   

- **Gerente del hotel**: puede modificar la información de los hoteles que gestiona, y consultar distintas estadísticas y métricas de interés.  

- **Administrador**: puede cambiar la información de los hoteles y eliminar reseñas.  También puede gestionar información relativa a los usuarios de la página, además de gestionar, verificar y validar los eventos de los usuarios. 

Todos los usuarios tendrán también la opción de iniciar sesión o registrar una cuenta. Para crear una cuenta de gestor de hotel, se deberá tener aprobación de la administración. 
 

### Imágenes 

Hotel: Foto del hotel y / o habitaciones 

Valoración: Es posible que el cliente ponga una foto en su valoración. 

Usuario registrado: Una foto de perfil para su avatar. 

 

### Gráficos

**Para gerentes de hoteles**  

- Puntuación de las valoraciones con el tiempo (gráfica de puntos unidos con líneas) 

- Número de reservas a lo largo del tiempo (gráfica de barras) 

**Para usuarios cliente**

 - Etiquetas más repetidas en las reservas (gráfica circular) 

 - Localizaciones más visitadas en las reservas (gráfica circular) 

 - Compañías más utilizadas para las reservas (gráfica circular) 

**Para usuario administrador**

 - Número de nuevos usuarios y hoteles registrados en el tiempo (gráfica de puntos unidos con líneas) 

 - Número de usuarios activos cada día (gráfica de puntos unidos con líneas) 

 

### Tecnología complementaria

Integración con Google Maps para mostrar los hoteles en una cierta área, utilizando el mapa interactivo. 

Permitir imprimir un PDF con los detalles de la reserva 
 

### Algoritmo o consulta avanzada

Se utilizará un algoritmo de recomendación automática de hoteles, basado en las reservas previas de cada usuario, y sus valoraciones. Puede marcarse cada hotel con una serie de etiquetas, y recordar las elecciones preferidas del usuario para mostrarle sugerencias personalizadas. 
