import { Component } from '@angular/core';
import { UserService } from '../../service/User.service';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { User } from '../../entities/user.model';
import { PageResponse } from '../../interfaces/pageResponse.interface';
import { Hotel } from '../../entities/hotel.model';
import { HotelService } from '../../service/Hotel.service';
import { LoginService } from '../../service/Login.service';



@Component({
  selector: 'app-viewHotelManager',
  templateUrl: './viewHotelsManager.component.html',
})
export class ViewHotelsManagerComponent {
  title = 'frontend';

  public user! : User;
  public page: number;
  public totalPages: number;
  public averageRating: number;
  public hotels: Hotel[];



  constructor(private userService: UserService,
    private router: Router, private hotelService: HotelService,
    public loginService: LoginService) {
    this.page = 0;
    this.totalPages = 1;
    this.averageRating = 0;
    this.hotels = [];
  }

  ngOnInit() {
    this.getCurrentUser();
  }

  getCurrentUser() {
    this.userService.getCurrentUser().subscribe({
      next: (user: User) => {
        this.user = user;
        this.getHotels(user.id);
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

  getHotels(id: number){
    if(this.page < this.totalPages){
      this.hotelService.getManagerHotels(id, this.page, 6).subscribe({
        next: (pageResponse: PageResponse<Hotel>) => {
          this.totalPages = pageResponse.totalPages;
          pageResponse.content.forEach(hotel => {
            this.hotels.push(hotel);
          });
          this.page += 1;
        },
        error: (err: HttpErrorResponse) => {
          console.log('Unknown error returning hotels');
          console.log(err);
          this.router.navigate(['/error']);
        }
      });
    }
  }


  deleteHotel(hotel: Hotel) {
    this.hotelService.deleteHotel(hotel.id).subscribe({
      next: () => {
        this.hotels = this.hotels.filter(h => h.id !== hotel.id);
      },
      error: (err: HttpErrorResponse) => {
        console.log('Unknown error deleting hotel');
        console.log(err);
        this.router.navigate(['/error']);
      }
    });
  }

}
