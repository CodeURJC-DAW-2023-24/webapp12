import { Component } from '@angular/core';
import { UserService } from '../../service/UserService';
import { User } from '../../entities/user.model';
import { Router, ActivatedRoute } from '@angular/router';

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

  createUser(userType: number, nick: string, name: string, lastname: string, 
    email: string, pass: string){

    this.userService.CreateUser(nick, name, lastname, email, pass, userType).subscribe({
      next: user => {
        console.log('Bad Request error');
        {this.router.navigate(['/login']);}
      },
      error: err => {
        if(err.status === 400){
          {this.router.navigate(['/register']);}

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