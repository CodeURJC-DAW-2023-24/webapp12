import { Component } from '@angular/core';
import { UserService } from '../../service/User.service';
import { Router, ActivatedRoute } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { User } from '../../entities/user.model';
import { PageResponse } from '../../interfaces/pageResponse.interface';
import { LoginService } from '../../service/Login.service';

@Component({
  selector: 'app-managerValidation',
  templateUrl: './managerValidation.component.html',
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
          });
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
        //Store the current URL
        let currentUrl = this.router.url;
        //Navigate to a temporary URL
        this.router.navigateByUrl('/', {skipLocationChange: true}).then(() => {
        //Navigate back to the current URL
        this.router.navigate([currentUrl]);
        });
      },
      error: (err: HttpErrorResponse) => {
        this.router.navigate(['/error']);
      }
    });
  }

  rejectManager(idManager: number) {
    this.userService.setManagerState(idManager, true).subscribe({
      next: _ => {
        //Store the current URL
        let currentUrl = this.router.url;
        //Navigate to a temporary URL
        this.router.navigateByUrl('/', {skipLocationChange: true}).then(() => {
        //Navigate back to the current URL
        this.router.navigate([currentUrl]);
        });
      },
      error: (err: HttpErrorResponse) => {
        this.router.navigate(['/error']);
      }
    });
  }
}
