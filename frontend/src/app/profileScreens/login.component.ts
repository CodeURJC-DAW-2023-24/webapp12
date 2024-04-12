import { Component, OnInit, Renderer2, ElementRef } from '@angular/core';
import { UserService } from '../service/UserService';
import { Router, ActivatedRoute } from '@angular/router';



@Component({
  selector: 'Login',
  templateUrl: './login.component.html',
  //styleUrl: ''
})
export class LoginComponent {
  title = 'frontend';

  public username: string;
  public password: string;

  constructor(private userService: UserService, private renderer: Renderer2, private el: ElementRef,
    private router: Router, private route: ActivatedRoute
  ) {
    this.username = '';
    this.password = '';
  }

  ngOnInit() {
    this.loadScript();
  }

  loadScript() {
    const node = this.renderer.createElement('script');
    node.src = 'https://www.google.com/recaptcha/api.js?onload=onLoadCallBack&render=explicit';
    node.type = 'text/javascript';
    node.async = true;
    node.charset = 'utf-8';
    this.renderer.appendChild(this.el.nativeElement, node);
  }

  submitCredentials(){
    this.userService.Login(this.username, this.password).subscribe({
      next: user =>{this.router.navigate(['/profile']);},
      error: err => {this.router.navigate(['/loginError']);}
    });
  }
}