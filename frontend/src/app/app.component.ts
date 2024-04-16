import { Component } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'frontend';


  constructor(
    private router: Router, private route: ActivatedRoute){
      
    }
  
  
    isProfilePage(): boolean {
      const profileRoutes = ['login', 'profile', 'error', 'register', 
      'loginError', 'nickTaken', 'addHotel', 'viewHotelsManager', 
      'editProfile/:userId', 'clientReservations/:userId', 
      'editHotel/:hotelId', 'addHotel'];
      return profileRoutes.some(routePattern => this.isRouteMatch(routePattern));
    }

    isRouteMatch(routePattern: string): boolean {
      const urlTree = this.router.parseUrl(this.router.url);
      const urlSegments = urlTree.root.children['primary'].segments.map(segment => segment.path);
      const routePatternSegments = routePattern.split('/');
    
      if (urlSegments.length !== routePatternSegments.length) {
        return false;
      }
    
      for (let i = 0; i < urlSegments.length; i++) {
        if (routePatternSegments[i].startsWith(':')) {
          continue;
        }
    
        if (routePatternSegments[i] !== urlSegments[i]) {
          return false;
        }
      }
    
      return true;
    }
}

