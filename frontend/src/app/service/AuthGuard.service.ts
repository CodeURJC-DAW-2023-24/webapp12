import { Injectable, InjectionToken } from '@angular/core';
import { ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { Observable, catchError, from, map, of } from 'rxjs';
import { UserService } from './User.service';
import { HotelService } from './Hotel.service';
import { User } from '../entities/user.model';

export const AUTH_GUARD = new InjectionToken<((route: ActivatedRouteSnapshot, state: RouterStateSnapshot) => Observable<boolean> | Promise<boolean> | boolean)>('AuthGuard');

@Injectable({
    providedIn: 'root',
    useFactory: (userService: UserService, hotelService: HotelService, router: Router) => new AuthGuardService(userService, hotelService, router),
    deps: [UserService, HotelService, Router],
})
export class AuthGuardService {

    constructor(private userService: UserService, private hotelService: HotelService,
        private router: Router) { }

    getCurrentUser(): Observable<User | null> {
        return this.userService.getCurrentUser().pipe(
            map((user: User) => user),
            catchError(err => {
                if (err.status === 403) {
                    console.log('Forbidden error');
                } else {
                    console.log('No user logged in');
                }
                this.router.navigate(['/error']);
                return of(null);
            })
        );
    }

    canActivate(
        next: ActivatedRouteSnapshot): Observable<boolean> {
        const roleFromRoute = next.data['role'];
        return this.userService.getCurrentUser().pipe(
            map(user => {
                if (user.rols.includes(roleFromRoute)) {
                    return true;
                } else {
                    this.router.navigate(['/error']);
                    return false;
                }
            }),
            catchError(err => {
                console.log(err);
                return from(this.router.navigate(['/error'])).pipe(
                    map(() => false)
                );
            })
        );
    }
}
