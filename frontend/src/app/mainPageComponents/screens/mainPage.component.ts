import { Component } from '@angular/core';
import { UserService } from '../../service/UserService';
import { HotelService } from '../../service/HotelService';
import { Router, ActivatedRoute } from '@angular/router';
import{ User } from '../../entities/user.model';
import{ Hotel } from '../../entities/hotel.model';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';

@Component({
  selector: 'app-mainPage',
  templateUrl: './mainPage.component.html',
  //"../../../shared/styles/hotelsPage.component.css",
  styleUrls: ["./mainPageButtons.component.css"]
})
export class MainPageComponent {
  title = 'frontend';

  public userType: string[];
  public isUser: boolean;
  public isClient: boolean;
  public user! : User;
  public hotels: Hotel[];
  public searchValue: string;

constructor(private userService: UserService, private hotelService: HotelService,
  private router: Router, private route: ActivatedRoute) {
    this.userType = [""];
    this.isUser = false;
    this.isClient = false;
    this.searchValue = '';
    this.hotels = []; 
}

ngOnInit() {
  this.getCurrentUser();
  this.getFirstHotels();
}

getCurrentUser() {
  this.userService.getCurrentUser().subscribe({
    next: user => {
      if (user && user.username){
        this.user = user;
        this.userType = user.rols;
        this.isUser = user.rols.includes("USER");
        this.isClient = user.rols.includes("CLIENT");
      }
      else{
        this.isUser = false;
        this.isClient = false;
      }
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

  getFirstHotels(){
    this.hotelService.getRecommendedHotels(0, 6).subscribe({
      next: (recommendedhHotels: Hotel[]) => {
        recommendedhHotels.forEach(hotel => {
          this.hotels.push(hotel);
        });
      },
      error: (err: HttpErrorResponse) => {
          console.log('Unknown error returning hotels');
          console.log(err);
          this.router.navigate(['/error']);
      }
    });
  }

  getHotelsBySearch(event: Event){
    event.preventDefault();
    this.hotelService.getHotelsBySearch(0, 6, this.searchValue).subscribe({
      next: (searchHotels: Hotel[]) => {
        this.hotels.length = 0;
        searchHotels.forEach(hotel => {
          this.hotels.push(hotel);
        });
      },
      error: (err: HttpErrorResponse) => {
          console.log('Unknown error returning hotels');
          console.log(err);
          this.router.navigate(['/error']);
      }
    });
  }

  getHotelImageUrl(hotelId: number): string {
    return `/api/hotels/${hotelId}/image`;
  }
}