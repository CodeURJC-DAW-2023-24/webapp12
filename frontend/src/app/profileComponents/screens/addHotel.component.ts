import { Component } from '@angular/core';
import { UserService } from '../../service/User.service';
import { Router } from '@angular/router';
import { User } from '../../entities/user.model';
import { LoginService } from '../../service/Login.service';



@Component({
    selector: 'app-addHotel',
    templateUrl: './addHotel.component.html',
})

export class AddHotelComponent {
    title = 'frontend';

    public user!: User;
    public hotelImageUrl!: string;
    public selectedFile: File;
    public new: boolean;


    constructor(private userService: UserService,
        private router: Router, public loginService: LoginService) {

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
        if (file && file.size > 0) {
            this.hotelImageUrl = URL.createObjectURL(file);
        }
    }
}
