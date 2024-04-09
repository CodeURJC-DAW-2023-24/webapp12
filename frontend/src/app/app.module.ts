import { FormsModule } from '@angular/forms';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';

import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { Header } from './profileScreens/header.component';
import { Footer } from './profileScreens/footer.component';
import { Login } from './profileScreens/login.component'; 
import { Profile } from './profileScreens/profile.component';  

@NgModule({
  declarations: [
    AppComponent,
    Header,
    Login,
    Footer,
    Profile,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    NgbModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
