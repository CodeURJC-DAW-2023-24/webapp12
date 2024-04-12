import { FormsModule } from '@angular/forms';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { RecaptchaModule } from 'ng-recaptcha';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { HeaderComponent } from './profileComponents/childs/header.component';
import { FooterComponent } from './profileComponents/childs/footer.component';
import { SidebarComponent } from './profileComponents/childs/sidebar.component';
import { LoginComponent } from './profileComponents/screens/login.component'; 
import { ProfileComponent } from './profileComponents/screens/profile.component';  
 
import { ErrorComponent} from './globalComponents/error.component';

import { LoginErrorComponent } from './profileComponents/errors/loginError.component';
import { RegisterComponent } from './profileComponents/screens/register.component';
import { NickTakenComponent } from './profileComponents/errors/nickTaken.component';

import { MainPageComponent } from './mainPageComponents/screens/mainPage.component';
import { FooterMainComponent } from './mainPageComponents/childs/footerMain.component';

@NgModule({
  declarations: [
    //Profile page
    AppComponent,
    HeaderComponent,
    LoginComponent,
    FooterComponent,
    SidebarComponent,
    ProfileComponent,
    ErrorComponent,
    LoginErrorComponent,
    RegisterComponent,
    NickTakenComponent,
    //Hotel page
    MainPageComponent,
    FooterMainComponent,
  ],
  imports: [
    RecaptchaModule,
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
