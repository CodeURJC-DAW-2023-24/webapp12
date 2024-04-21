import { Component } from '@angular/core';
import { UserService } from '../../service/User.service';
import { User } from '../../entities/user.model';
import { Router, ActivatedRoute } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { LoginService } from '../../service/Login.service';

@Component({
  selector: 'app-managerValidation',
  templateUrl: './managerValidation.component.html',
  //styleUrl: ''
})
export class ManagerValidationComponent{
  title = 'frontend';

  public unvalidatedManagers!: User[];
  public page!: number;
  public totalPages!: number;

  constructor(private userService: UserService, private router: Router, private route: ActivatedRoute, public loginService: LoginService) {
    this.unvalidatedManagers = [];
    this.page = 0;
    this.totalPages = 1;
  }
/*
  //Por hacer: en unvalidatedManagers poner el array de managers no validados
  ngOnInit() {
		this.unvalidatedManagers = this.userService.getUnvalidatedManagers(0, 6);
  }


  getUnvalidatedManagers() {
    if(this.page < this.totalPages){
      console.log("im in");
      console.log("page",this.page);
      this.userService.getUnvalidatedManagers(this.page, 6).subscribe({
          next: (managers: User[]) => {
            this.unvalidatedManagers = managers;
            this.page++;
          },
          error: (err: HttpErrorResponse) => {
            console.log('Unknown error returning unvalidated managers');
            console.log(err);
            this.router.navigate(['/error']);
          }
        });
    }
  }

*/
  //Por hacer: marcar manager como validado
  acceptManager(idManager: number) {}

  //Por hacer: marcar manager como no validado
  rejectManager(idManager: number) {}

}
