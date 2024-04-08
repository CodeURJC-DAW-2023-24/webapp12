import { Component,OnInit, Renderer2, ElementRef } from '@angular/core';

@Component({
  selector: 'Login',
  templateUrl: './login.component.html',
  //styleUrl: ''
})
export class Login {
  title = 'frontend';

  constructor(private renderer: Renderer2, private el: ElementRef) {}

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
}