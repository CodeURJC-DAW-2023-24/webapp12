import { FormsModule } from '@angular/forms';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';

import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { HeaderComponent } from './globalComponents/header.component';
import { FooterComponent } from './globalComponents/footer.component';
import { LoginComponent } from './profileScreens/login.component'; 
import { ProfileComponent } from './profileScreens/profile.component';  
import { SidebarComponent } from './globalComponents/sidebar.component'; 
import { ErrorComponent} from './globalComponents/error.component';  

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    LoginComponent,
    FooterComponent,
    ProfileComponent,
    SidebarComponent,
    ErrorComponent,
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
