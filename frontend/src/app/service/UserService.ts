import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  constructor(private http: HttpClient) {}

  Login(username: string, password: string) : Observable<string>{
    console.log("Login service");
    return this.http.post<string>('/api/login', {username, password});

    
  } 
}