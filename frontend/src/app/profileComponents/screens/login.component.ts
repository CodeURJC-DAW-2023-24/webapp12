import { Component, Renderer2, ElementRef } from '@angular/core';
import { UserService } from '../../service/User.service';
import { Router, ActivatedRoute } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { LoginService } from '../../service/Login.service';



@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  //styleUrl: ''
})
export class LoginComponent {
  title = 'frontend';

  public username: string;
  public password: string;
  public captchaIsResolved: boolean;

  constructor(private userService: UserService, private renderer: Renderer2, private el: ElementRef,
    private router: Router, private route: ActivatedRoute, public loginService: LoginService
  ) {
    this.username = '';
    this.password = '';
    this.captchaIsResolved = false;
  }

  submitCredentials(event: Event){
    event.preventDefault();
    this.userService.login(this.username, this.password).subscribe({
      next: _ =>{this.router.navigate(['/profile']);},
      error: (err: HttpErrorResponse) => {
        if (err.status === 400) {
          console.log('Bad Request error');
          this.router.navigate(['/error']);

        } else if (err.status === 401) {
          console.log('Unauthorized error');
          this.router.navigate(['/loginError']);

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

  handleResolved(captchaResponse: string | null) {
    if (captchaResponse) {
      this.captchaIsResolved = true;
    }
  }

  handleExpired() {
    this.captchaIsResolved = false;
  }
}
