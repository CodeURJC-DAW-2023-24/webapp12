import { Component } from '@angular/core';
import { UserService } from '../../service/User.service';
import { User } from '../../entities/user.model';
import { Router, ActivatedRoute } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { LoginService } from '../../service/Login.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
})
export class ProfileComponent {
  title = 'frontend';

  public userType: string[];
  public isUser: boolean;
  public isClient: boolean;
  public isManager: boolean;
  public isAdmin: boolean;
  public isValidated: boolean;
  public user! : User;
  public imageUrl!: string;

constructor(private userService: UserService, private router: Router,
  public loginService: LoginService) {
    this.userType = [""];
    this.isUser = false;
    this.isClient = false;
    this.isManager = false;
    this.isAdmin = false;
    this.isValidated = false;

}

  ngOnInit() {
    this.userService.getCurrentUser().subscribe({
    next: (user: User) =>{
      this.user = user;

      this.userType = user.rols;
      this.isUser = user.rols.includes("USER");
      this.isClient = user.rols.includes("CLIENT");
      this.isManager = user.rols.includes("MANAGER");
      this.isAdmin = user.rols.includes("ADMIN");
      this.imageUrl = `/api/users/${user.id}/image`;
      this.isValidated = user.validated? true : false;
    },
    error: (err: HttpErrorResponse) => {
      if (err.status === 403) {
        console.log('Forbidden error');
        this.router.navigate(['/error']);

      }else {
        console.log('Unknown error');
        this.router.navigate(['/error']);
      }
    }
    });
  }

  applyManager(event: Event){
    event.preventDefault();
    this.userService.applyManager(this.user.id).subscribe({
      next: (newUser: User) =>{
        this.user = newUser;
      },
      error: (err: HttpErrorResponse) => {
        if (err.status === 400) {
          console.log('Bad Request error');
          this.router.navigate(['/error']);
        } else if (err.status === 409) {
          console.log('Conflict error');
          this.router.navigate(['/error']);
        } else if (err.status === 403) {
          console.log('Forbidden error');
          this.router.navigate(['/error']);
        } else {
          console.log('Unknown error');
          this.router.navigate(['/error']);
        }
      }
    });
  }

  logOut(){
    this.userService.logOut().subscribe({
      next: () => {
        this.router.navigate(['/login']);
      },
      error: (err: HttpErrorResponse) => {
        console.log(err);
        this.router.navigate(['/error']);
      }
    });
  }
}
