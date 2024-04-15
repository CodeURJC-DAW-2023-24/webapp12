import { Component, Renderer2, ElementRef, Input } from '@angular/core';
import { UserService } from '../../service/User.service';
import { Router, ActivatedRoute } from '@angular/router';
import { User } from '../../entities/user.model';
import { HotelService } from '../../service/Hotel.service';
import { Hotel } from '../../entities/hotel.model';
import { LoginService } from '../../service/Login.service';



@Component({
    selector: 'app-hotelForm',
    templateUrl: './hotelForm.component.html',
    //styleUrl: ''
})
export class HotelFormComponent {
    title = 'frontend';

    @Input() public childSelectedFile: File;


    constructor(private userService: UserService,
        private renderer: Renderer2, private el: ElementRef,
        private router: Router, private route: ActivatedRoute, 
        private hotelService: HotelService, public loginService: LoginService) {

        this.childSelectedFile = new File([], '');

    }

    createHotel(name: string, location: string, description: string, room1: string, cost1: string, room2: string, cost2: string, room3: string, cost3: string, room4: string, cost4: string) {
        let formData = new FormData();
        formData.append('name', name);
        formData.append('location', location);
        formData.append('description', description);
        formData.append('room1', room1);
        formData.append('cost1', cost1);
        formData.append('room2', room2);
        formData.append('cost2', cost2);
        formData.append('room3', room3);
        formData.append('cost3', cost3);
        formData.append('room4', room4);
        formData.append('cost4', cost4);

        this.hotelService.createNewHotel(formData).subscribe({
            next: (hotel: Hotel) => {
                this.addPhotoToHotel(hotel.id);
                this.router.navigate(['/viewHotelsManager']);
            },
            error: err => {
                // Handle error
                if (err.status === 201) {
                    console.log('Hotel created successfully, but response could not be parsed', err);
                } else if (err.status === 400) {
                    console.error('Error updating user details: Exception originated from JSON data processing or mapping', err);
                } else if (err.status === 403) {
                    console.error('Error updating user details: Operation not allowed for the current user', err);
                } else if (err.status === 404) {
                    console.error('Error updating user details: User not found', err);
                } else {
                    console.error('Error updating user details', err);
                    this.router.navigate(['/error']);
                }
            }
        });
    }

    addPhotoToHotel(id: number) {
        this.hotelService.editHotelImage(id, this.childSelectedFile).subscribe({
            next: _ => {
                console.log('Hotel image added');
            },
            error: err => {
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