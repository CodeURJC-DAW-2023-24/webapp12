import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { User } from '../entities/user.model';

const BASE_URL = '/api/auth';

@Injectable({ providedIn: 'root' })
export class LoginService {

    logged: boolean;
    user: User;

    constructor(private http: HttpClient) {
        this.user = {} as User;
        this.logged = false;
        this.reqIsLogged();
    }

    reqIsLogged() {

        this.http.get('/api/currentUser', { withCredentials: true }).subscribe(
            response => {
                this.user = response as User;
                this.logged = true;
            },
            error => {
                if (error.status != 404) {
                    console.error('Error when asking if logged: ' + JSON.stringify(error));
                }
            }
        );

    }

    logIn(user: string, pass: string) {

        this.http.post(BASE_URL + "/login", { username: user, password: pass }, { withCredentials: true })
            .subscribe(
                (response) => this.reqIsLogged(),
                (error) => alert("Wrong credentials")
            );

    }

    logOut() {

        return this.http.post(BASE_URL + '/logout', { withCredentials: true })
            .subscribe((resp: any) => {
                this.logged = false;
                this.user = {} as User;
            });

    }

    isLogged() {
        return this.logged;
    }

    isUser() {
        return this.user && this.user.rols.indexOf('USER') !== -1;
    }

    isClient() {
        return this.user && this.user.rols.indexOf('CLIENT') !== -1;
    }

    isManager() {
        return this.user && this.user.rols.indexOf('MANAGER') !== -1;
    }

    isAdmin() {
        return this.user && this.user.rols.indexOf('ADMIN') !== -1;
    }

    currentUser() {
        return this.user;
    }
}
