import { FormsModule } from '@angular/forms';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';

import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { RecaptchaModule } from 'ng-recaptcha';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { HeaderComponent } from './profile/childs/header.component';
import { FooterComponent } from './profile/childs/footer.component';
import { LoginComponent } from './profile/screens/login.component'; 
import { ProfileComponent } from './profile/screens/profile.component';  
import { SidebarComponent } from './profile/childs/sidebar.component'; 
import { ErrorComponent} from './globalComponents/error.component';  
import { LoginErrorComponent } from './profile/errors/loginError.component';
import { RegisterComponent } from './profile/screens/register.component';
import { NickTakenComponent } from './profile/errors/nickTaken.component';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    LoginComponent,
    FooterComponent,
    ProfileComponent,
    SidebarComponent,
    ErrorComponent,
    LoginErrorComponent,
    RegisterComponent,
    NickTakenComponent
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
