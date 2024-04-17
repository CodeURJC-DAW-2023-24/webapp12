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
    @Input() public childNew: boolean;
    @Input() public childHotel: Hotel | undefined;


    constructor(private userService: UserService,
        private renderer: Renderer2, private el: ElementRef,
        private router: Router, private route: ActivatedRoute,
        private hotelService: HotelService, public loginService: LoginService) {

        this.childSelectedFile = new File([], '');
        this.childNew = false;

    }

    onSubmitHotel(name: string, location: string, desc: string, room1: string, cost1: string, room2: string, cost2: string,
        room3: string, cost3: string, room4: string, cost4: string) {

        if (this.childNew) {
            this.createHotel(name, location, desc, room1, cost1, room2, cost2, room3, cost3, room4, cost4);
        } else {
            this.updateHotel(name, location, desc, room1, cost1, room2, cost2, room3, cost3, room4, cost4);
        }
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

    getRoomDetails(hotel: Hotel) {
        let room1 = 0, room2 = 0, room3 = 0, room4 = 0;
        let cost1 = 0, cost2 = 0, cost3 = 0, cost4 = 0;
    
        if (hotel.rooms) {
            for (let room of hotel.rooms) {
                switch (room.maxClients) {
                    case 1:
                        room1++;
                        cost1 = room.cost;
                        break;
                    case 2:
                        room2++;
                        cost2 = room.cost;
                        break;
                    case 3:
                        room3++;
                        cost3 = room.cost;
                        break;
                    case 4:
                        room4++;
                        cost4 = room.cost;
                        break;
                }
            }
        }
    
        return { room1, cost1, room2, cost2, room3, cost3, room4, cost4 };
    }

    updateHotel(name: string, location: string, description: string, room1: string,
        cost1: string, room2: string, cost2: string, room3: string, cost3: string, room4: string, cost4: string): void {
    
            let updates: { [key: string]: string } = {};
            let { room1: currentRoom1 = '', cost1: currentCost1 = '', room2: currentRoom2 = '', cost2: currentCost2 = '', 
                room3: currentRoom3 = '', cost3: currentCost3 = '', room4: currentRoom4 = '', cost4: currentCost4 = '' } = 
                this.childHotel ? this.getRoomDetails(this.childHotel) : {};

            if (this.childHotel && name !== this.childHotel.name) {
                updates['name'] = name;
            }
            if (this.childHotel && location !== this.childHotel.location) {
                updates['location'] = location;
            }
            if (this.childHotel && description !== this.childHotel.description) {
                updates['description'] = description;
            }
            
            if (parseInt(room1) !== currentRoom1) {
                updates['room1'] = room1;
            }
            if (parseInt(cost1) !== currentCost1) {
                updates['cost1'] = cost1;
            }
            if (parseInt(room2) !== currentRoom2) {
                updates['room2'] = room2;
            }
            if (parseInt(cost2) !== currentCost2) {
                updates['cost2'] = cost2;
            }
            if (parseInt(room3) !== currentRoom3) {
                updates['room3'] = room3;
            }
            if (parseInt(cost3) !== currentCost3) {
                updates['cost3'] = cost3;
            }
            if (parseInt(room4) !== currentRoom4) {
                updates['room4'] = room4;
            }
            if (parseInt(cost4) !== currentCost4) {
                updates['cost4'] = cost4;
            }
    
        let formData = new FormData();
        for (const key in updates) {
            if (updates.hasOwnProperty(key)) {
                formData.append(key, updates[key]);
            }
        }

        if (!this.childHotel || !this.childHotel.id) {
            console.error('Error: childHotel or childHotel.id is not defined');
            this.router.navigate(['/error']);
            return;
        }else{   
            this.hotelService.updateHotelDetails(this.childHotel.id, formData).subscribe({
                next: (_: Hotel) => {
                    this.router.navigate(['/viewHotelsManager']);
                },
                error: err => {
                    // Handle error
                    if (err.status === 400) {
                        console.error('Error updating hotel details: Exception originated from JSON data processing or mapping', err);
                    } else if (err.status === 403) {
                        console.error('Error updating hotel details: Operation not allowed for the current user', err);
                    } else if (err.status === 404) {
                        console.error('Error updating hotel details: Hotel not found', err);
                    } else {
                        console.error('Error updating hotel details', err);
                    }
                    this.router.navigate(['/error']);
                }
            });
        }
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