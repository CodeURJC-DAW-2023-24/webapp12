import { Component,OnInit, Renderer2, ElementRef } from '@angular/core';
import { UserService } from '../service/UserService';


@Component({
  selector: 'Login',
  templateUrl: './login.component.html',
  //styleUrl: ''
})
export class Login {
  title = 'frontend';

  public nickname: string;
  public pass: string;

  constructor(private userService: UserService, private renderer: Renderer2, private el: ElementRef) {
    this.nickname = '';
    this.pass = '';
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
    //ENCODE PASSWORD HERE
    this.userService.getUser(this.nickname, this.pass)
  }
}