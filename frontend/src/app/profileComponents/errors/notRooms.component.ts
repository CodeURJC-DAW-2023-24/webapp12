import { User } from './../../entities/user.model';
import { UserService } from './../../service/User.service';
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { LoginService } from '../../service/Login.service';
import { RoomService } from '../../service/Room.service';
import { HttpClient } from '@angular/common/http';
import { Location } from '@angular/common';

@Component({
  selector: 'app-notrooms',
  templateUrl: './notRooms.component.html',
  styleUrls: ["../../../assets/css/hotelPages.component.css"]
})
export class NotRoomsComponent  {
  title = 'frontend';


  public isUser: boolean;

  public allRoomsBooked: boolean;
  public user!: User;



  constructor(
    private http: HttpClient,
    private loginService: LoginService,
    private router: Router,
    private roomService: RoomService,
    private userService: UserService,
    private location: Location
  ) {
    this.isUser = false;

    this.allRoomsBooked = false;
  }

  ngOnInit() {
    this.isUser = this.loginService.isUser();
    this.getCurrentUser();
  }

  goBack() {
    this.location.back();
  }


  getCurrentUser() {
    this.userService.getCurrentUser().subscribe({
      next: (user: User) => {
        this.user = user;
      },
      error: err => {
        if (err.status === 403) {
          console.log('Forbidden error');
          this.router.navigate(['/error']);
        } else {
          console.log('No user logged in');
          this.router.navigate(['/error']);
        }
      }
    });
  }


}
