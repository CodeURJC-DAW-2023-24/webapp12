import { Component } from '@angular/core';
import { UserService } from '../../service/User.service';
import { HotelService } from '../../service/Hotel.service';
import { Router, ActivatedRoute } from '@angular/router';
import{ User } from '../../entities/user.model';
import{ Hotel } from '../../entities/hotel.model';
import { HttpErrorResponse } from '@angular/common/http';
import { PageResponse } from '../../interfaces/pageResponse.interface';
import { LoginService } from '../../service/Login.service';

@Component({
  selector: 'app-mainPage',
  templateUrl: './mainPage.component.html',
  styleUrls: ["../../../assets/css/hotelPages.component.css","./mainPage.component.css"]
})
export class MainPageComponent {
  title = 'frontend';

  public userType: string[];
  public isUser: boolean;
  public isClient: boolean;
  public user! : User;
  public hotels: Hotel[];
  public searchValue: string;
  public page : number
  public totalPages: number;

constructor(private userService: UserService, private hotelService: HotelService,
  private router: Router, private route: ActivatedRoute, public loginService: LoginService) {
    this.userType = [""];
    this.isUser = false;
    this.isClient = false;
    this.searchValue = '';
    this.hotels = [];
    this.page = 0;
    this.totalPages = 1;
}

ngOnInit() {
  this.getCurrentUser();

}

getCurrentUser() {
  this.userService.getCurrentUser().subscribe({
    next: user => {
        this.user = user;
        this.userType = user.rols;
        this.isUser = user.rols.includes("USER");
        this.isClient = user.rols.includes("CLIENT");
        this.getRecommendedHotels();
    },
    error: err => {
      if (err.status === 403) {
        console.log('Forbidden error');
        this.router.navigate(['/error']);
      } else if (err.status === 404) {
        console.log('User not logged in');
        this.isUser = false;
        this.isClient = false;
        this.userType = [];
        this.getRecommendedHotels();
      }
    }
  });
}

getRecommendedHotels(){
  if(this.page < this.totalPages){
    this.hotelService.getRecommendedHotels(this.page, 6).subscribe({
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


getAllHotelsBySearch(event: Event){
  event.preventDefault();
  this.page = 0;
  this.hotelService.getAllHotelsBySearch(this.searchValue).subscribe({
    next: (hotelsResponse: Hotel[]) => {
      this.hotels.length = 0;
      hotelsResponse.forEach(hotel => {
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

  getAllHotelsSize(){
    this.hotelService.getAllHotelsSize().subscribe({
      next: (size: number) => {
        return size;
      },
      error: (err: HttpErrorResponse) => {
          console.log('Unknown error returning hotels size');
          console.log(err);
          this.router.navigate(['/error']);
      }
    });
  }

  trackByHotelId(index: number, hotel: Hotel): number {
    return hotel.id;
  }
}
