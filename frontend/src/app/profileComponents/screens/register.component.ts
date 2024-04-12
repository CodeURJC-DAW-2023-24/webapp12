import { Component } from '@angular/core';
import { UserService } from '../../service/UserService';
import { Router, ActivatedRoute } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  //styleUrl: ''
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

constructor(private userService: UserService, private router: Router, private route: ActivatedRoute) {
    this.userType = -1;
    this.isUser = false; 
    this.nick = '';
    this.name = '';
    this.lastname = '';
    this.email = '';
    this.pass = '';
}

  CreateUser(){
    this.userService.CreateUser(this.nick, this.name, this.lastname, this.email, this.pass, this.userType).subscribe({
      next: _ => {
        console.log('Bad Request error');
        {this.router.navigate(['/login']);}
      },
      error: (err: HttpErrorResponse) => {
        if(err.status === 400){
          console.log('Fill all the fields');
          this.router.navigateByUrl('/', {skipLocationChange: true}).then(() => {
            this.router.navigate(['/register']);
          });

        }else if (err.status === 409){
          console.log('Conflict error');
          {this.router.navigate(['/nickTaken']);}
          
        }else{
            // Handle other errors
            this.router.navigate(['/error']);
        }
      }
    });
  }
    

}