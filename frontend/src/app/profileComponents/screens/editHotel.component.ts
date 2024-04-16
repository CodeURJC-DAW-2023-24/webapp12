import { Component } from '@angular/core';
import { UserService } from '../../service/User.service';
import { HotelService } from '../../service/Hotel.service';
import { User } from '../../entities/user.model';
import { Router, ActivatedRoute } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { LoginService } from '../../service/Login.service';
import { Hotel } from '../../entities/hotel.model';

@Component({
    selector: 'app-editHotel',
    templateUrl: './editHotel.component.html',
    //styleUrl: ''
})
export class EditHotelComponent {
    title = 'frontend';
    public user!: User;
    public hotel!: Hotel;

    public hotelId!: number;
    public imageUrl!: string;
    public selectedFile: File;
    public new : boolean;

    constructor(private userService: UserService, private hotelService: HotelService, private router: Router, 
        private route: ActivatedRoute, public loginService: LoginService) {
        this.selectedFile = new File([], '');
        this.new = false;

        this.route.params.subscribe(params => {
            this.hotelId = params['hotelId'];
        });

    }

    ngOnInit() {
        this.getCurrentUser();
        this.getHotel();
        
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

      getHotel(){
        this.hotelService.getHotelById(this.hotelId).subscribe({
            next: (hotel: Hotel) => {
                this.hotel = hotel;
                this.imageUrl = `/api/hotels/${hotel.id}/image`;
            },
            error: (err: HttpErrorResponse) => {
                if (err.status === 403) {
                    console.log('Forbidden error');
                    this.router.navigate(['/error']);

                } else {
                    console.log('Unknown error');
                    this.router.navigate(['/error']);
                }
            }
        });
      }

    

    fileSelected(event: Event) {
        event.preventDefault();
        const inputElement = event.target as HTMLInputElement;
        if (inputElement.files && inputElement.files.length > 0) {
            this.selectedFile = inputElement.files[0];
        }
    }
    

    editHotelImage(file: File) {
        if (file) {
            this.hotelService.editHotelImage(this.hotelId, file).subscribe({
            next: _ => {
                console.log('Image updated');
                this.imageUrl = `/api/hotels/${this.hotelId}/image?${new Date().getTime()}`;
            },
            error: err => {
                // Handle error
                if (err.status === 400) {
                    console.error('Error updating user details: Exception originated from JSON data processing or mapping', err);
                } else if (err.status === 403) {
                    console.error('Error updating user details: Operation not allowed for the current user', err);
                } else if (err.status === 404) {
                    console.error('Error updating user details: User not found', err);
                } else {
                    console.error('Error updating user details', err);
                }
                this.router.navigate(['/error']);
            }
      });
    }
    }
}