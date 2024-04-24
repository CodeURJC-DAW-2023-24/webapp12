import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { LoginService } from '../../service/Login.service';
import { RoomService } from '../../service/Room.service';
import { Room } from '../../entities/room.model';
import { HttpClient } from '@angular/common/http';
@Component({
  selector: 'app-notrooms',
  templateUrl: './notRooms.component.html',
  //styleUrl: ''
  styleUrls: ["../../../assets/css/hotelPages.component.css"]
})
export class NotRoomsComponent  {
  title = 'frontend';


  public isUser: boolean;
  public id: string;
  public allRoomsBooked: boolean;

  constructor(
    private http: HttpClient,
    private loginService: LoginService,
    private router: Router,
    private roomService: RoomService
  ) {
    this.isUser = false;
    this.id = '';
    this.allRoomsBooked = false;
  }

  ngOnInit() {
    this.isUser = this.loginService.isUser();
    this.id = this.loginService.currentUser()?.id.toString();
    // Check if all rooms are booked when the component is initialized
    this.roomService.getAllRooms().subscribe(rooms => {
      this.allRoomsBooked = rooms.every(room => room.reservations && room.reservations.length > 0);
    });
  }
  checkAllRoomsBooked() {
    this.roomService.getAllRooms().subscribe(
      (rooms: Room[]) => {
        this.allRoomsBooked = rooms.every(room => room.reservations && room.reservations.length > 0);
        if (this.allRoomsBooked) {
          this.router.navigate(['notRooms']);
        }
      },
      error => {
        console.error('Error fetching rooms', error);
      }
    );
  }
  bookRoom(roomId: number) {
    this.roomService.getRoomById(roomId).subscribe((room: Room) => {
      if (room.reservations && room.reservations.length > 0) {
        // The room is already booked, so navigate to 'notRooms' without trying to book it
        this.router.navigate(['notRooms']);
      } else {
        // The room is not booked, so try to book it
        const reservationDetails = {
          // Fill this object with the details of the reservation
        };
        this.http.post(`https://localhost:8444/api/reservations/users/${this.id}/hotels/${roomId}`, reservationDetails)
          .subscribe(
            response => {
              // Handle successful response
              this.router.navigate(['rooms']);
            },
            error => {
              // Handle other errors
            }
          );
      }
    });
  }
}
