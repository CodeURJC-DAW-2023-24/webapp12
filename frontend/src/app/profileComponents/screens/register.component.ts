import { Component } from '@angular/core';
import { UserService } from '../../service/User.service';
import { Router, ActivatedRoute } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { LoginService } from '../../service/Login.service';


@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
})
export class RegisterComponent {
  title = 'frontend';

  public userType: number;
  public isUser: boolean;
  public nick: string;
  public name: string;
  public lastname: string;
  public email: string;
  public pass: string;

constructor(private userService: UserService, private router: Router, public loginService: LoginService) {
    this.userType = -1;
    this.isUser = false;
    this.nick = '';
    this.name = '';
    this.lastname = '';
    this.email = '';
    this.pass = '';
}

  createUser(){
    this.userService.createUser(this.nick, this.name, this.lastname, this.email, this.pass, this.userType).subscribe({
      next: _ => {
        this.router.navigate(['/login']);
      },
      error: (err: HttpErrorResponse) => {
        if(err.status === 400){
          console.log('Fill all the fields');
          this.router.navigateByUrl('/', {skipLocationChange: true}).then(() => {
            this.router.navigate(['/register']);
          });

        }else if (err.status === 409){
          console.log('Conflict error');
          this.router.navigate(['/nickTaken']);

        }else{
            this.router.navigate(['/error']);
        }
      }
    });
  }


}
