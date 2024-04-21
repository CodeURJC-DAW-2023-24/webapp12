import { Component, Renderer2, ElementRef } from '@angular/core';
import { UserService } from '../../service/User.service';
import { Router, ActivatedRoute } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { Reservation } from '../../entities/reservation.model';
import { User } from '../../entities/user.model';
import { ReservationService } from '../../service/Reservation.service';
import { PageResponse } from '../../interfaces/pageResponse.interface';
import { LoginService } from '../../service/Login.service';

@Component({
  selector: 'app-managerValidation',
  templateUrl: './managerValidation.component.html',
  //styleUrl: ''
})
export class ManagerValidationComponent{
  title = 'frontend';

  public unvalidatedManagers: User[];
  public page!: number;
  public totalPages!: number;

  constructor(private userService: UserService, private router: Router, private route: ActivatedRoute, public loginService: LoginService) {
    this.unvalidatedManagers = [];
    this.page = 0;
    this.totalPages = 1;
  }

  //Por hacer: en unvalidatedManagers poner el array de managers no validados
  ngOnInit() {
    this.getUnvalidatedManagers()
  }

  getUnvalidatedManagers(){
    if(this.page < this.totalPages){
      this.userService.getUnvalidatedManagers(this.page, 1).subscribe({
        next: (pageResponse: PageResponse<User>) => {
          this.totalPages = pageResponse.totalPages;
          pageResponse.content.forEach(manager => {
            this.unvalidatedManagers.push(manager);
            console.log(this.unvalidatedManagers)
          });
          // Increment the page number after each successful API call
          this.page += 1;
        },
        error: (err: HttpErrorResponse) => {
          console.log('Unknown error returning reservations');
          console.log(err);
          this.router.navigate(['/error']);
        }
      });
    }
  }


  acceptManager(idManager: number) {
    this.userService.setManagerState(idManager, false).subscribe({
      next: _ => {

        // Guarda la URL actual
        let currentUrl = this.router.url;

        // Navega a una URL temporal
        this.router.navigateByUrl('/', {skipLocationChange: true}).then(() => {
        // Navega de nuevo a la URL actual
        this.router.navigate([currentUrl]);
        });
      },
      error: (err: HttpErrorResponse) => {
        // Handle other errors
        this.router.navigate(['/error']);
      }
    });
  }

  rejectManager(idManager: number) {
    this.userService.setManagerState(idManager, true).subscribe({
      next: _ => {

        // Guarda la URL actual
        let currentUrl = this.router.url;

        // Navega a una URL temporal
        this.router.navigateByUrl('/', {skipLocationChange: true}).then(() => {
        // Navega de nuevo a la URL actual
        this.router.navigate([currentUrl]);
        });
      },
      error: (err: HttpErrorResponse) => {
        // Handle other errors
        this.router.navigate(['/error']);
      }
    });
  }


}
