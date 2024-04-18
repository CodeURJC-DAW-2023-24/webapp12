import { Component, Renderer2, ElementRef } from '@angular/core';
import { UserService } from '../../service/User.service';
import { Router, ActivatedRoute } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { Reservation } from '../../entities/reservation.model';
import { User } from '../../entities/user.model';
import { HotelService } from '../../service/Hotel.service';
import { ReservationService } from '../../service/Reservation.service';
import { PageResponse } from '../../interfaces/pageResponse.interface';
import { Hotel } from '../../entities/hotel.model';
import { LoginService } from '../../service/Login.service';



@Component({
    selector: 'app-hotelReviews',
    templateUrl: './hotelReviews.component.html',
    //styleUrl: ''
})
export class hotelReviewsComponent{
    title = 'frontend';

    public user!: User;
    public hotelImageUrl!: string;
    public selectedFile: File;
    public new: boolean;


    constructor(private userService: UserService,
        private renderer: Renderer2, private el: ElementRef,
        private router: Router, private route: ActivatedRoute,
        private hotelService: HotelService, public loginService: LoginService) {

        this.selectedFile = new File([], '');
        this.new = true;

    }

    ngOnInit() {
        this.getCurrentUser();
    }

    getCurrentUser() {
        this.userService.getCurrentUser().subscribe({
            next: (user: User) => {
                this.user = user;
                this.hotelImageUrl = "assets/images/default-hotel.jpg";
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

    onFileSelected(event: Event) {
        event.preventDefault();
        const inputElement = event.target as HTMLInputElement;
        if (inputElement.files && inputElement.files.length > 0) {
            this.selectedFile = inputElement.files[0];
        }
    }

    addHotelImage(file: File) {
        console.log(file.size);
        if (file && file.size > 0) {
            this.hotelImageUrl = URL.createObjectURL(file);
        }
    }
}
